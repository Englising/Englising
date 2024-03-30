import React, { useState,  useRef, useEffect } from 'react';
import WaitChatArea from '../component/chat/WaitChatArea'
import {useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import useStomp from "../hooks/useStomp";
import { Client, IMessage } from "@stomp/stompjs";
// import { createBrowserHistory } from "history";
// import useCustomBack from '../hooks/useCustomBack';

interface User {
    userId : number;
    nickname: string;
    profileImg: string;
    color: string;
    isMe : boolean;
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
    const params = useParams().multiId;
    const [userId, setUserId] = useState<number>(0);
    const client = useRef<Client>();
    const navigate = useNavigate();

    // const history = createBrowserHistory();

    // useEffect(()=> {
    //     return history.listen(() => {
    //         if(history.action === "POP"){
    //             console.log("뒤로가기누름!");
    //             axios.delete(`https://j10a106.p.ssafy.io/api/multiplay/${params}`, {withCredentials:true})
    //             navigate("/englising/selectMulti");
    //             disconnect();
    //         }
    //     })
    // })

    // useStomp 훅을 직접 함수 컴포넌트 내에서 호출
    const [connect, disconnect] = useStomp(client, `/sub/participant/${params}`, (message: IMessage) => {
        console.log("Received message:", message.body);
        //메시지 주인공(?)의 userData
        const userData = JSON.parse(message.body);
        const state = userData.kind;

        if (state === 'enter') {
                setMultiRoom(prevState => {
                    if (!prevState) return prevState;
                    const updatedUsers = [...prevState.currentUser, {
                        userId: userData.user.userId,
                        nickname: userData.user.nickname,
                        profileImg: userData.user.profileImg,
                        color: userData.user.color,
                        isMe : userData.user.isMe,
                    }];
                    return {
                        ...prevState,
                        currentUser: updatedUsers
                    }
                }); 
            } else if (state === 'leave') {
                //나간 유저 삭제
                setMultiRoom(prevState => {
                    if (!prevState) return prevState;
                    //나간 유저 삭제
                    const updatedUsers = prevState.currentUser.filter(user => user.userId !== userData.user.userId);
                    return {
                        ...prevState,
                        currentUser: updatedUsers
                    };
                }); 
                
            } 
            // 방장 바뀜
            else if (state ==='change'){
                setMultiRoom({ ...multiroom, managerUserId: userData.user.userId} as Room);
            }
        }
    );
    
    //게임시작용 웹소켓 구독
    const [connectGame, disconnectGame] = useStomp(client, `/sub/round/${params}`, (message: IMessage) => {
        console.log("Received message:", message.body);
        const gameData = JSON.parse(message.body);
        const state = gameData.status;
        const round = gameData.round;

        if (state === "GAMESTART" && round === 0) {
                //게임방으로 보내주기
                console.log(gameData.data)
                navigate(`/multiplay/${multiroom?.multiPlayId}`);
            } 
        }
    );

    useEffect(() => {
        axios.get(`https://j10a106.p.ssafy.io/api/multiplay/${params}`, {withCredentials:true})
            .then((response) => {
                setMultiRoom(response.data.data);
                // console.log(response.data.data);
            });
    }, [params]); // params를 의존성 배열에 추가

    useEffect(()=>{
        console.log("연결 시도")
        connect();
        connectGame();

        return () => {
            disconnect();
            disconnectGame();
        }
    },[]);

    //내가 나갈때
    const leaveRoom = ():void => {
        axios.delete(`https://j10a106.p.ssafy.io/api/multiplay/${params}`, {withCredentials:true})
        navigate("/englising/selectMulti");
        disconnect();
        disconnectGame();
    }

    //방장이 게임 시작 눌렀을 때
    const startGame = ():void => {
        axios.get(`https://j10a106.p.ssafy.io/api/multiplay/${params}/game`, {withCredentials:true})
        .then(()=>{
            console.log("시작요청")
            navigate(`/multiplay/${multiroom?.multiPlayId}`);
        })
    }

    useEffect(() => {
        if (multiroom && multiroom.currentUser) {
            const currentUser = multiroom.currentUser.find(user => user.isMe);
            if (currentUser) {
                setUserId(currentUser.userId);
            }
            // console.log(userId)
        }
    }, [multiroom]);

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
                                        <div className='flex flex-col items-center'>
                                            <div className="w-28 h-28 rounded-full relative " style={{background: item?.color }} >
                                                <img src={item.profileImg} className='w-20 h-20 absolute top-4 left-4'/>
                                            </div>
                                            <div className='flex flex-col pt-3 items-center'>
                                            {(item.userId === multiroom?.managerUserId) && 
                                            <div className='text-secondary-500 pb-2'>(방장)</div>}
                                            {(item.isMe) && 
                                            <div className='text-secondary-500 pb-2'>(Me)</div>}
                                                    <div className='text-white pr-2'>
                                                        {item.nickname}
                                                    </div>
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
                    {(userId === multiroom?.managerUserId) &&
                    <button onClick={startGame} className='text-black bg-secondary-500 w-48 h-12 rounded-lg text-sm hover:opacity-50'> 
                        게임시작        
                    </button> 
                    }
                    <button onClick={leaveRoom} className='text-black bg-red-400 w-48 h-12 rounded-lg text-sm hover:opacity-50'>
                        나가기
                    </button>
                </div>
                    
                
                
            </div>
        </div>
    );
};

export default WaitingRoomPage;