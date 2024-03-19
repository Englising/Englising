package org.englising.com.englisingbe.singleplay.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.global.util.PlayListType;
import org.englising.com.englisingbe.singleplay.dto.response.PlayListResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SinglePlayServiceImpl {

    //TODO 플레이리스트 종류에 따라 결과 반환
    public List<PlayListResponseDto> getPlayList(PlayListType type, Integer page, Integer size){
        switch (type){
            case like -> {
                //TODO Repository에서 유저가 좋아한 목록 최신 순으로 조회
                // 1. LikeRepository에서 created at, updated at 순으로 track id 가져오기
                // 2. TrackRepository에서 trackId로 track 정보 가져오기
                // 3. AlbumRepository에서 trackId로 album 정보 가져오기
                // 4. ArtistTrackRepository에서 TrackId로
                return null;
            }
            case  recent -> {
                //TODO Reppsitory에서 유저가 플레이한 목록 최신 순으로 조회
                return null;
            }
            case recommend -> {
                //TODO FastApi에서 유저의 추천 플레이리스트 가져오기
                return null;
            }
        }
        return null;
    }

}
