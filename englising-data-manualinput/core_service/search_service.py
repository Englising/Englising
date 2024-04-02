from typing import List

import client.spotify_client as spotify
from client.google_trans_client import detect_lyric_language
from client.musix_client import find_lyrics
from client.naver_dict_client import NaverDictionaryCrawler
from client.youtube_crawler import YoutubeCrawler
import client.youtube_client as youtube_client
from database.mysql_manager import Session
import pandas as pd
import nltk
from nltk.corpus import wordnet as wn
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import WordNetLemmatizer
from nltk import pos_tag
from better_profanity import profanity

from dto.lyric_dto import LyricDto
from dto.track_album_artist_dto import TrackAlbumArtistDto

from log.englising_logger import log
from log.log_info import LogList, LogKind

from dto.album_dto import AlbumDto
from dto.artist_dto import ArtistDto
from dto.track_dto import TrackDto, YoutubeDto, MusixMatchDto

import crud.album_crud as album_crud
import crud.artist_crud as artist_crud
import crud.track_crud as track_crud
import crud.word_crud as word_crud
import crud.artist_album_crud as artist_album_crud
import crud.artist_track_crud as artist_track_crud

from model import Album, Artist, Track, Word, TrackWord, ArtistAlbum, ArtistTrack

nltk.download('wordnet')
nltk.download('averaged_perceptron_tagger')
nltk.download('punkt')
nltk.download('stopwords')

naver_scraper = NaverDictionaryCrawler()
stop_words = set(stopwords.words('english'))
lemmatizer = WordNetLemmatizer()
badword_cnt = 0


def get_search_results(track_name: str, artist_name: str) -> bool:
    try:
        dto: TrackAlbumArtistDto = __get_spotify_search_track__(track_name, artist_name)
        dto.track.genre = __figure_track_genre__(dto.track)
        dto.track.youtube_id = __get_youtube_id__(dto).youtube_id
        dto.lyrics = __get_lyrics__(dto)
        model = __save_track_album_artist_models__(dto)
        words = __get_words__(dto)
        if not words:
            return False
        return True
    except Exception as e:
        log(LogList.SEARCH_SERVICE, LogKind.ERROR, "Error: {e}".format(e=e))
        return False


def __get_spotify_search_track__(track_name: str, artist_name: str) -> TrackAlbumArtistDto:
    try:
        spotify_ids = spotify.search_track(track_name, artist_name)
        album_dto: AlbumDto = spotify.get_album_dto_by_spotify_id(spotify_ids["album_spotify_id"])
        track_dto: TrackDto = spotify.get_track_audiofeature_spotify(spotify_ids["track_spotify_id"],
                                                                     spotify.get_track_by_spotify_id_spotify(spotify_ids["track_spotify_id"]))
        artist_dto: ArtistDto = spotify.get_artist_by_spotify_id_spotify(spotify_ids["artist_spotify_id"])
        return TrackAlbumArtistDto(
            track= track_dto,
            album= album_dto,
            artist= artist_dto
        )
    except Exception as e:
        log(LogList.SEARCH_SERVICE, LogKind.ERROR, "Failed Getting Track From Spotify: TIMEOUT {e}".format(e=e))
        return None


def __save_track_album_artist_models__(dto: TrackAlbumArtistDto) -> TrackAlbumArtistDto:
    session = Session()
    try:
        album: Album = (album_crud.get_album_by_spotify_id(dto.album.spotify_id, session)
                        or album_crud.create_album((Album(
                            title=dto.album.title,
                            type=dto.album.type,
                            total_tracks=dto.album.total_tracks,
                            spotify_id=dto.album.spotify_id,
                            cover_image=dto.album.cover_image,
                            release_date=dto.album.release_date,
                        ), session)))
        artist: Artist = (artist_crud.get_artist_by_spotify_id(dto.album.spotify_id, session)
                          or artist_crud.create_artist(Artist(
                        name=dto.artist.name,
                        genres=dto.artist.genres,
                        spotify_id=dto.artist.spotify_id,
                        spotify_popularity=dto.artist.spotify_popularity,
                        image=dto.artist.image
                    ), session))
        track: Track = (track_crud.get_track_by_spotify_id(dto.track.spotify_id, session)
                        or track_crud.create_track(Track(
                            album_id=dto.album.album_id,
                            track_index=dto.track.track_index,
                            title=dto.track.title,
                            spotify_id=dto.track.spotify_id,
                            youtube_id=None,
                            isrc=dto.track.isrc,
                            spotify_popularity=dto.track.spotify_popularity,
                            duration_ms=dto.track.duration_ms,
                            feature_acousticness=dto.track.feature_acousticness,
                            feature_danceability=dto.track.feature_danceability,
                            feature_energy=dto.track.feature_energy,
                            feature_positiveness=dto.track.feature_positiveness
                        ), session))
        if artist_album_crud.get_artist_album_by_artist_id_album_id(artist.artist_id, album.album_id, session) is None:
            artist_album_crud.create_artist_album(ArtistAlbum(
                artist_id=artist.artist_id,
                album_id=album.album_id
            ), session)
        if artist is not None:
            artist_track_crud.create_artist_track(ArtistTrack(
                artist_id=artist.artist_id,
                track_id=track.track_id
            ), session)
        return TrackAlbumArtistDto(
            track=track,
            artist=artist,
            album=album
        )
    except Exception as e:
        log(LogList.SEARCH_SERVICE, LogKind.ERROR, "Failed Saving Track: TIMEOUT {e}".format(e=e))
        return False
    finally:
        session.close()


