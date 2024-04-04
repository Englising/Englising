import React from "react";
import KakaoLogin from "../assets/kakao_login_large_narrow.png";
import landingVideo from "../assets/landingMp4.mp4";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import LandingSlider from "../component/main/LandingSlider";

const IndexPage = () => {
  const navigate = useNavigate();
  const handleKakaoLogin = () => {
    window.location.href = "https://j10a106.p.ssafy.io/api/oauth2/authorization/kakao";
  };

  const guestLogin = async () => {
    axios
      .post("https://j10a106.p.ssafy.io/api/auth/guest", {
        withCredentials: true, // 이 옵션을 설정하여 쿠키와 인증 정보를 함께 보냄
      })
      .then((response) => {
        // 유저아이디 받아서 로컬에 저장
        localStorage.setItem("userId", response.data.data.userId.toString());
        location.reload();
      });

    navigate("/englising/selectSingle1");
  };

  return (
    <div className="bg-black h-svh w-screen m-0 p-0 flex items-center justify-center relative">
      <video autoPlay muted playsInline className="flex absolute inset-0 w-full h-full object-cover p-6 pb-12">
        <source src={landingVideo}></source>
      </video>
      <div className="flex flex-row z-50 pb-24 pt-16 w-full h-full">
        <div className="flex flex-row gap-5 self-end pl-[135px] pr-[40px] pb-12">
          {/* 카카오로그인 */}
          <img src={KakaoLogin} className="h-16 w-56" onClick={handleKakaoLogin} />
          {/* 게스트로그인 */}
          <div
            className="flex flex-row items-center justify-center h-16 w-56 bg-gray-300 text-center text-black text-base font-normal rounded-lg z-50 "
            onClick={guestLogin}
          >
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-8 h-8">
              <path
                fill-rule="evenodd"
                d="M18.685 19.097A9.723 9.723 0 0 0 21.75 12c0-5.385-4.365-9.75-9.75-9.75S2.25 6.615 2.25 12a9.723 9.723 0 0 0 3.065 7.097A9.716 9.716 0 0 0 12 21.75a9.716 9.716 0 0 0 6.685-2.653Zm-12.54-1.285A7.486 7.486 0 0 1 12 15a7.486 7.486 0 0 1 5.855 2.812A8.224 8.224 0 0 1 12 20.25a8.224 8.224 0 0 1-5.855-2.438ZM15.75 9a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0Z"
                clip-rule="evenodd"
              />
            </svg>
            <h1 className="pl-6 text-xl">게스트 로그인</h1>
          </div>
        </div>
        <div className="pr-16 z-50 w-1/2 h-5/6 self-center pl-3 pb-3">
          <LandingSlider />
        </div>
      </div>
    </div>
  );
};

export default IndexPage;
