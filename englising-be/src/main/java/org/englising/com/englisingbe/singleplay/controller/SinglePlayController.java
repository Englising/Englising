package org.englising.com.englisingbe.singleplay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SinglePlayController {

    @GetMapping("/playlist")
    public ResponseEntity getPlaylists(@RequestParam Integer page, @RequestParam Integer size){
        Map<String, Object> track = new HashMap<>();
        track.put("albumId", 1);
        track.put("albumTitle", "hero");
        track.put("albumImg", "asdfasdfasd");
        track.put("trackId", 1);
        track.put("trackTitle", "asdf");
        track.put("artists", Arrays.asList("taylor swift", "ddd", "dddd"));
        track.put("spotifyId", "Ado287Dfs");
        track.put("score", 2);
        track.put("isLike", true);

        List<Map<String, Object>> tracks = Arrays.asList(track, track);

        Map<String, Object> sort = new HashMap<>();
        sort.put("empty", true);
        sort.put("sorted", false);
        sort.put("unsorted", true);

        Map<String, Object> data = new HashMap<>();
        data.put("tracks", tracks);
        data.put("size", 20);
        data.put("number", 0);
        data.put("sort", sort);
        data.put("first", true);
        data.put("last", true);
        data.put("numberOfElements", 1);
        data.put("empty", false);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "플레이리스트 정보를 가져왔습니다");
        response.put("data", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

