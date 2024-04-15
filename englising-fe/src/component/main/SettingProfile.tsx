import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import RandomButton from './RandomButton';

export interface Profile {
    color: string;
    nickname: string;
    profileImg: string;
}

interface SettingProfileProps {
    open: boolean;
    close: () => void;
    updateProfile: (updatedProfile: Profile) => void;
}

const SettingProfile: React.FC<SettingProfileProps> = (props) => {
    const [profile, setProfile] = useState<Profile>({ color: '', nickname: '', profileImg: '' });


    const { open, close, updateProfile } = props;

    const inputRef = useRef < HTMLInputElement | null > (null);

    useEffect(() => {
        if (open) { // 모달 열때 프로필 데이터
            axios.get("https://englising.com/api/user/profile", {withCredentials:true})
                .then((response) => {
                    setProfile(response.data.data); // Response.data.data 대신 Response.data로 수정
                })
                .catch((error) => {
                    console.error('프로필 가져오기 실패', error);
                });
        }
    }, [open]); // open 상태가 변경될 때마다 호출되도록 useEffect 의존성 배열에 추가

    const getRandomProfile = async () => {
        axios.get("https://englising.com/api/user/profile/random", {withCredentials:true})
            .then((Response) => {
                setProfile({ ...profile, profileImg: Response.data.data.profileImg, color: Response.data.data.color});
            })
    };

    const saveProfile = () => {
        if (/^\s+/.test(profile.nickname) || profile.nickname == "") {
            if (inputRef.current != null) {
                setProfile({
                    ...profile,
                    nickname : ""
                })
                inputRef.current.placeholder = "공백 시작 금지!"
            }
            return;
        }

        axios.put("https://englising.com/api/user/profile", profile , {withCredentials:true}) // 프로필 정보 서버에 전송
            .then(() => {
                updateProfile(profile); // 프로필 업데이트 함수 호출하여 사이드바 등에 반영
                close(); // 모달 닫기
            })
            .catch((error) => {
                console.log(error.response.status);
                if (error.response && error.response.request.status === 403) {
                    alert("중복된 닉네임입니다");
                } else {
                    console.error('프로필 업데이트 실패', error);
                }
                // console.error('프로필 업데이트 실패', error);
            });

        if (inputRef.current != null) {
            inputRef.current.placeholder = ""
        }
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if(e.key == 'Enter'){
            saveProfile();
        } 
    }

    return (
        <div className={`modal ${open ? "openModal" : ""} bg-black bg-opacity-70 text-white h-screen w-full fixed left-0 top-0 flex justify-center items-center z-50`}>
            <div className='bg-primary-700 w-[600px] h-80 flex flex-col justify-center items-center rounded-xl relative'>
                <div className='flex flex-row'>
                    <div className='pb-6 text-2xl font-bold'>회원 정보 수정</div>
                    <button className='absolute top-10 right-10 font-bold text-xl' onClick={close}>
                        X
                    </button>
                </div>
                {profile && (
                    <div className='flex flex-row'>
                        <div className='relative pb-12 pr-12'>
                            <div className="w-28 h-28 rounded-full relative justify-center place-self-center " style={{ background: profile?.color }}>
                                <img src={profile?.profileImg} className='w-20 h-20 absolute top-4 left-4  place-self-center' />
                            </div>
                            <div className='absolute top-20 right-11' 
                            onClick={getRandomProfile}>
                                <RandomButton/>
                            </div>
                        </div>
                        <div className='justify-center pt-2 pl-8'>
                            <div>닉네임</div>
                            <div className='flex flex-row gap-4'>
                                <input 
                                    ref={inputRef}
                                    value={profile.nickname}
                                    placeholder=""
                                    onKeyDown={(event) => handleKeyDown(event)}
                                    className='placeholder:text-primary-500 text-black h-14 w-36 rounded-xl pl-4'
                                    onChange={(e) => setProfile({ ...profile, nickname: e.target.value })}>
                                </input>
                            </div>
                        </div>
                    </div>
                )}
                <button className='text-secondary-500 font-bold text-xl' onClick={saveProfile}>저장</button> {/* 저장 버튼 추가 */}
            </div>
        </div>
    );
};

export default SettingProfile;
