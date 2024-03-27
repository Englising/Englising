import React, { useState, ChangeEvent, useEffect } from 'react';
import Multiroom from '../component/main/MultiRoom.tsx';
import RandomButton from '../component/main/RandomButton.tsx';
import { Link } from 'react-router-dom';
import axios from 'axios';

const SettingMulti = () => {
    const [roomImg, setRoomImg] = useState<string>("");
    const [isSecret, setIsSecret] = useState<boolean>(false);
    const [multiId, setMultiId] = useState<number>(0);
    
    //일단 이 페이지 들어오면 roomImg api로 요청해서 바꿈
    useEffect(() => {
        axios.get("https://j10a106.p.ssafy.io/api/multiplay/image")
            .then((Response) => {
                setRoomImg(Response.data.data);
                setRoomInfo({ ...roomInfo, roomImg: Response.data.data});
            })
    }, []);

    const changeRoomImg = async() => {
        axios.get("https://j10a106.p.ssafy.io/api/multiplay/image")
            .then((Response) => {
                setRoomImg(Response.data.data);
                setRoomInfo({ ...roomInfo, roomImg: Response.data.data});
            })
    };

    const [roomInfo, setRoomInfo] = useState({
        roomId:0,
        genre: "pop",
        roomName: "", // 방 이름
        maxUser: 2, // 최대 사용자 수
        currentUser: 1, // 현재 사용자 수
        password: "",
        isSecret: false,
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

    //공개범위 변경
    const handlePublicityChange = (isPublic: boolean) => {
        setIsSecret(!isPublic); // 공개 여부 상태 업데이트
        setRoomInfo({ ...roomInfo, isSecret: !isPublic }); // roomInfo 업데이트
    };

    //비밀번호 설정
    const handlePasswordChange = (event: ChangeEvent<HTMLInputElement>) => {
        setRoomInfo({ ...roomInfo, password: event.target.value });
    };

    const handleGenreChange = (genre: string) => {
        setRoomInfo({ ...roomInfo, genre });
    };    


    const finishSetting = async () => {
        if(roomInfo.roomName===""){
            alert("방 이름을 입력해주세요!");
        } 
        else{
            console.log(roomInfo)
            axios.post("https://j10a106.p.ssafy.io/api/multiplay",{
                roomName: roomInfo.roomName, // 방 이름
                maxUser: roomInfo.maxUser, // 최대 사용자 수
                currentUser: roomInfo.currentUser, // 현재 사용자 수
                isSecret: roomInfo.isSecret,
                password: roomInfo.password,
                multiPlayImgUrl: roomInfo.roomImg,
                genre: roomInfo.genre,
            })
            .then((Response) => {
                setMultiId(Response.data.data);
                console.log(Response.data.data);
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
                            {roomInfo.isSecret ? <h1>비밀 번호</h1> : <div></div>}
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
                        {/* 장르 설정 */}
                        <div>
                        <button 
                            className={`text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50 ${roomInfo.genre === 'rock' && 'bg-secondary-500 text-black border-0'}`} // hiphop 장르가 선택된 경우 스타일 적용
                            onClick={() => handleGenreChange('rock')} // hiphop 버튼 클릭 시
                        >
                            ROCK
                        </button>
                        <button 
                            className={`text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50 ${roomInfo.genre === 'pop' && 'bg-secondary-500 text-black border-0'}`} // pop 장르가 선택된 경우 스타일 적용
                            onClick={() => handleGenreChange('pop')} // pop 버튼 클릭 시
                        >
                            POP
                        </button>
                        <button 
                            className={`text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50 ${roomInfo.genre === 'rnb' && 'bg-secondary-500 text-black border-0'}`} // rnb 장르가 선택된 경우 스타일 적용
                            onClick={() => handleGenreChange('rnb')} // rnb 버튼 클릭 시
                        >
                            R&B
                        </button>
                        <button 
                            className={`text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50 ${roomInfo.genre === 'dance' && 'bg-secondary-500 text-black border-0'}`} // dance 장르가 선택된 경우 스타일 적용
                            onClick={() => handleGenreChange('dance')} // dance 버튼 클릭 시
                        >
                            DANCE
                        </button>
                        </div>

                        {/* 공개범위설정 */}
                        <div className='flex flex-row space-x-5 pt-3.5 pb-4'>
                        <button 
                            className={`text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50 ${!isSecret && 'bg-secondary-500 text-black border-0'}`} // isSecret이 false면 선택된 스타일을 적용
                            onClick={() => handlePublicityChange(true)} // 전체공개 버튼 클릭 시 공개 상태로 변경
                        >
                            전체공개
                        </button>
                        <button 
                            className={`text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50 ${isSecret && 'bg-secondary-500 text-black border-0'}`} // isSecret이 true면 선택된 스타일을 적용
                            onClick={() => handlePublicityChange(false)} // 친구만 버튼 클릭 시 비공개 상태로 변경
                        >
                            친구만
                        </button>                 
                        </div>
                        {/* 비밀번호 설정 */}
                        {roomInfo.isSecret ? <input
                            type="text"
                            name="room_password"
                            className='w-64 h-10 pl-3 bg-secondary-100 rounded-lg placeholder:text-primary-700 text-black'
                            placeholder='비밀번호를 입력하시오 (4자릿수 숫자)'
                            value={roomInfo.password}
                            onChange={handlePasswordChange}
                        /> : <div></div>}
                        
                        {/* 방 설정완료 */}
                        <div className='pl-28 pt-10' onClick={finishSetting}>
                            <button className='text-black bg-secondary-500 w-48 h-12 rounded-lg text-sm hover:opacity-50'>
                                <p>
                                    <Link to ={`/waitroom/${multiId}`}>방 설정 완료</Link>
                                </p>        
                            </button>
                        </div>
                    </div>
                    {/* 방 미리보기 */}
                    <div className='h-48 pt-48 pl-24 flex flex-col items-center relative'>
                        <h1 className='text-white pb-5 text-sm'>↓ 랜덤 버튼을 눌러서 방 이미지를 바꿔봐요! ↓</h1>
                        <Multiroom roomName={roomInfo.roomName} roomId={1} maxUser={roomInfo.maxUser} currentUser={roomInfo.currentUser} multiPlayImgUrl={roomInfo.roomImg} />
                        <div className='absolute top-[250px] right-[35px]' onClick={changeRoomImg}><RandomButton/></div>
                        <h1 className='text-secondary-500 pt-6 font-semibold'>방 미리보기</h1>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SettingMulti;