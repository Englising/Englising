import React from 'react';
import Logo from '../assets/logo.png'
import KakaoLogin from '../assets/kakao_login_large_narrow.png'

const IndexPage = () => {
    const handleKakaoLogin = () => {
        window.location.href = "https://j10a106.p.ssafy.io/oauth2/authorization/kakao"
    }

    return (
        <div className="bg-black h-svh w-screen m-0 p-0 flex items-center justify-center">
            <div className='pt-18 flex flex-col justify-center items-center'>
                <img src={Logo} className='h-36 w-auto'/>   
                <div className='pt-16'>
                    <img src={KakaoLogin} className='h-16 w-auto' onClick={handleKakaoLogin}/>   
                </div>
                
            </div>
            
        </div>
    );
};

export default IndexPage;