def __figure_track_genre__(track_dto: TrackDto) -> str:
    music_df = pd.DataFrame([{
        'feature_acousticness': track_dto.feature_acousticness,
        'feature_danceability': track_dto.feature_danceability,
        'feature_energy': track_dto.feature_energy
    }])
    music_df = music_df.fillna('')
    music_df['genre'] = music_df.apply(
        lambda row: __classify_genre__(row['feature_acousticness'], row['feature_danceability'],
                                        row['feature_energy']), axis=1)
    return music_df['genre'].iloc[0]


def __classify_genre__(acousticness, danceability, energy):
    if danceability >= 0.727:
        return 'dance'
    elif acousticness >= 0.171:
        return 'rnb'
    elif energy >= 0.730:
        return 'rock'
    else:
        return 'pop'


def __get_youtube_id__(dto: TrackAlbumArtistDto) -> YoutubeDto:
    return youtube_client.search_youtube(
        dto.track.title,
        dto.artist.name,
        dto.track.duration_ms
    )


def __get_lyrics__(dto: TrackAlbumArtistDto) -> List[LyricDto]:
    lyrics: List[LyricDto] = find_lyrics(MusixMatchDto(
        album=dto.album.title,
        artist=dto.artist.name,
        track_name=dto.track.title,
        track_spotify_id=dto.track.spotify_id,
        track_duration=dto.track.duration_ms
    ))
    for lyric in lyrics:
        if not detect_lyric_language(lyric.en_text):
            log(LogList.SEARCH_SERVICE.name, LogKind.INFO, "Lyric is not english " + dto.track.title)
            return None
    return lyrics


def __get_words__(dto: TrackAlbumArtistDto) -> bool:
    global badword_cnt
    badword_cnt = 0
    session = Session()
    try:
        for lyric in dto.lyrics:
            # 단어로 가사 분리
            origin_words = __extract_words_from_lyric__(lyric.en_text)
            # 만약 욕설이 너무 많은 가사인 경우
            if badword_cnt >= 5:
                log(LogList.SEARCH_SERVICE.name, LogKind.WARNING, f"RatedR Track trackId: {lyric.track_id}")
                return None
            for index, refined_word, origin_word in origin_words:
                # 단어 하나씩 Word Table에 저장
                word = word_crud.find_word_by_en_text(refined_word, session)
                if word is None:
                    word_dto = naver_scraper.get_word_details(refined_word)
                    if word_dto.ko_text_1 is None or word_dto.ko_text_1 == '':
                        continue
                    word = word_crud.__save_word_details__(word_dto, session)
                # TrackWord Table에 저장
                word_crud.create_track_word(TrackWord(
                    track_id=lyric.track_id,
                    lyric_id=lyric.lyric_id,
                    word_id=word.word_id,
                    word_index=index,
                    origin_word=origin_word
                ), session)
        return True
    except Exception as e:
        log(LogList.SEARCH_SERVICE.name, LogKind.INFO, "Error in Word Parsing" + dto.track.title)
        return False
    finally:
        session.close()


def __extract_words_from_lyric__(en_text) -> list:
    words = word_tokenize(en_text)
    tagged_words = pos_tag(words)

    filtered_words_with_index = [
        (index, lemmatizer.lemmatize(word.lower(), __get_wordnet_pos__(tag)), word.lower())
        for index, (word, tag) in enumerate(tagged_words)
        if word.isalpha() and word.lower() not in stop_words and len(word) > 2 and __is_wordnet_word__(word)
    ]
    return filtered_words_with_index


def __extract_words_from_lyric_with_space__(en_text) -> list:
    splitted_words = en_text.split(" ")
    index = -1
    finished_words = []

    for word in splitted_words:
        global badword_cnt
        index += 1
        tokenized_words = word_tokenize(word)
        tagged_words = pos_tag(tokenized_words)
        for word, tag in tagged_words:
            if word.isalpha() and word.lower() not in stop_words and len(word) > 2 and __is_wordnet_word__(word):
                if profanity.contains_profanity(word):
                    badword_cnt += 1
                else:
                    finished_words.append((index, lemmatizer.lemmatize(word.lower(), __get_wordnet_pos__(tag)), word.lower()))
            index += 1
    return finished_words


def __get_wordnet_pos__(treebank_tag):
    if treebank_tag.startswith('J'):
        return wn.ADJ
    elif treebank_tag.startswith('V'):
        return wn.VERB
    elif treebank_tag.startswith('N'):
        return wn.NOUN
    elif treebank_tag.startswith('R'):
        return wn.ADV
    else:
        return wn.NOUN


def __is_wordnet_word__(word):
    return bool(wn.synsets(word))


def __save_word_details__(self, word_dto, session) -> Word:
    word = Word(
        en_text = word_dto.en_text,
        ko_text_1 = word_dto.ko_text_1,
        ko_text_2 = word_dto.ko_text_2,
        ko_text_3 = word_dto.ko_text_3,
        example = word_dto.example
    )
    return word_crud.create_word(word, session)


print(get_search_results("yello", "coldplay"))
