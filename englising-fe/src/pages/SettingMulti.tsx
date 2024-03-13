import React, { useState } from 'react';
import Sidebar from '../component/main/Sidebar.tsx';
import imgRoom6 from '../assets/imgRoom6.jpg';
import Multiroom from '../component/main/MultiRoom.tsx';

const SettingMulti = () => {
    // 슬라이더 값 상태를 관리하기 위한 useState 훅 사용
    const [sliderValue, setSliderValue] = useState<number>(1);

    // 슬라이더 값이 변경될 때 호출되는 함수
    const handleSliderChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        // 변경된 슬라이더 값으로 상태 업데이트
        setSliderValue(parseInt(event.target.value));
    };

    return (
        <div className="bg-black h-svh w-screen m-0 p-0 flex">
            <Sidebar/>

            <div className='pl-8'>
                <div className='flex flex-row'>
                    {/* 설정할 카테고리 */}
                    <div className='flex flex-col pt-28 pl-6'>
                        <h1 className='text-white font-bold text-xl w-48 pb-16'>단체 플레이</h1>
                        <div className='pl-3 font-extrabold text-secondary-500 space-y-20'>
                            <h1>인원 수</h1>
                            <h1>방 이름</h1>
                            <h1>공개 범위</h1>
                            <h1>비밀 번호</h1>
                        </div>
                    </div>
                    {/* 설정하기 */}
                    <div className='flex flex-col pt-44  space-y-14'>
                        {/* 인원수 설정 */}
                        <div className='flex flex-row pb-5'>
                            <div>
                                <div className='text-white flex flex-row space-x-56'>
                                    <h1>1</h1>
                                    <h1>6</h1>
                                </div>
                                <input 
                                    type="range" 
                                    min="1" 
                                    max="6" 
                                    step="1" 
                                    value={sliderValue} 
                                    onChange={handleSliderChange} 
                                    className="w-60"
                                />
                            </div>
                            {/* 선택한 숫자 출력 */}
                            <div className='flex flex-row pt-5'>
                                <span className="text-secondary-500 font-bold pl-8">{sliderValue}</span>
                                <div className='text-white'>명</div>
                            </div>
                        </div>
                        {/* 방 이름 설정 */}
                        <input
                            type="text"
                            name="roomname"
                            className='w-64 h-10 pl-3 bg-secondary-100 rounded-lg placeholder:text-primary-700 '
                            placeholder='방 이름을 입력하시오'
                            />
                        {/* 공개범위설정 */}
                        <div className='flex flex-row space-x-5 pt-3.5 pb-4'>
                            <button className='text-black bg-secondary-500 w-24 h-7 rounded-full text-sm hover:opacity-50'>All</button>
                            <button className='text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50'>POP</button>
                        </div>
                        {/* 비밀번호 설정 */}
                        <input
                            type="text"
                            name="room_password"
                            className='w-64 h-10 pl-3 bg-secondary-100 rounded-lg placeholder:text-primary-700 '
                            placeholder='비밀번호를 입력하시오'
                            />
                        
                    </div>
                    {/* 방 미리보기 */}
                    <div className='h-48 pt-52 pl-24 flex flex-col items-center'>
                        <Multiroom room_name="하이" room_id={1} max_user={1} current_user={1} multi_img={imgRoom6}/>
                        <h1 className='text-secondary-500 pt-6 font-semibold'>방 미리보기</h1>
                    </div>    
                </div>
                
                </div>
        </div>
    );
};

export default SettingMulti;