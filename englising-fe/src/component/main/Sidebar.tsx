import React from 'react';
import { NavLink } from 'react-router-dom';
import imgLover from '../../assets/lover.jpg';

const sideBar = () => {
    const single_title = [
        {name: '싱글 플레이', path: '/'},
    ];
    const single_menus = [
        {name: '추천 플레이리스트', path: '/'},
        {name: '좋아요한 음악', path: '/'},
        {name: '최근 플레이한 음악', path: '/'},
    ];
    const multi_title = [
        {name: '단체 플레이', path: '/'},
    ];
    const multi_menus = [
        {name: '방 참여하기', path: '/'},
        {name: '방 생성하기', path: '/'},
    ];
    const other_menus = [
        {name: '단어장', path: '/'},
    ];
    return (
        <div>
            <div className='w-64 bg-black flex flex-col justify-center pl-5 gap-3.5 text-white relative pt-12'>
            <img src={imgLover} alt={imgLover} className='w-28 h-28 rounded-full place-self-center'/>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="#00ffff" className="w-8 h-8 absolute top-32 left-40">
                <path stroke-linecap="round" stroke-linejoin="round" d="M10.343 3.94c.09-.542.56-.94 1.11-.94h1.093c.55 0 1.02.398 1.11.94l.149.894c.07.424.384.764.78.93.398.164.855.142 1.205-.108l.737-.527a1.125 1.125 0 0 1 1.45.12l.773.774c.39.389.44 1.002.12 1.45l-.527.737c-.25.35-.272.806-.107 1.204.165.397.505.71.93.78l.893.15c.543.09.94.559.94 1.109v1.094c0 .55-.397 1.02-.94 1.11l-.894.149c-.424.07-.764.383-.929.78-.165.398-.143.854.107 1.204l.527.738c.32.447.269 1.06-.12 1.45l-.774.773a1.125 1.125 0 0 1-1.449.12l-.738-.527c-.35-.25-.806-.272-1.203-.107-.398.165-.71.505-.781.929l-.149.894c-.09.542-.56.94-1.11.94h-1.094c-.55 0-1.019-.398-1.11-.94l-.148-.894c-.071-.424-.384-.764-.781-.93-.398-.164-.854-.142-1.204.108l-.738.527c-.447.32-1.06.269-1.45-.12l-.773-.774a1.125 1.125 0 0 1-.12-1.45l.527-.737c.25-.35.272-.806.108-1.204-.165-.397-.506-.71-.93-.78l-.894-.15c-.542-.09-.94-.56-.94-1.109v-1.094c0-.55.398-1.02.94-1.11l.894-.149c.424-.07.765-.383.93-.78.165-.398.143-.854-.108-1.204l-.526-.738a1.125 1.125 0 0 1 .12-1.45l.773-.773a1.125 1.125 0 0 1 1.45-.12l.737.527c.35.25.807.272 1.204.107.397-.165.71-.505.78-.929l.15-.894Z" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
            </svg>

            <div className='pt-6 text-sm text-primary-300'>MUSIC</div>
                {single_title.map((menu, index) => {
                    return (
                        <NavLink
                            to = {menu.path}
                            key={index}
                            className="pl-5 pt-3"
                        >
                            <div className='flex flex-row gap-2'>
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="w-5 h-5">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="m9 9 10.5-3m0 6.553v3.75a2.25 2.25 0 0 1-1.632 2.163l-1.32.377a1.803 1.803 0 1 1-.99-3.467l2.31-.66a2.25 2.25 0 0 0 1.632-2.163Zm0 0V2.25L9 5.25v10.303m0 0v3.75a2.25 2.25 0 0 1-1.632 2.163l-1.32.377a1.803 1.803 0 0 1-.99-3.467l2.31-.66A2.25 2.25 0 0 0 9 15.553Z" />
                                </svg>
                                {menu.name} 
                            </div>
                        </NavLink>
                        
                    );
                })}
                {single_menus.map((menu, index) => {
                    return (
                        <NavLink
                            to = {menu.path}
                            key={index}
                            className="pl-10 text-sm"
                        >
                            {menu.name}
                        </NavLink>
                        
                    );
                })}
                {multi_title.map((menu, index) => {
                    return (
                        <NavLink
                            to = {menu.path}
                            key={index}
                            className="pl-5 pt-5"
                        >
                            <div className='flex flex-row gap-2'>
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="w-6 h-6">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M18 18.72a9.094 9.094 0 0 0 3.741-.479 3 3 0 0 0-4.682-2.72m.94 3.198.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0 1 12 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 0 1 6 18.719m12 0a5.971 5.971 0 0 0-.941-3.197m0 0A5.995 5.995 0 0 0 12 12.75a5.995 5.995 0 0 0-5.058 2.772m0 0a3 3 0 0 0-4.681 2.72 8.986 8.986 0 0 0 3.74.477m.94-3.197a5.971 5.971 0 0 0-.94 3.197M15 6.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Zm6 3a2.25 2.25 0 1 1-4.5 0 2.25 2.25 0 0 1 4.5 0Zm-13.5 0a2.25 2.25 0 1 1-4.5 0 2.25 2.25 0 0 1 4.5 0Z" />
                            </svg>
                                {menu.name} 
                            </div>
                        </NavLink>
                        
                    );
                })}
                {multi_menus.map((menu, index) => {
                    return (
                        <NavLink
                            to = {menu.path}
                            key={index}
                            className="pl-10 text-sm"
                        >
                            {menu.name}
                        </NavLink>
                        
                    );
                })}
                <div className='pt-6 text-sm text-primary-300'>OTHER</div>
                {other_menus.map((menu, index) => {
                    return (
                        <NavLink
                            to = {menu.path}
                            key={index}
                            className="pl-5 pt-3"
                        >
                            <div className='flex flex-row gap-2'>
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="w-6 h-6">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.042A8.967 8.967 0 0 0 6 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 0 1 6 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 0 1 6-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0 0 18 18a8.967 8.967 0 0 0-6 2.292m0-14.25v14.25" />
                            </svg>
                            {menu.name}
                            </div>
                        </NavLink>
                        
                    );
                })}

            </div>
        </div>
    );
};

export default sideBar;