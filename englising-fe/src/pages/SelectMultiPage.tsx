import React, { useState, useEffect } from 'react';
import MultiRoom from '../component/main/MultiRoom.tsx';
import axios from 'axios';

interface Room {
    multiPlayId: number;
    roomName: string;
    currentUser: number;
    maxUser: number;
    multiPlayImgUrl: string;
    isSecret: boolean;
    genre: string;
    password : number;
}

interface RoomButtonProps {
    buttonText: string;
    apiEndpoint: string;
    onClick: (endpoint: string) => void; // 클릭 이벤트 핸들러
    selected: boolean; // 선택된 버튼 여부
}

const SelectMultiPage: React.FC = () => {
    const [multiroom, setMultiRoom] = useState<Room[]>([]);
    const [selectedButton, setSelectedButton] = useState<string>(''); // 선택된 버튼 상태

    useEffect(() => {
        axios.get(`https://j10a106.p.ssafy.io/api/multiplay/rooms?genre=all&page=0&size=1000`, {
            withCredentials: true, 
        })
            .then((response) => {
                // 응답 받아서 리스트에 넣기
                setMultiRoom(response.data.data);
                setSelectedButton("all");
                console.log('대기방 가져오기 성공');
            })
            .catch((error) => {
                // 오류 처리
                console.error(`대기방 목록 가져오기 실패`, error);
            });
    }, []); 

    const handleClick = async (endpoint: string) => {
        try {
            // API 호출
            const response = await axios.get(`https://j10a106.p.ssafy.io/api/multiplay/rooms?genre=${endpoint}&page=0&size=1000`, {
                withCredentials: true, 
            });

            // 응답 받아서 리스트에 넣기
            setMultiRoom(response.data.data);
            setSelectedButton(endpoint);
        } catch (error) {
            // 오류 처리
            console.error(`대기방 목록 가져오기 실패`, error);
        }
    };

    const RoomButton: React.FC<RoomButtonProps> = ({ buttonText, apiEndpoint, onClick, selected }) => {
        return (
            <button className={`text-black ${selected ? 'bg-secondary-500' : 'bg-primary-500'} w-24 h-7 rounded-full text-sm hover:opacity-50`} onClick={() => onClick(apiEndpoint)}>
                {buttonText}
            </button>
        );
    };
    
    return (
        <div className="bg-black h-screen w-screen m-0 p-0 flex">
            <div className='flex flex-col pt-10 pl-8'>

                <div className='pt-8 flex flex-row'>
                    {/* 대기방 목록 */}
                    <div>            
                        <div className='flex flex-row pb-6 w-full'>
                            <div className='text-white font-bold text-xl align-middle pr-36'>단체 플레이</div>
                            <div className='text-white font-thin text-sm align-middle'>참여할 방을 선택해 입장해주세요!</div>
                        </div>
                        {/* 장르 선택 버튼 */}
                        <div className='flex flex-row gap-6 pb-6'>
                            <RoomButton buttonText="All" apiEndpoint="all" onClick={handleClick} selected={selectedButton === "all"} />
                            <RoomButton buttonText="POP" apiEndpoint="pop"  onClick={handleClick} selected={selectedButton === "pop"} />
                            <RoomButton buttonText="R&B" apiEndpoint="rnb" onClick={handleClick} selected={selectedButton === "rnb"} />
                            <RoomButton buttonText="ROCK" apiEndpoint="rock"  onClick={handleClick} selected={selectedButton === "rock"} />
                            <RoomButton buttonText="DANCE" apiEndpoint="dance" onClick={handleClick} selected={selectedButton === "dance"} />

                        </div>
                        <div className="relative flex flex-col">
                            {multiroom && multiroom.length > 0 ? ( // multiroom 배열이 비어있지 않은 경우에만 map 함수 호출
                                <div className='text-white grid grid-cols-4 gap-9 justify-items-start overflow-y-auto scrollbar-webkit'>
                                    {multiroom.map((item) => (
                                        <MultiRoom key={item.multiPlayId} roomName={item.roomName} roomId={item.multiPlayId} maxUser={item.maxUser} currentUser={item.currentUser} multiPlayImgUrl={item.multiPlayImgUrl} isSecret={item.isSecret} genre={item.genre} password={item.password}/>
                                    ))}
                                </div>
                            ) : (
                                <div className="text-white w-full">대기방 목록이 없습니다.</div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SelectMultiPage;
