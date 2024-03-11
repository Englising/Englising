import React from 'react';

interface MultiroomProps {
    album_title: string;
    title: string;
    artists: string;
    img: string;
    is_like: boolean;
    score: number;
}

const renderLikeIcon = (is_like: boolean) => {
    if (is_like) {
        return (
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FF69B4" className="w-6 h-6">
                <path d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z" />
            </svg>
        );
    } else {
        return (
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="w-6 h-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
            </svg>
        );
    }
}

const renderStars = (score: number) => {
    const filledStars = Math.floor(score);
    const emptyStars = Math.floor(3 - score);
    
    const stars = [];
    for (let i = 0; i < filledStars; i++) {
        stars.push(
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFFF00" className="w-6 h-6">
                <path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.006 5.404.434c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.434 2.082-5.005Z" clip-rule="evenodd" />
            </svg>
        );
    }
    for (let i = 0; i < emptyStars; i++) {
        stars.push(
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="w-6 h-6">
                <path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
            </svg>
        );
    }
    return stars;
}

const Multiroom: React.FC<MultiroomProps> = ({album_title, title, artists, img, is_like, score}) => {
    return (
        <div className='text-white bg-primary-300/50 w-52 relative rounded-lg hover:opacity-50'>
            <div className='absolute left-2 top-2'> {renderLikeIcon(is_like)} </div>
            <img src={img} alt={title} className='w-52 h-52 rounded-t-lg '/>
            <div className='flex flex-row'>
                <div className='pt-2 pl-2'>
                    <div className='text-xs font-bold text-secondary-500'>{album_title} </div>
                    <div className='font-bold text-xl pt-1'> {title} </div>
                    <div className='text-sm pb-2'> {artists}</div>
                </div>
                <div className='flex flex-row absolute right-2 bottom-4'> {renderStars(score)} </div>
            </div>
        </div>
    );
};

export default Multiroom;
