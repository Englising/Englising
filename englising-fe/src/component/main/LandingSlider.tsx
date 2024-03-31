import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/swiper-bundle.min.css';
import SwiperCore, { Autoplay, Pagination, Navigation } from 'swiper';
SwiperCore.use([Autoplay, Pagination, Navigation]);


import explain1 from '../../assets/explain1.jpg';
import explain2 from '../../assets/explain2.jpg';
import explain3 from '../../assets/explain3.jpg';


// npm install swiper@6.8.4 로 설치해야 합니다
const FullScreenSlider = () => {
    return (
        <div className="w-[600px] h-[600px] flex flex-col items-center justify-center relative pt-10">
            <div className="bg-black bg-opacity-50 py-2 px-3 text-white text-xl font-bold absolute top-28 rounded-lg right-6">Englising은 무슨 게임인가요?</div>
            <Swiper
                direction={'horizontal'} // 수직 방향으로 슬라이드 표시
                slidesPerView={1} // 한 번에 보여지는 슬라이드 수를 1로 설정하여 한 페이지 단위로 넘어가도록 합니다.
                spaceBetween={30}
                mousewheel={true}
                pagination={{ clickable: true, bulletClass: 'swiper-pagination-bullet', bulletActiveClass: 'swiper-pagination-bullet-active' }}
                navigation={{ nextEl: '.swiper-button-next', prevEl: '.swiper-button-prev' }} // Navigation 모듈을 사용하여 화살표 추가
                autoplay={{ delay: 10000, disableOnInteraction: true }}
                style={{ height: '80%', width: '90%' }}
                freeMode={false}
            >
                <SwiperSlide>
                    <img src={explain1} alt="Slide 1" style={{ width: '100%', height: '100%', objectFit: 'contain' }} />
                </SwiperSlide>
                <SwiperSlide>
                    <img src={explain2} alt="Slide 2" style={{ width: '100%', height: '100%', objectFit: 'contain' }} />
                </SwiperSlide>
                <SwiperSlide>
                    <img src={explain3} alt="Slide 3" style={{ width: '100%', height: '100%', objectFit: 'contain' }} />
                </SwiperSlide>
                {/* 화살표 추가 */}
                <div className="swiper-button-next" style={{ color: 'white' }}></div>
                <div className="swiper-button-prev" style={{ color: 'white' }}></div>
            </Swiper>
            <style>
                {`
                .swiper-pagination-bullet {
                    background-color: #ffffff; /* 동그라미의 색상을 흰색으로 변경 */
                }

                .swiper-pagination-bullet-active {
                    background-color: #00ffff; /* 현재 페이지를 나타내는 동그라미의 색상을 빨간색으로 변경 */
                }
                `}
            </style>
        </div>
    );
};

export default FullScreenSlider;
