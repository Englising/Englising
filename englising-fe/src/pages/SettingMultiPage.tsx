import React, { useState, ChangeEvent, useEffect } from 'react';
import Multiroom from '../component/main/MultiRoom.tsx';
import RandomButton from '../component/main/RandomButton.tsx';
import axios from 'axios';

const SettingMulti = () => {
    const [roomImg, setRoomImg] = useState<string>("");
    
    //일단 이 페이지 들어오면 roomImg api로 요청해서 바꿈
    useEffect(() => {
        axios.get("https://j10a106.p.ssafy.io/api/multiplay/image")
            .then((Response) => {
                setRoomImg(Response.data.data);
                console.log(roomImg)
            })
    }, []);

    const changeRoomImg = async() => {
        axios.get("https://j10a106.p.ssafy.io/api/multiplay/image")
            .then((Response) => {
                setRoomImg(Response.data.data);
                console.log(roomImg)
            })
    };

    const [roomInfo, setRoomInfo] = useState({
        roomName: "방 이름", // 방 이름
        maxUser: 1, // 최대 사용자 수
        currentUser: 1, // 현재 사용자 수
        password: 1111,
        roomImg: roomImg,
    });

    // 방 이름 변경 시 호출되는 함수
    const handleRoomNameChange = (event: ChangeEvent<HTMLInputElement>) => {
        setRoomInfo({ ...roomInfo, roomName: event.target.value });
    };

    // 인원 수 변경 시 호출되는 함수
    const handleMaxUserChange = (event: ChangeEvent<HTMLInputElement>) => {
        setRoomInfo({ ...roomInfo, maxUser: parseInt(event.target.value) });
    };

    const finishSetting = async () => {
        if(roomInfo.roomName===""){
            alert("방 이름을 입력해주세요!");
        } 
        else{
            axios.post("https://j10a106.p.ssafy.io/api/multiplay",{
                roomName: roomInfo.roomName, // 방 이름
                maxUser: roomInfo.maxUser, // 최대 사용자 수
                currentUser: roomInfo.currentUser, // 현재 사용자 수
                password: roomInfo.password,
                roomImg: roomImg,
            })
            //request로 multiplayId 받아서 waitroom/multiplayId로 보내주기
        }
    }

    return (
        <div className="bg-black h-svh w-screen m-0 p-0 flex">
            <div className='pl-8'>
                <div className='flex flex-row'>
                    {/* 설정할 카테고리 */}
                    <div className='flex flex-col pt-20 pl-6'>
                        <h1 className='text-white font-bold text-xl w-48 pb-16'>단체 플레이</h1>
                        <div className='pl-3 font-extrabold text-secondary-500 space-y-20 pb-20'>
                            <h1>인원 수</h1>
                            <h1>방 이름</h1>
                            <h1>공개 범위</h1>
                            <h1>비밀 번호</h1>
                        </div>
                        
                    </div>
                    {/* 설정하기 */}
                    <div className='flex flex-col pt-40 space-y-12'>
                        {/* 인원수 설정 */}
                        <div className='flex flex-row pb-5'>
                            <div>
                                <div className='text-white flex flex-row space-x-12'>
                                    <h1>2</h1>
                                    <h1>3</h1>
                                    <h1>4</h1>
                                    <h1>5</h1>
                                    <h1>6</h1>
                                </div>
                                <input
                                    type="range"
                                    min="2"
                                    max="6"
                                    step="1"
                                    value={roomInfo.maxUser}
                                    onChange={handleMaxUserChange}
                                    className="w-60"
                                />
                            </div>
                            {/* 선택한 숫자 출력 */}
                            <div className='flex flex-row pt-5'>
                                <span className="text-secondary-500 font-bold pl-8">{roomInfo.maxUser}</span>
                                <div className='text-white'>명</div>
                            </div>
                        </div>
                        {/* 방 이름 설정 */}
                        <input
                            type="text"
                            name="roomname"
                            value={roomInfo.roomName}
                            onChange={handleRoomNameChange}
                            className='w-64 h-10 pl-3 bg-secondary-100 rounded-lg placeholder:text-primary-700 '
                            placeholder='방 이름을 입력하시오'
                        />
                        {/* 공개범위설정 */}
                        <div className='flex flex-row space-x-5 pt-3.5 pb-4'>
                            <button className='text-black bg-secondary-500 w-24 h-7 rounded-full text-sm hover:opacity-50'>전체공개</button>
                            <button className='text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50'>친구만</button>
                        </div>
                        {/* 비밀번호 설정 */}
                        <input
                            type="text"
                            name="room_password"
                            className='w-64 h-10 pl-3 bg-secondary-100 rounded-lg placeholder:text-primary-700 '
                            placeholder='비밀번호를 입력하시오 (4자릿수 숫자)'
                            value={roomInfo.password}
                        />
                        {/* 방 설정완료 */}
                        <div className='pl-28 pt-10' onClick={finishSetting}>
                            <button className='text-black bg-secondary-500 w-48 h-12 rounded-lg text-sm hover:opacity-50'>방 설정 완료</button>
                        </div>
                    </div>
                    {/* 방 미리보기 */}
                    <div className='h-48 pt-52 pl-24 flex flex-col items-center relative'>
                        <Multiroom room_name={roomInfo.roomName} room_id={1} max_user={roomInfo.maxUser} current_user={roomInfo.currentUser} multi_img={roomImg} />
                        <div className='absolute top-[218px] right-[160px]' onClick={changeRoomImg}><RandomButton/></div>
                        <h1 className='text-secondary-500 pt-6 font-semibold'>방 미리보기</h1>
                    </div>
                    
                </div>
            </div>
        </div>
    );
};

export default SettingMulti;
