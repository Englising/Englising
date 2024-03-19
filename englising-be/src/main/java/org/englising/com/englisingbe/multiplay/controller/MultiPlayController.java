package org.englising.com.englisingbe.multiplay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.multiplay.dto.response.MultiPlayListResponseDto;
import org.englising.com.englisingbe.multiplay.entity.MultiPlay;
import org.englising.com.englisingbe.multiplay.repository.MultiPlayRepository;
import org.englising.com.englisingbe.multiplay.service.MultiPlayService;
import org.englising.com.englisingbe.multiplay.service.MultiPlayServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/multiplay")
public class MultiPlayController {

    private final MultiPlayServiceImpl multiPlayService;

    @GetMapping("/rooms")
    @Operation(
        summary = "멀티플레이 대기방 리스트 조회",
        description = "genre 파라미터로 멀티플레이 방 장르를 보내주세요. 페이지네이션이 적용되어 있습니다"
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.HEADER),
        @Parameter(name = "genre", description = "멀티플레이 방 장르"),
        @Parameter(name = "page", description = "페이지 번호", in = ParameterIn.QUERY),
        @Parameter(name = "size", description = "(선택적) 페이지당 컨텐츠 개수, 기본 10", in = ParameterIn.QUERY)
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(
            mediaType = "application/json"
        )
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공"),
    })

    public ResponseEntity getMultiPlayList(@RequestParam String genre, @RequestParam Integer page, @RequestParam Integer size){
        List<MultiPlay> multiPlayLists = multiPlayService.getMultiPlayList(genre, page, size);
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

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PostMapping
    @Operation(
        summary = "멀티플레이 방 만들기",
        description = "멀티플레이 방 생성 시 필요한 방 이름, 총 인원, 장르를 가져옵니다"
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.HEADER),
    })
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(
            mediaType = "application/json"
        )
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    public ResponseEntity createMultiPlayRoom(@RequestBody Map<String, Object> requestBody) {
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

    @GetMapping("/room/{multiplayId}")
    @Operation(
        summary = "멀티플레이 방 조회",
        description = " "
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.HEADER),
        @Parameter(name = "mulitplay_id", description = "멀티플레이 방 아이디"),
        })
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(
            mediaType = "application/json"
        )
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    public ResponseEntity getMultiPlayRoom(@PathVariable Integer multiplayId) {

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

    @GetMapping("/result")
    @Operation(
        summary = "멀티플레이 종료 및 결과",
        description = "멀티플레이 성공 여부"
    )
    @Parameters({
        @Parameter(name = "token", description = "JWT AccessToken", in = ParameterIn.HEADER),
        @Parameter(name = "multiplayId", description = "멀티플레이 아이디", in = ParameterIn.QUERY),
    })
    public ResponseEntity getMultiPlayResult(@RequestParam Integer multiplayId) {
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