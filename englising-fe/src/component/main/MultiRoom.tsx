import React from 'react';
import { Link } from 'react-router-dom';

interface MultiroomProps {
    roomId: number;
    roomName: string;
    currentUser: number;
    maxUser: number;
    multiPlayImgUrl: string;
}


const Multiroom: React.FC<MultiroomProps> = ({roomId,roomName, currentUser, maxUser, multiPlayImgUrl}) => {
    return (
        <div className='relative'>
            <div className='text-white bg-primary-800/50 w-52 relative rounded-lg hover:opacity-50 relative'>
                <img src={multiPlayImgUrl} alt={roomName} className='w-52 h-48 rounded-t-lg '/>
                <div className='flex flex-row'>
                    <div className='pt-2 pl-2'>
                        <div className='text-lg font-extrabold text-white pt-3'>{roomName} </div>
                        <div className='flex flex-row text-sm pb-4 gap-1'>
                            <div className='text-secondary-500'> {currentUser} </div>
                            <div>/</div>
                            <div> {maxUser}</div>
                        </div>
                    </div>  
                </div>
            </div>
        <div className='flex justify-center items-center rounded-full h-9 w-9 bg-white absolute right-2 top-44 text-xs text-center font-bold text-black'>
                <p>
                <Link to ={`/waitroom/${roomId}`}>입장</Link>
                </p>
            </div>
        </div>
    
    );
};

export default Multiroom;
