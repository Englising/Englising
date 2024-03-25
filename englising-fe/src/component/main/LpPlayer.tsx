import React, { useState, useEffect } from 'react';
import lpPlayer from '../../assets/lp.png';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';


interface LpPlayerProps {
    trackId? : number;
    title?: string;
    img?: string;
    artists?: string;
    youtubeId?: string
}

const LpPlayer: React.FC<LpPlayerProps> = ({ title, img, artists, trackId, youtubeId }) => {
const navigate = useNavigate();

    const [bgColor1, setBgColor1] = useState('#ced4da');
    const [bgColor2, setBgColor2] = useState('#ced4da');
    const [bgColor3, setBgColor3] = useState('#ced4da');

    const [level, setLevel] = useState<number>(1);

    const selectLevel1 = () => {
        setLevel(1);
        setBgColor1('#00ffff');
        setBgColor2('#ced4da');
        setBgColor3('#ced4da');
    };

    useEffect(() => {
        console.log(youtubeId);
        // 초기화 로직을 useEffect 내부에 넣어 한 번만 실행되도록 함
        selectLevel1();
    }, []); // 빈 배열을 전달하여 컴포넌트가 처음 마운트될 때만 실행되도록 함


    const selectLevel2 = () => {
        setLevel(2);
        setBgColor2('#00ffff');
        setBgColor1('#ced4da');
        setBgColor3('#ced4da');
    };
    const selectLevel3 = () => {
        setLevel(3);
        setBgColor3('#00ffff');
        setBgColor2('#ced4da');
        setBgColor1('#ced4da');
    };
    
    const circleStyle: React.CSSProperties = {
        backgroundImage: img ? `url(${img})` : 'none', 
        backgroundColor: img ? '' : 'rgba(192, 192, 192, 0.5)',// 이미지가 있으면 배경으로 설정, 없으면 none으로 설정
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        opacity: img ? 0.5 : 1,
    };

    const startSingleplay  = () => {
        navigate(`/singlePlay/${trackId}/${level}/${youtubeId}`, {
            state: {
                title: title,
                img: img,
                artists: artists,
            },
        })
        console.log("게임시작성공");
    };

    return (
        <div className="relative flex flex-col justify-center content-center items-center">
            <div className='relative flex justify-center items-center flex-col'>
                <img className="h-max w-auto z-0 " src={lpPlayer} alt="lpPlayer"  />
                <div className="rounded-full w-9/12 h-1/2 z-30 absolute flex flex-col items-center justify-center ">
                    <div style={circleStyle} className="rounded-full h-full w-full z-30 absolute flex flex-col items-center justify-center "></div>
                    {title && artists && (
                        <div className='z-40 opacity-100'>
                            <div className='text-center text-2xl font-semibold text-white '>{title}</div>
                            <div className='text-center text-lg font-semibold text-white '>{artists}</div>
                        </div>
                    )}
                    {!title && !artists && (
                        <div className='z-40 opacity-100'>
                            <div className='text-center text-xl font-semibold text-white'>플레이리스트에서</div>
                            <div className='text-center text-xl font-semibold text-primary-800'>플레이 할 노래를</div>
                            <div className='text-center text-xl font-semibold text-white'>선택해주세요!</div>
                        </div>
                    )}
                </div>
                <div >
                    {!title && !artists && (
                        <div className='w-4/6 h-1/6 flex flex-row z-40 absolute bottom-4 left-6 items-center space-x-3'>
                            <div className="bg-primary-200 rounded-full h-1/2 w-2/12 z-40 text-center pt-2 "></div>
                            <div className="bg-primary-200 rounded-full h-1/2 w-2/12 z-40 text-center pt-2"></div>
                            <div className="bg-primary-200 rounded-full h-1/2 w-2/12 z-40 text-center pt-2 "></div>
                        </div>
                    )}
                    {/* 레벨 선택 */}
                    {title && artists && (
                        <div>
                            <div className='text-white absolute bottom-20 left-6 text-xl font-bold'>LEVEL</div>
                            <div className='w-4/6 h-1/6 flex flex-row z-40 absolute bottom-4 left-6 items-center space-x-3'>
                                {/*1*/}
                                <div onClick={selectLevel1} style={{background: bgColor1}} className="bg-primary-200 rounded-full h-1/2 w-2/12 z-40 text-center pt-2 font-bold">1
                                    <div className="text-black text-xs font-bold pt-3">easy</div>
                                </div>
                                {/*2*/}
                                <div onClick={selectLevel2}  style={{background: bgColor2}}className="bg-primary-200 rounded-full h-1/2 w-2/12 z-40 text-center pt-2 font-bold">2</div>
                                {/*3*/}
                                <div onClick={selectLevel3} style={{background: bgColor3}} className="bg-primary-200 rounded-full h-1/2 w-2/12 z-40 text-center pt-2 font-bold">3
                                    <div className="text-black text-xs font-bold pt-3">hard</div>
                                </div>
                            </div>
                            <button className='bg-secondary-500 text-black absolute bottom-10 right-7 rounded-lg py-1.5 px-2 drop-shadow-md font-bold'
                                    onClick={startSingleplay}>
                                <div> Game Start</div>
                            </button>
                        </div>
                    )}
                    
                </div>
            </div>
        </div>
    );
};

export default LpPlayer;
