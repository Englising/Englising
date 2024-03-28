package org.englising.com.englisingbe.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.global.exception.ErrorHttpStatus;
import org.englising.com.englisingbe.global.exception.GlobalException;
import org.englising.com.englisingbe.global.exception.GlobalExceptionHandler;
import org.englising.com.englisingbe.user.dto.NicknameResponseDto;
import org.englising.com.englisingbe.user.dto.ProfileDto;
import org.englising.com.englisingbe.user.dto.RandomImgDto;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.ProfileColorRepository;
import org.englising.com.englisingbe.user.repository.ProfileImageRepository;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ProfileImageRepository imageRepository;
    private final ProfileColorRepository profileColorRepository;

    public User getUserById(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new GlobalException(ErrorHttpStatus.USER_NOT_FOUND));

        return user;
    }

    public ProfileDto getProfile(Long userId) {
        User user = getUserById(userId);

        return new ProfileDto(user.getProfileImg(), user.getColor(), user.getNickname());
    }

    public void updateProfile(Long userId, ProfileDto profileDto) {
        User user = getUserById(userId);
        boolean isExist = userRepository.existsByNicknameAndUserIdNot(profileDto.getNickname(), userId);
        if(isExist) {
            throw new GlobalException(ErrorHttpStatus.USER_NICKNAME_DUPLICATED);
        }

        user.updateUser(profileDto.getNickname(), profileDto.getColor(), profileDto.getProfileImg());
    }

    public NicknameResponseDto checkNickname(String nickname, Long userId) {
        boolean isExist = userRepository.existsByNicknameAndUserIdNot(nickname, userId);

        return new NicknameResponseDto(isExist);
    }

    public RandomImgDto getRandomImg() {
        String imgUrl = makeRandomProfileImgUrl();
        String color = makeRandomColor();

        return new RandomImgDto(imgUrl, color);
    }

    public String makeRandomNickname() {
        List<String> adjective = Arrays.asList("귀여운", "행복한", "즐거운", "배고픈", "노란", "동그란", "푸른", "수줍은"
        , "그리운", "배부른", "부자", "깨발랄한", "웃고있는", "해맑은", "슬픈", "반가운", "무서운", "귀여운");
        int adjectiveIdx = (int) (Math.random() * adjective.size());
        String adj = adjective.get(adjectiveIdx);

        List<String> animals = Arrays.asList("강아지", "고양이", "오리", "닭", "사슴", "햄스터", "판다", "곰");
        int animalsIdx = (int) (Math.random() * animals.size());
        String ani = animals.get(animalsIdx);

        String nickname = adj + ani;
        int nickCnt = userRepository.countByNicknameStartingWith(nickname);
        if (nickCnt > 0) {
            return nickname + (nickCnt + 1);
        }

        return nickname;
    }

    public String makeRandomProfileImgUrl() {
        long imgCnt = imageRepository.count();
        long imageId = (long) (Math.random() * imgCnt);

        if(imageId == 0) imageId = 1;

        return imageRepository.findById(imageId)
                .get().getProfileImageUrl();
    }

    public String makeRandomColor() {
        long colorCnt = profileColorRepository.count();
        long colorId = (long) (Math.random() * colorCnt);

        if(colorId == 0) colorId = 1;

        return profileColorRepository.findById(colorId)
                .get().getProfileColor();
    }
}