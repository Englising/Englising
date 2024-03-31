import React from 'react';
import useSpeek from '../../hooks/useSpeek';

interface WordProps {
    engText : string;
    korText1 : string;
    korText2 : string;
    korText3 : string;
    example : string;
    liked : boolean;
}

// const renderLikeIcon = (liked: boolean) => {
//     if (liked) {
//         return (
//             <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FF69B4" className="w-6 h-6">
//                 <path d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z" />
//             </svg>
//         );
//     } else {
//         return (
//             <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="w-6 h-6">
//                 <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
//             </svg>
//         );
//     }
// }

const WordCard: React.FC<WordProps> = ({ engText, korText1, korText2, korText3, example }) => {
    const handleSpeekClick = (word: string): void => {
        useSpeek(word);
    }
    return (
        <div className='text-white bg-primary-800 pl-5 w-[400px] h-[220px] relative rounded-xl'>
            <div className='text-2xl pl-0.5 pt-2 font-bold'>{engText}</div>
            {korText1 && <div className='pt-4 pl-0.5 text-sm'>1. {korText1}</div>}
            {korText2 && <div className='text-sm'>2. {korText2}</div>}
            {korText3 &&<div className='text-sm'>3. {korText3}</div> }
            <div className='text-secondary-100 pt-4 pb-3'>예문 : {example}</div>
            <svg className="feather feather-volume-2 absolute top-4 right-14 cursor-pointer" onClick={() => handleSpeekClick(engText)} fill="none" height="28" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" viewBox="0 0 28 28" width="28" xmlns="https://www.w3.org/2000/svg"><polygon points="11 5 6 9 2 9 2 15 6 15 11 19 11 5"/><path d="M19.07 4.93a10 10 0 0 1 0 14.14M15.54 8.46a5 5 0 0 1 0 7.07"/></svg>
            {/* <div className='absolute top-4 right-5'>{renderLikeIcon(liked)} </div> */}
        </div>
    );
};

export default WordCard;