from typing import List

import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
from dotenv import load_dotenv
import os
import time
import logging

load_dotenv()

SPOTIFY_CLIENT_ID = os.getenv("SPOTIFY_CLIENT_ID")
SPOTIFY_CLIENT_SECRET = os.getenv("SPOTIFY_CLIENT_SECRET")

client_credentials_manager = SpotifyClientCredentials(client_id=SPOTIFY_CLIENT_ID, client_secret=SPOTIFY_CLIENT_SECRET)
spotify = spotipy.Spotify(client_credentials_manager=client_credentials_manager)
limit = 50


def get_albums_by_year(year):
    return get_search_albums("year:" + str(year))


def get_albums_new():
    return get_search_albums("tag:new")


def get_search_albums(query: str) -> List[Album]:
    albums = []
    offset = 0
    total = None
    while total is None or len(albums) < total:
        logging.info(f"[SPOTIFY LOG] : Getting Search Albums for query:{query} offset:{offset}")
        spotify_results = SpotifySearchResponse.parse_obj(
            spotify.search(q=query, type='album', limit=limit, offset=offset))
        if total is None:
            total = spotify_results.albums.total
        albums.extend(spotify_results.albums.items)
        offset += limit
        time.sleep(1)
    return albums


def get_artist(spotify_id):
    logging.info(f"[SPOTIFY LOG] : Getting Artist for spotify_id:{spotify_id}")
    try:
        return Artist.parse_obj(spotify.artist(spotify_id))
    except Exception as e:
        logging.error(f"[SPOTIFY LOG] : FAILED Getting Artist for spotify_id: {spotify_id}. Reason: {e}")
        return None


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