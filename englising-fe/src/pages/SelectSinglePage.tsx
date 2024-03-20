import React, { useState } from 'react';
import img2002 from '../assets/2002.jpg';
import imgChanges from '../assets/changes.jpg';
import imgLover from '../assets/lover.jpg';
import imgSugar from '../assets/sugar.jpg';
import imgYouth from '../assets/youth.jpg';
import imgMe from '../assets/me.jpg';
import LpPlayer from '../component/main/LpPlayer.tsx';
import Singleroom from '../component/main/SingleRoom.tsx';


interface Music {
    album_title: string;
    track_id: number;
    track_title: string;
    artists: string;
    album_img: string;
    score: number;
    is_like: boolean;
}

export interface SelectedMusic {
    track_title: string;
    artists: string;
    album_img: string;
}


const SelectSinglePage: React.FC = () => {
    const music: Music[] = [{
        album_title: 'album_title 1',
        track_id: 1,
        track_title: '2002',
        artists: 'Anne-Marie',
        album_img: img2002,
        score: 2,
        is_like: true
    }, {
        album_title: 'album_title 2',
        track_id: 2,
        track_title: 'changes',
        artists: 'Lauv',
        album_img: imgChanges,
        score: 3,
        is_like: true
    },
    {
        album_title: 'album_title 3',
        track_id: 3,
        track_title: 'lover',
        artists: 'Taylor Swift',
        album_img: imgLover,
        score: 2,
        is_like: false
    }, {
        album_title: 'album_title 4',
        track_id: 4,
        track_title: 'Sugar',
        artists: 'Maroon 5',
        album_img: imgSugar,
        score: 1,
        is_like: false
    }, {
        album_title: 'album_title 5',
        track_id: 5,
        track_title: 'YOUTH',
        artists: 'Troye Sivan',
        album_img: imgYouth,
        score: 3,
        is_like: true
    }, {
        album_title: 'album_title 6',
        track_id: 6,
        track_title: 'ME!',
        artists: 'Taylor Swift',
        album_img: imgMe,
        score: 1,
        is_like: true
    }];
    const [selectedMusic, setSeletedMusic] = useState<SelectedMusic>({
        track_title: "",
        artists: "",
        album_img: ""
    })

    const handleClickButton = (index: number):void => {
        setSeletedMusic({
            track_title: music[index].track_title,
            artists: music[index].artists,
            album_img: music[index].album_img
        })
    }
    return (
        <div className="bg-black h-svh w-screen m-0 p-0 flex">
            {/*검색창*/}
            <div className='flex flex-col pt-10 pl-8 '>
            <div className="h-11 w-3/5 rounded-lg bg-gradient-to-r from-[white] via-[#00ffff] to-[#3F4685] p-0.5 relative">
                <div className="flex h-full w-full rounded-lg items-center bg-primary-950 back ">
                    <div className="text-sm text-primary-200 font-thin pl-5 py-2 flex-1">플레이하고 싶은 노래를 검색해보세요!</div>
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="white" className="w-6 h-6 absolute right-4 top-1/2 transform -translate-y-1/2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z" />
                    </svg>
                </div>
            </div>

            <div className='pt-5 flex flex-row h-5/6'>
                {/* lp판 */}
                <div >
                    <h1 className='text-white font-bold text-xl w-60 pb-6'>싱글 플레이</h1>
                    <LpPlayer title={selectedMusic.track_title} img={selectedMusic.album_img} artists={selectedMusic.artists}/>
                </div>

                    {/* 플레이리스트 목록 */}
                    {/* 자꾸 화면 삐져나와... 고쳐줘...*/}
                <div className='pl-14 w-4/5'>
                    <h1 className=' text-white font-bold text-xl w-60 pb-3'>추천 플레이리스트</h1>
                    <div className='flex flex-row pb-6 w-full'>
                        <h1 className=' text-white font-thin text-sm w-60 flex-1'>플레이 할 노래를 선택해주세요!</h1>
                        <h1 className=' text-white font-thin text-sm w-60 text-right flex-1 pr-5'>ⓘ 플레이 할 노래를 선택해주세요!</h1>
                    </div>
    
                    <div className="relative flex flex-col overflow-y-auto h-full">
                        <div className='text-white grid grid-cols-3 gap-4 justify-items-start '>
                            {music.map((item, index)=> (
                                <div key={index} onClick={() => handleClickButton(index)}>
                                    <Singleroom album_title={item.album_title} title={item.track_title} artists={item.artists} img={item.album_img} is_like={item.is_like} score={item.score} />
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>

            </div>
        </div>
    );
};

export default SelectSinglePage;
