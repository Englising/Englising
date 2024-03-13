import React from 'react';
import Sidebar from '../component/main/Sidebar.tsx';
import Multiroom from '../component/main/MultiRoom.tsx';
import imgRoom1 from '../assets/imgRoom1.jpg';
import imgRoom2 from '../assets/imgRoom2.jpg';
import imgRoom3 from '../assets/imgRoom3.jpg';
import imgRoom4 from '../assets/imgRoom4.jpg';
import imgRoom5 from '../assets/imgRoom5.jpg';
import imgRoom6 from '../assets/imgRoom6.jpg';


interface Room {
    room_id: number;
    room_name: string;
    current_user: number;
    max_user: number;
    multi_img: string;
}

const SelectMultiPage: React.FC = () => {
    const room: Room[] = [{
        room_id: 1,
        room_name: 'A106 들어와라',
        current_user: 5,
        max_user: 6,
        multi_img: imgRoom1,
    }, {
        room_id: 2,
        room_name: '싸피초등학교 16기',
        current_user: 4,
        max_user: 6,
        multi_img: imgRoom2,
    },
    {
        room_id: 3,
        room_name: '아보카도 좋아하는 모임',
        current_user: 2,
        max_user: 6,
        multi_img: imgRoom3,

    }, {
        room_id: 4,
        room_name: '싸피 오픽 스터디',
        current_user: 3,
        max_user: 5,
        multi_img: imgRoom4,

    }, {
        room_id: 5,
        room_name: '돼지파뤼',
        current_user: 3,
        max_user: 4,
        multi_img: imgRoom5,

    }, {
        room_id: 6,
        room_name: '대우부대찌개 갈 사람',
        current_user: 4,
        max_user: 6,
        multi_img: imgRoom6,
    }];

    return (
        <div className="bg-black h-screen w-screen m-0 p-0 flex">
            {/* sideBar */}
            <Sidebar/>

            <div className='flex flex-col pt-10 pl-8'>

                <div className='pt-8 flex flex-row'>
                    {/* 대기방 목록 */}
                    <div>            
                        <div className='flex flex-row pb-6 w-full'>
                            <div className='text-white font-bold text-xl w-48 align-middle'>단체 플레이</div>
                            <div className='text-white font-thin text-sm align-middle'>참여할 방을 선택해 입장해주세요!</div>
                        </div>
                        {/* 장르 선택 버튼 */}
                        <div className='flex flex-row gap-4 pb-6'>
                            <button className='text-black bg-secondary-500 w-24 h-7 rounded-full text-sm hover:opacity-50'>All</button>
                            <button className='text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50'>POP</button>
                            <button className='text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50'>HIP-HOP</button>
                            <button className='text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50'>ROCK</button>
                            <button className='text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50'>DANCE</button>
                        </div>
                        <div className="relative flex flex-col overflow-y-auto">
                            <div className='text-white grid grid-cols-4 gap-4 justify-items-start'>
                                {room.map((item)=> (
                                    <Multiroom room_name={item.room_name} room_id={item.room_id} max_user={item.max_user} current_user={item.current_user} multi_img={item.multi_img}/>
                                ))}
                            </div>
                        </div>
                    </div>
            </div>

            </div>

            
        </div>
    );
};

export default SelectMultiPage;
