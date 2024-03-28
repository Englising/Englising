import React from 'react';
import Logo from '../assets/logo.png'
import KakaoLogin from '../assets/kakao_login_large_narrow.png'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const IndexPage = () => {

    const navigate = useNavigate();
    const handleKakaoLogin = () => {
        window.location.href = "https://j10a106.p.ssafy.io/api/oauth2/authorization/kakao"
    }

    const guestLogin = async() => {
        axios.post("https://j10a106.p.ssafy.io/api/auth/guest")
        .then((response) => {
            // 유저아이디 받아서 로컬에 저장
            localStorage.setItem("userId", response.data.data.userId.toString());
        })
        console.log("로그인 성공")
        navigate("/englising/selectSingle1");
    }

    return (
        <div className="bg-black h-svh w-screen m-0 p-0 flex items-center justify-center">
            <div className='pt-18 flex flex-col justify-center items-center'>
                <img src={Logo} className='h-36 w-auto'/>   
                <div className='pt-16'>
                    <img src={KakaoLogin} className='h-16 w-auto' onClick={handleKakaoLogin}/>   
                </div>
                
            </div>
            <div className='pt-16 h-36 w-24 bg-white text-black'
                onClick={guestLogin}>
                게스트로그인
            </div>
        </div>
    );
};

export default IndexPage;