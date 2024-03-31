import React from 'react';

interface SingleroomProps {
    track_id:number;
    album_title: string;
    title: string;
    artists: string;
    img: string;
    is_like: boolean;
    score: number;
}

const renderStars = (score: number) => {
    const filledStars = Math.floor(score);
    const emptyStars = Math.floor(3 - score);
    
    const stars = [];
    for (let i = 0; i < filledStars; i++) {
        stars.push(
            <svg xmlns="https://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFFF00" className="w-6 h-6">
                <path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.006 5.404.434c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.434 2.082-5.005Z" clip-rule="evenodd" />
            </svg>
        );
    }
    for (let i = 0; i < emptyStars; i++) {
        stars.push(
            <svg xmlns="https://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                <path strokeLinecap="round" strokeLinejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
            </svg>
        );
    }
    return stars;
}

const Singleroom: React.FC<SingleroomProps> = ({ track_id, album_title, title, artists, img, is_like, score }) => {
    return (
        <div className='relative'>
            <div className='text-white bg-primary-300/50 w-52  rounded-lg hover:opacity-50'>
                <img src={img} alt={title} className='w-52 h-52 rounded-t-lg' />
                    <div className='flex flex-row'>
                        <div className='pt-2 pl-2'>
                            <div className='max-w-48 max-h-4 overflow-hidden text-xs font-bold text-secondary-500 truncate'>{album_title} </div>
                            <div className='max-w-48 overflow-hidden text-ellipsis whitespace-nowrap font-bold text-xl pt-1 truncate'> {title} </div>
                            <div className='max-w-48 overflow-hidden text-sm pb-2 truncate'> {artists}</div>
                        </div>
                        <div className='flex flex-row absolute right-2 bottom-24'> {renderStars(score)} </div>
                    </div>
            </div>
        </div>
        
    );
};

export default Singleroom;
