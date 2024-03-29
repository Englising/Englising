import React, { useState,  useRef, useEffect } from 'react';
import WaitChatArea from '../component/chat/WaitChatArea'
import { Link, useParams } from 'react-router-dom';
import axios from 'axios';
import useStomp from "../hooks/useStomp";
import { Client, IMessage } from "@stomp/stompjs";

interface User {
    userId : number;
    nickname: string;
    profileImg: string;
    color: string;
}
interface Room {
    multiPlayId: number;
    roomName: string;
    maxUser: number;
    multiPlayImgUrl: string;
    secret: boolean;
    genre: string;
    password : number;
    managerUserId: number;
    currentUser: User[];
}


const WaitingRoomPage: React.FC = () => {
    const [multiroom, setMultiRoom] = useState<Room | undefined>();
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const [userId, setUserId] = useState<number>(0);
    const params = useParams().multiId;
    const client = useRef<Client>();
    

    // useStomp 훅을 직접 함수 컴포넌트 내에서 호출
    const [connect, disconnect] =useStomp(client, `participant/${params}`, (message: IMessage) => {
        console.log("Received message:", message.body);
        const userData = JSON.parse(message.body);
        const state = userData.kind;

        // const userExists = multiroom?.currentUser.some(user => user.userId === userData.userId);
        // if (!userExists) {
            if (state === 'enter') {
                setMultiRoom(prevState => {
                    if (!prevState) return prevState;
                    const updatedUsers = [...prevState.currentUser, {
                        userId: userData.userId,
                        nickname: userData.nickname,
                        profileImg: userData.profileImg,
                        color: userData.color
                    }];
                    return {
                        ...prevState,
                        currentUser: updatedUsers
                    };
                }); 
            } else if (state === 'leave') {
                //나갈떄..
                disconnect();
            }
        }
    // }
    );

    useEffect(() => {
        axios.get(`https://j10a106.p.ssafy.io/api/multiplay/${params}`)
            .then((response) => {
                setMultiRoom(response.data.data);
                setUserId(parseInt(localStorage.getItem("userId") || "0"));
            });
    }, [params]); // params를 의존성 배열에 추가

    useEffect(()=>{
        console.log("연결 시도")
        console.log(multiroom)
        connect();

        return () => disconnect();
    },[]);

    const leaveRoom = ():void => {
        axios.delete(`https://j10a106.p.ssafy.io/api/multiplay/${params}`)
    }

    const startGame = ():void => {
        window.location.href = `/waitroom/${multiroom?.multiPlayId}`;
    }


    return (
        <div className="bg-black h-svh w-screen m-0 p-0 ">
            <div className='flex flex-col w-full'>
                <div className='flex flex-row pb-6 pt-20 pl-48 h-20 w-full items-center'>
                    <div className='text-secondary-500 font-bold text-xl'>{multiroom?.roomName}</div>
                    <div className='text-white font-semibold text-sm pl-6'>대기방</div>
                    <div className='text-white font-thin text-sm pl-28'>방장이 게임을 시작할 수 있습니다.</div>
                </div>

                <div className='flex flex-row h-4/5'>
                    {/* 멤버 목록 */}
                    <div className='flex flex-col w-1/2'>
                        <div className='pl-52 pt-8 flex flex-row'>
                            <div className='text-white'> 대기 중 멤버</div>
                            <div className='text-secondary-500 pl-8 pr-2'> {multiroom?.currentUser.length}</div>
                            <div className='text-white pr-2'> /</div>
                            <div className='text-white'> {multiroom?.maxUser}</div>
                        </div>
                        {/* <div className='pl-56 pt-8 grid grid-cols-3'> */}
                        {multiroom?.currentUser && multiroom.currentUser.length > 0 ? ( // multiroom 배열이 비어있지 않은 경우에만 map 함수 호출
                                <div className='text-white grid grid-cols-3 gap-6 justify-items-center overflow-y-auto pl-48 pt-10'>
                                    {multiroom.currentUser.map((item) => (
                                        <div className='flex flex-col'>
                                            {/* 색깔 추가해야돼 */}
                                            <div className="w-28 h-28 rounded-full relative justify-center place-self-center" style={{background: item?.color }} >
                                                <img src={item.profileImg} className='w-20 h-20 absolute top-4 left-4  place-self-center'/>
                                            </div>
                                            <div className='text-white w-36 pl-6 pt-4 pb-10'>{item.nickname}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="text-white w-full">대기방 목록이 없습니다.</div>
                            )}
                        {/* </div> */}
                    </div>

                    <div className='pt-12 pl-28 w-2/5'>
                        <WaitChatArea/>
                    </div>
                </div>

                <div className='flex flex-row justify-center pt-10 gap-8'>
                    {/* 방장이면 이거 표시되게 */}
                    {/* {(userId === multiroom?.managerUserId) && */}
                    <button onClick={startGame} className='text-black bg-secondary-500 w-48 h-12 rounded-lg text-sm hover:opacity-50'> 
                        게임시작        
                    </button> 
                    {/* } */}
                    <button onClick={leaveRoom} className='text-black bg-red-400 w-48 h-12 rounded-lg text-sm hover:opacity-50'>
                        <Link to ="/englising/selectMulti">나가기</Link>
                    </button>
                </div>
                    
                
                
            </div>
        </div>
    );
};

export default WaitingRoomPage;