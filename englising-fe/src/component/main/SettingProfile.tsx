import React, { useEffect, useState } from 'react';
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
}

const SettingProfile: React.FC<SettingProfileProps> = (props) => {
    const [profile, setProfile] = useState<Profile | undefined>();
    const { open, close} = props;

    useEffect(() => {
        if (open) { // 모달이 열려있는 경우에만 데이터를 가져오도록 수정
            axios.get("https://j10a106.p.ssafy.io/api/user/profile")
                .then((response) => {
                    setProfile(response.data.data); // Response.data.data 대신 Response.data로 수정
                })
                .catch((error) => {
                    console.error('프로필 가져오기 실패', error);
                });
        }
    }, [open]); // open 상태가 변경될 때마다 호출되도록 useEffect 의존성 배열에 추가

    const getRandomProfile = async () => {
        axios.get("https://j10a106.p.ssafy.io/api/user/profile/random")
            .then((Response) => {
                setProfile(Response.data.data);
            })
    };

    return (
        <div className={`modal ${open ? "openModal" : ""} bg-black text-white`}>
            회원 정보 수정
            {profile && ( // 프로필 데이터가 있는 경우에만 렌더링
                <div className='relative'>
                    <div className="w-28 h-28 rounded-full relative justify-center place-self-center " style={{background: profile?.color }}>
                    <img src={profile?.profileImg}  className='w-20 h-20 absolute top-4 left-4  place-self-center'/>
                    </div>
                    <div className='absolute top-16 right-28' onClick={getRandomProfile}><RandomButton/></div>
                </div>
                
            )}
            <input placeholder={profile?.nickname} className='placeholder:text-black'></input>
            <button className="close" onClick={close}>
                close
            </button>
        </div>
    );
};

export default SettingProfile;
