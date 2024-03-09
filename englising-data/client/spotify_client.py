from typing import List
import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
from dotenv import load_dotenv
import os

from log.log_info import LogList, LogKind
from log.englising_logger import log

from dto.artist_dto import ArtistDto
from dto.job_dto import ArtistJobDto
from dto.track_dto import TrackDto
from dto.album_dto import AlbumDto

load_dotenv()

SPOTIFY_CLIENT_ID = os.getenv("SPOTIFY_CLIENT_ID")
SPOTIFY_CLIENT_SECRET = os.getenv("SPOTIFY_CLIENT_SECRET")

client_credentials_manager = SpotifyClientCredentials(client_id=SPOTIFY_CLIENT_ID, client_secret=SPOTIFY_CLIENT_SECRET)
spotify = spotipy.Spotify(client_credentials_manager=client_credentials_manager)
limit = 50


def get_albums_by_year(year) -> List[str]:
    return get_search_albums("year:" + str(year))


def get_albums_new() -> List[str]:
    return get_search_albums("tag:new")


def get_search_albums(query: str) -> List[str]:
    log(LogList.SPOTIFY, LogKind.INFO, "Getting search albums {query}".format(query=query))
    try:
        albums: List[str] = []
        offset = 0
        total = None
        while total is None or len(albums) < total:
            spotify_results = spotify.search(q=query, type='album', limit=limit, offset=offset)["albums"]
            if total is None:
                total = spotify_results["total"]
            for item in spotify_results["items"]:
                albums.append(item["id"])
            offset += limit
        return albums
    except Exception as e:
        log(LogList.SPOTIFY, LogKind.ERROR, "Failed search albums {e}".format(e=e))
        return None


def get_album_by_spotify_id(spotify_id: str) -> ArtistJobDto:
    log(LogList.SPOTIFY, LogKind.INFO, "Getting album {id}".format(id=spotify_id))
    try:
        result = spotify.album(spotify_id)
        artists: List[str] = []
        tracks: List[str] = []
        for artist in result["artists"]:
            artists.append(artist["id"])
        for track in result["tracks"]["items"]:
            tracks.append(track["id"])
        album = AlbumDto(
            title = result["name"],
            type = result["type"],
            total_tracks = result["total_tracks"],
            spotify_id = result["id"],
            cover_image = result["images"][0]["url"],
            release_date = result["release_date"]
        )
        return ArtistJobDto(
            album = album,
            artists = artists,
            tracks = tracks
        )
    except Exception as e:
        log(LogList.SPOTIFY, LogKind.ERROR, "Failed album {e}".format(e=e))
        return None


def get_artist_by_spotify_id(spotify_id) -> ArtistDto:
    log(LogList.SPOTIFY, LogKind.INFO, "Getting artist {id}".format(id=spotify_id))
    try:
        result = spotify.artist(spotify_id)
        print(result)
        return ArtistDto(
            name=result["name"],
            genres=str(result["genres"]),
            spotify_id=result["id"],
            spotify_popularity=result["popularity"],
            image=result["images"][0]["url"]
        )
    except Exception as e:
        log(LogList.SPOTIFY, LogKind.ERROR, "Failed artist {e}".format(e=e))
        return None


def get_track(spotify_id) -> TrackDto:
    log(LogList.SPOTIFY, LogKind.INFO, "Getting track {id}".format(id=spotify_id))
    try:
        result = spotify.track(spotify_id)
        track = TrackDto(
            track_index = result["track_number"],
            title = result["name"],
            spotify_id = result["id"],
            youtube_id = "",
            isrc = result["external_ids"]["isrc"],
            spotify_popularity = result["popularity"],
            duration_ms = result["duration_ms"],
            is_lyrics = False,
            artists = []
        )
        for artist in result["artists"]:
            track.artists.append(artist["id"])
    except Exception as e:
        log(LogList.SPOTIFY, LogKind.ERROR, "Failed track {e}".format(e=e))
        return None


def get_track_audiofeature(spotify_id:str, track:TrackDto) -> TrackDto:
    log(LogList.SPOTIFY, LogKind.INFO, "Getting track audiofeatures {id}".format(id=spotify_id))
    try:
        result = spotify.audio_features(spotify_id)
        track.feature_acousticness = result["acousticness"]
        track.feature_danceability = result["danceability"]
        track.feature_energy = result["energy"]
        track.feature_positiveness = result["valence"]
        return track
    except Exception as e:
        log(LogList.SPOTIFY, LogKind.ERROR, "Failed track audiofeatures {e}".format(e=e))
        return None



