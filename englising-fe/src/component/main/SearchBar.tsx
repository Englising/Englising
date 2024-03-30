import React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const SearchBar = () => {
    const [searchTerm, setSearchTerm] = useState<string>('');
    const navigate = useNavigate();

    const handleSearch = () => {
        navigate(`/englising/searchResult/${searchTerm}`)
    };
    return (
        <div className="h-11 w-3/5 rounded-lg bg-gradient-to-r from-[white] via-[#00ffff] to-[#3F4685] p-0.5 relative">
                    <div className="flex h-full w-full rounded-lg items-center bg-primary-950 back ">
                        <input
                            type="text"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="text-sm text-primary-200 font-thin pl-5 py-2 flex-1 outline-none bg-black"
                            placeholder="플레이하고 싶은 노래를 검색해보세요!"
                        />
                        <button onClick={handleSearch} className="absolute right-4 top-1/2 transform -translate-y-1/2 bg-transparent border-none">
                            <svg xmlns="https://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="white" className="w-6 h-6">
                                <path strokeLinecap="round" strokeLinejoin="round" d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z" />
                            </svg>
                        </button>
                    </div>
                </div>
    );
};

export default SearchBar;