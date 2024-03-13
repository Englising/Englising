package org.englising.com.englisingbe.multiplay.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MultiPlayController {

    @GetMapping("/multiplay/rooms")
    public ResponseEntity getMultiLists(@RequestParam Integer page, @RequestParam Integer size){
        Map<String, Object> room1 = new HashMap<>();
        room1.put("room_id", 1);
        room1.put("room_name", "손민기 들어와");
        room1.put("current_user", 2);
        room1.put("max_user", 6);
        room1.put("multi_img", "multi_img_url1");
        room1.put("genre", "신나는");

        Map<String, Object> room2 = new HashMap<>();
        room2.put("room_id", 2);
        room2.put("room_name", "김아영 들어와");
        room2.put("current_user", 3);
        room2.put("max_user", 5);
        room2.put("multi_img", "multi_img_url2");
        room2.put("genre", "hiphip");

        // Sample room data
        List<Map<String, Object>> rooms = Arrays.asList(room1, room2);

        Map<String, Object> sort = new HashMap<>();
        sort.put("empty", true);
        sort.put("sorted", false);
        sort.put("unsorted", true);

        Map<String, Object> data = new HashMap<>();
        data.put("rooms", rooms);
        data.put("size", 20);
        data.put("number", 0);
        data.put("sort", sort);
        data.put("first", true);
        data.put("last", true);
        data.put("numberOfElements", 1);
        data.put("empty", false);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "멀티 플레이 방 리스트를 가져왔습니다.");
        response.put("data", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/multiplay/createRoom")
    public ResponseEntity createMultiplayRoom(@RequestBody Map<String, Object> requestBody) {
        String roomName = (String) requestBody.get("room_name");
        Integer totalPeople = (Integer) requestBody.get("total_people");
        String genre = (String) requestBody.get("genre");
        Boolean isSecret = (Boolean) requestBody.get("is_secret");
        Integer roomPassword = (Integer) requestBody.get("room_pw");

        // Creating the new room
        Map<String, Object> newRoom = new HashMap<>();
        newRoom.put("multiplay_id", 3);
        newRoom.put("room_name", roomName);
        newRoom.put("total_people", totalPeople);
        newRoom.put("genre", genre);
        newRoom.put("is_secret", isSecret);
        newRoom.put("room_pw", roomPassword);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("rooms", Arrays.asList(newRoom));

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "멀티플레이 방을 생성했습니다.");
        response.put("data", responseData);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/multiplay/room/{multiplayId}")
    public ResponseEntity getMultiplayRoom(@PathVariable Integer multiplayId) {

        Map<String, Object> user1 = new HashMap<>();
        user1.put("track_id", "3");
        user1.put("multiplay_users", Arrays.asList(3, 5, 6, 7, 10));
        user1.put("current_user", 5);
        user1.put("max_user", 6);
        user1.put("multi_img", "multiUrl1");

        List<Map<String, Object>> users = Arrays.asList(user1);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("users", users);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "멀티 플레이 방 정보를 가져왔습니다.");
        response.put("data", responseData);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/multiplay/result")
    public ResponseEntity getMultiResult(@RequestParam Integer multiplayId) {
        Map<String, Object> result = new HashMap<>();
        result.put("is_success", true);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("result", result);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "멀티 플레이 결과를 가져왔습니다.");
        response.put("data", responseData);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
