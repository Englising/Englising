from typing import List
import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
from dotenv import load_dotenv
import os

from dto.artist_dto import ArtistDto
from dto.job_dto import ArtistJobDto
from log.log_info import LogList, LogKind
from log.englising_logger import log

from dto.album_dto import AlbumDto


load_dotenv()

SPOTIFY_CLIENT_ID = os.getenv("SPOTIFY_CLIENT_ID")
SPOTIFY_CLIENT_SECRET = os.getenv("SPOTIFY_CLIENT_SECRET")

client_credentials_manager = SpotifyClientCredentials(client_id=SPOTIFY_CLIENT_ID, client_secret=SPOTIFY_CLIENT_SECRET)
spotify = spotipy.Spotify(client_credentials_manager=client_credentials_manager)
limit = 50

log(LogList.SPOTIFY, LogKind.INFO, "hello")


def get_albums_by_year(year) -> List[str]:
    return get_search_albums("year:" + str(year))


def get_albums_new() -> List[str]:
    return get_search_albums("tag:new")


def get_search_albums(query: str) -> List[str]:
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


def get_album_by_spotify_id(spotify_id: str) -> ArtistJobDto:
    result = spotify.album(spotify_id)
    artists: List[str] = []
    tracks: List[str] = []
    for artist in result["artists"]:
        artists.append(artist["id"])
    for track in result["tracks"]["items"]:
        tracks.append(track["id"])
    album = AlbumDto(
        album_id = -1,
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


def get_artist_by_spotify_id(spotify_id) -> ArtistDto:
    try:
        result = spotify.artist(spotify_id)
        print(result)

    except Exception as e:
        print(e)
    return ArtistDto(
        artist_id = -1,
        name = result["name"],
        genres = str(result["genres"]),
        spotify_id = result["id"],
        spotify_popularity = result["popularity"],
        image = result["images"][0]["url"]
    )


def get_album(spotify_id):
    logging.info(f"[SPOTIFY LOG] : Getting Album for spotify_id:{spotify_id}")
    track_ids = []
    try:
        items = spotify.album(spotify_id)['tracks']['items']
        for item in items:
            track_ids.append(item['id'])
        return track_ids
    except Exception as e:
        logging.error(f"[SPOTIFY LOG] : FAILED  Getting Album for spotify_id: {spotify_id}. Reason: {e}")
        return None


def get_track_audiofeature(spotify_id):
    logging.info(f"[SPOTIFY LOG] : Getting Track AudioFeature for spotify_id:{spotify_id}")
    try:
        print(spotify.audio_features(spotify_id))
        return AudioFeatures.parse_obj(spotify.audio_features(spotify_id)[0])
    except Exception as e:
        logging.error(f"[SPOTIFY LOG] : FAILED Getting Track AudioFeature for spotify_id: {spotify_id}. Reason: {e}")
        return None


def get_track(spotify_id):
    logging.info(f"[SPOTIFY LOG] : Getting Track for spotify_id:{spotify_id}")
    try:
        return TrackResponse.parse_obj(spotify.track(spotify_id))
    except Exception as e:
        logging.error(f"[SPOTIFY LOG] : FAILED Getting Track for spotify_id: {spotify_id}. Reason: {e}")
        return None