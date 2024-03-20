package org.englising.com.englisingbe.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.user.dto.ProfileDto;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
//    todo. private final S3Service s3Service;

    public ProfileDto getProfile(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(); // todo. 에러 코드 지정

        return new ProfileDto(user.getProfileImg(), user.getNickname());
    }

    public void updateProfile(String email, ProfileDto profileDto) {
        User user = userRepository.findByEmail(email).orElseThrow(); // todo. 에러 코드 지정

        // todo. s3에서 프로필 이미지 삭제
        //  후 새로운 이미지 등록

        user.updateUser(profileDto.getNickname(), profileDto.getProfileImg());
    }


//    @Transactional
//    public void updateTrade(Long tradeId, User user, MultipartFile image, String content, int duration) {
//        Trade trade = tradeRepository.findById(tradeId)
//                .orElseThrow(() -> new BaseException(ErrorCode.TRADE_NOT_FOUND));
//        s3Service.deleteFile(trade.getImage());
//        String imagePath = s3Service.saveFile(image);
//        trade.updateTrade(imagePath, content, duration);
//    }



//    public Slice<ReviewResponseDto> getReviewList(Long eventId, Long userId, Pageable pageable) {
//        return reviewRepository.findSliceByEventId(eventId, userId, pageable);
//    }
//
//    @Transactional
//    public void saveReview(Long eventId, Long userId, String content, MultipartFile image) {
//
//        Event event = eventRepository.findById(eventId).orElseThrow(() -> new BaseException(EVENT_NOT_FOUND));
//        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
//
//        String imagePath = null;
//        if (image != null) {
//            imagePath = s3Service.saveFile(image);
//        }
//
//        reviewRepository.save(Review.builder()
//                .content(content)
//                .event(event)
//                .user(user)
//                .image(imagePath)
//                .build());
//    }

    //USER_NOT_FOUND(404, "USER-004", "유저를 찾을 수 없는 경우"),





}
