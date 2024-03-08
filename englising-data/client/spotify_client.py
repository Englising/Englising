from typing import List
import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
from dotenv import load_dotenv
import os

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


def get_albums_by_year(year):
    return get_search_albums("year:" + str(year))


def get_albums_new():
    return get_search_albums("tag:new")


def get_search_albums(query: str) -> List[AlbumDto]:
    albums: List[AlbumDto] = []
    offset = 0
    total = None
    while total is None or len(albums) < total:
        spotify_results = spotify.search(q=query, type='album', limit=limit, offset=offset)["albums"]
        if total is None:
            total = spotify_results["total"]
        albums.extend(transfer_to_album_dto(spotify_results))
        offset += limit
    return albums


def transfer_to_album_dto(result):
    album_list = []
    items = result["items"]
    for item in items:
        artists: List[str] = []
        for artist in item["artists"]:
            artists.append(artist["id"])
        album_list.append(AlbumDto(
            album_id = 0,
            artists = artists,
            title = item["name"],
            type = item["type"],
            total_tracks = item["total_tracks"],
            spotify_id = item["id"],
            cover_image = item["images"][0]["url"],
            release_date = item["release_date"]
        ))
    return album_list


print(get_search_albums("year:2023"))

# def get_artist(spotify_id):
#     try:
#         return Artist.parse_obj(spotify.artist(spotify_id))
#     except Exception as e:
#         logging.error(f"[SPOTIFY LOG] : FAILED Getting Artist for spotify_id: {spotify_id}. Reason: {e}")
#         return None
#
#
# def get_album(spotify_id):
#     logging.info(f"[SPOTIFY LOG] : Getting Album for spotify_id:{spotify_id}")
#     track_ids = []
#     try:
#         items = spotify.album(spotify_id)['tracks']['items']
#         for item in items:
#             track_ids.append(item['id'])
#         return track_ids
#     except Exception as e:
#         logging.error(f"[SPOTIFY LOG] : FAILED  Getting Album for spotify_id: {spotify_id}. Reason: {e}")
#         return None
#
#
# def get_track_audiofeature(spotify_id):
#     logging.info(f"[SPOTIFY LOG] : Getting Track AudioFeature for spotify_id:{spotify_id}")
#     try:
#         print(spotify.audio_features(spotify_id))
#         return AudioFeatures.parse_obj(spotify.audio_features(spotify_id)[0])
#     except Exception as e:
#         logging.error(f"[SPOTIFY LOG] : FAILED Getting Track AudioFeature for spotify_id: {spotify_id}. Reason: {e}")
#         return None
#
#
# def get_track(spotify_id):
#     logging.info(f"[SPOTIFY LOG] : Getting Track for spotify_id:{spotify_id}")
#     try:
#         return TrackResponse.parse_obj(spotify.track(spotify_id))
#     except Exception as e:
#         logging.error(f"[SPOTIFY LOG] : FAILED Getting Track for spotify_id: {spotify_id}. Reason: {e}")
#         return None