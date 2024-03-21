import React from 'react';
import imgUser from '../assets/user.png'
import ChatArea from '../component/chat/ChatArea';
import { Link } from 'react-router-dom';



const WaitingRoomPage = () => {
    return (
        <div className="bg-black h-svh w-screen m-0 p-0 ">
            <div className='flex flex-col w-full'>
                <div className='flex flex-row pb-6 pt-20 pl-48 h-20 w-full items-center'>
                    <div className='text-secondary-500 font-bold text-xl'>아보카도 좋아하는 모임</div>
                    <div className='text-white font-semibold text-sm pl-6'>대기방</div>
                    <div className='text-white font-thin text-sm pl-28'>방장이 게임을 시작할 수 있습니다.</div>
                </div>

                <div className='flex flex-row h-4/5'>
                    {/* 멤버 목록 */}
                    <div className='flex flex-col w-1/2'>
                        <div className='pl-52 pt-12 flex flex-row'>
                            <div className='text-white'> 대기 중 멤버</div>
                            <div className='text-secondary-500 pl-8 pr-2'> 5</div>
                            <div className='text-white pr-2'> /</div>
                            <div className='text-white'> 6</div>
                        </div>
                        <div className='pl-56 pt-8 grid grid-cols-3 w-full'>
                            {/* 데이터 들어오면 for문으로 수정 + 컴포넌트 분리 예정 */}
                            <div className='flex flex-col'>
                                <img src={imgUser} className='h-24 w-24'/>
                                <div className='text-white pl-6 pt-4 pb-10'>이지우</div>
                            </div>
                            <div className='flex flex-col'>
                                <img src={imgUser} className='h-24 w-24'/>
                                <div className='text-white pl-6 pt-4 pb-10'>이지우</div>
                            </div>
                            <div className='flex flex-col'>
                                <img src={imgUser} className='h-24 w-24'/>
                                <div className='text-white pl-6 pt-4 pb-10'>이지우</div>
                            </div>
                            <div className='flex flex-col'>
                                <img src={imgUser} className='h-24 w-24'/>
                                <div className='text-white pl-6 pt-4 pb-10'>이지우</div>
                            </div>
                            <div className='flex flex-col'>
                                <img src={imgUser} className='h-24 w-24'/>
                                <div className='text-white pl-6 pt-4 pb-10'>이지우</div>
                            </div>
                        </div>
                    </div>

                    <div className='pt-12 pl-28 w-2/5'>
                        <ChatArea/>
                    </div>
                </div>

                <div className='flex justify-center pt-16'>
                    <button className='text-black bg-secondary-500 w-48 h-12 rounded-lg text-sm hover:opacity-50'>
                        <Link to ="/multiPlay/:multiId">방 설정 완료</Link>
                    </button>
                </div>
                
            </div>
        </div>
    );
};

export default WaitingRoomPage;