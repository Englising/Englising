import React from 'react';


interface MultiroomProps {
    room_id: number;
    room_name: string;
    current_user: number;
    max_user: number;
    multi_img: string;
}



const Multiroom: React.FC<MultiroomProps> = ({room_name, current_user, max_user, multi_img}) => {
    return (
        <div className='text-white bg-primary-500/50 w-52 relative rounded-lg hover:opacity-50 relative'>
            <img src={multi_img} alt={room_name} className='w-52 h-48 rounded-t-lg '/>
            <div className='rounded-full h-9 w-9 bg-white absolute right-2 top-44 text-sm text-center font-bold text-black'>
                <p className=''>입장</p>
            </div>
            <div className='flex flex-row'>
                <div className='pt-2 pl-2'>
                    <div className='text-lg font-extrabold text-white pt-3'>{room_name} </div>
                    <div className='flex flex-row text-sm pb-4 gap-1'>
                        <div className='text-secondary-500'> {current_user} </div>
                        <div>/</div>
                        <div> {max_user}</div>
                    </div>
                </div>
                
            </div>
        </div>
    );
};

export default Multiroom;
