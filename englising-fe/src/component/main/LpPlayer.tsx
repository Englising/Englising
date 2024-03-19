import React from 'react';
import lpPlayer from '../../assets/lp.png';

interface LpPlayerProps {
    title?: string;
    img?: string;
    artists?: string;
}

const LpPlayer: React.FC<LpPlayerProps> = ({ title, img, artists }) => {
    const circleStyle: React.CSSProperties = {
        backgroundImage: img ? `url(${img})` : 'none', 
        backgroundColor: img ? '' : 'rgba(192, 192, 192, 0.5)',// 이미지가 있으면 배경으로 설정, 없으면 none으로 설정
        backgroundSize: 'cover',
        backgroundPosition: 'center',
    };

    return (
        <div className="relative flex flex-col justify-center content-center items-center">
            <div className='relative flex justify-center items-center flex-col'>
                <img className="h-max w-auto z-0" src={lpPlayer} alt="lpPlayer" />
                <div style={circleStyle} className="rounded-full w-9/12 h-1/2 z-40 absolute flex flex-col items-center justify-center ">
                    {title && artists && (
                        <>
                            <div className='text-center text-xl font-semibold text-white'>{title}</div>
                            <div className='text-center text-xl font-semibold text-primary-800'>{artists}</div>
                        </>
                    )}
                    {!title && !artists && (
                        <>
                            <div className='text-center text-xl font-semibold text-white'>플레이리스트에서</div>
                            <div className='text-center text-xl font-semibold text-primary-800'>플레이 할 노래를</div>
                            <div className='text-center text-xl font-semibold text-white'>선택해주세요!</div>
                        </>
                    )}
                </div>
                <div className='w-4/6 h-1/6 flex flex-row z-40 absolute bottom-4 left-6 items-center space-x-3'>
                    <div className="bg-primary-200 rounded-full h-1/2 w-2/12 z-40 "></div>
                    <div className="bg-primary-200 rounded-full h-1/2 w-2/12 z-40 "></div>
                    <div className="bg-primary-200 rounded-full h-1/2 w-2/12 z-40 "></div>
                </div>
            </div>
        </div>
    );
};

export default LpPlayer;
