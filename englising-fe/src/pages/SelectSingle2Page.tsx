import React, { useState, useEffect } from 'react';
import LpPlayer from '../component/main/LpPlayer.tsx';
import Singleroom from '../component/main/SingleRoom.tsx';
import axios from 'axios';
import SearchBar from '../component/main/SearchBar.tsx';

interface Music {
    albumTitle: string;
    trackId: number;
    trackTitle: string;
    artists: string;
    albumImg: string;
    score: number;
    isLike: boolean;
    youtubeId: string;
}

export interface SelectedMusic {
    trackId: number;
    trackTitle: string;
    artists: string;
    albumImg: string;
    youtubeId: string;
}


const SelectSinglePage: React.FC = () => {
    const [playList, setPlayList] = useState<Music[]>([]);

    useEffect(() => {
    axios.get("https://j10a106.p.ssafy.io/api/singleplay/playlist?type=like&page=0&size=20", {withCredentials:true})
        .then((Response) => {
            console.log(Response.data)
            setPlayList(Response.data.data.playList);
        })
        .catch((error) => {
            console.error('Error fetching playlist:', error);
        });
    }, []);
    const [selectedMusic, setSeletedMusic] = useState<SelectedMusic>({
        trackId: 0,
        trackTitle: "",
        artists: "",
        albumImg: "",
        youtubeId: "",
    })

    const handleClickButton = (index: number):void => {
        setSeletedMusic({
            trackId: playList[index].trackId,
            trackTitle: playList[index].trackTitle,
            artists: playList[index].artists,
            albumImg: playList[index].albumImg,
            youtubeId: playList[index].youtubeId,
        })
    }
    const toggleLike = (index: number) => {
        const updatedPlaylist = [...playList];
        updatedPlaylist[index].isLike = !updatedPlaylist[index].isLike;
        setPlayList(updatedPlaylist);
        axios.post('https://j10a106.p.ssafy.io/api/track/like', { trackId: playList[index].trackId }, {
            withCredentials: true, // 이 옵션을 설정하여 쿠키와 인증 정보를 함께 보냄
            })
            .then(() => {
                // 성공적으로 토글되면 liked 상태 변경
                console.log("like")
            })
            .catch(error => {
                console.error('Error toggling like:', error);
            });

    };

    return (
        <div className="bg-black h-svh w-screen m-0 p-0 flex">
            {/*검색창*/}
            <div className='flex flex-col pt-10 pl-8 '>
            <SearchBar></SearchBar>

            <div className='pt-5 flex flex-row h-5/6'>
                {/* lp판 */}
                <div >
                    <h1 className='text-white font-bold text-xl w-60 pb-6'>싱글 플레이</h1>
                    <LpPlayer trackId={selectedMusic.trackId} title={selectedMusic.trackTitle} img={selectedMusic.albumImg} artists={selectedMusic.artists} youtubeId={selectedMusic.youtubeId}/>
                </div>

                    {/* 플레이리스트 목록 */}
                    {/* 자꾸 화면 삐져나와... 고쳐줘...*/}
                <div className='pl-10 w-4/5'>
                    <h1 className=' text-white font-bold text-xl w-60 pb-3'>좋아요한 음악</h1>
                    <div className='flex flex-row pb-6 w-full'>
                        <h1 className=' text-white font-thin text-sm w-60 flex-1'>플레이 할 노래를 선택해주세요!</h1>
                        <h1 className=' text-white font-thin text-sm w-60 text-right flex-1 pr-5'>ⓘ 플레이 할 노래를 선택해주세요!</h1>
                    </div>
    
                    <div className="relative flex flex-col overflow-y-auto h-full">
                        <div className='text-white grid grid-cols-3 gap-4 justify-items-start '>
                        {playList && playList.length > 0 ? ( // playList가 비어있지 않은 경우에만 map 함수 호출
                        playList.map((item, index) => (
                            <div key={index} className='relative' >
                                <div onClick={() => handleClickButton(index)}>
                                <Singleroom track_id={item.trackId} album_title={item.albumTitle} title={item.trackTitle} artists={item.artists} img={item.albumImg} is_like={item.isLike} score={item.score} />
                                </div>
                                {/* 하트 아이콘 */}
                                {item.isLike ? (
                                    <div onClick={() => toggleLike(index)} className='absolute top-0 left-0'>
                                        <svg xmlns="https://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FF69B4" className="w-6 h-6 absolute left-2 top-2 z-40">
                                        <path d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z" />
                                        </svg>
                                    </div>
                                ) : (
                                    <div onClick={() => toggleLike(index)} className='absolute top-0 left-0'>
                                        <svg xmlns="https://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6 absolute left-2 top-2 z-40">
                                            <path strokeLinecap="round" strokeLinejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
                                        </svg>
                                    </div>
                                )}
                            </div>
                        ))
                            ) : (
                                <div className="text-white w-48">플레이리스트가 비어있습니다.</div>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            </div>
        </div>
    );
};

export default SelectSinglePage;
