package org.englising.com.englisingbe.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
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
//    todo. private final S3Service s3Service; s3 이미지 처리

    public ProfileDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.USER_NOT_FOUND));

        return new ProfileDto(user.getProfileImg(), user.getNickname());
    }

    public void updateProfile(String email, ProfileDto profileDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.USER_NOT_FOUND));

        // todo. s3에서 프로필 이미지 삭제
        //  후 새로운 이미지 등록

        user.updateUser(profileDto.getNickname(), profileDto.getProfileImg());
    }
}