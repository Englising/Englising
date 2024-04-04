import WordCard from "../component/main/WordCard";
import React, {useState, useEffect} from 'react';
import axios from 'axios';

interface Word {
    wordId : number;
    enText : string;
    korText1 : string;
    korText2 : string;
    korText3 : string;
    example : string;
    liked : boolean;
}

interface WordButtonProps {
    buttonText: string;
    apiEndpoint: string;
    onClick: (endpoint: string) => void; // 클릭 이벤트 핸들러
    selected: boolean; // 선택된 버튼 여부
}

const WordListPage = () => {
    const [wordList, setWordList] = useState<Word[]>([]);
    const [selectedButton, setSelectedButton] = useState<string>(''); // 선택된 버튼 상태

    useEffect(() => {
        axios.get(`https://j10a106.p.ssafy.io/api/word/list?type=like&page=0&size=20`)
            .then((response) => {
                // 응답 받아서 리스트에 넣기
                setWordList(response.data.data.wordResponseDto);
                setSelectedButton("like");
                console.log('단어목록 가져오기 성공');
            })
            .catch((error) => {
                // 오류 처리
                console.error(`단어 목록 가져오기 실패`, error);
            });
    }, []);

    const handleClick = async (endpoint: string) => {
        try {
            // API 호출
            const response = await axios.get(`https://j10a106.p.ssafy.io/api/word/list?type=${endpoint}&page=0&size=20`);
            // 응답 받아서 리스트에 넣기
            setWordList(response.data.data.wordResponseDto);
            setSelectedButton(endpoint);
            console.log('단어목록 가져오기 성공');
        } catch (error) {
            // 오류 처리
            console.error(`단어 목록 가져오기 실패`, error);
        }
    };

    const WordButton: React.FC<WordButtonProps> = ({ buttonText, apiEndpoint, onClick, selected }) => {
        return (
            <button className={`text-black ${selected ? 'bg-secondary-500' : 'bg-primary-500'} w-24 h-7 rounded-full text-sm hover:opacity-50`} onClick={() => onClick(apiEndpoint)}>
                {buttonText}
            </button>
        );
    };

    const toggleLike = (index: number) => {
        const updatedWordlist = [...wordList];
        updatedWordlist[index].liked = !updatedWordlist[index].liked;
        setWordList(updatedWordlist);
        try {
            // 즐겨찾기 상태를 서버에 업데이트
            const wordId = updatedWordlist[index].wordId;
            axios.post("https://j10a106.p.ssafy.io/api/word/like",{
                wordId : wordId
            });
            console.log('단어 즐겨찾기 업데이트 성공');
        } catch (error) {
            console.error('단어 즐겨찾기 업데이트 실패', error);
        }
    };

    return (
        <div className="bg-black h-svh w-screen m-0 p-0 flex">
            <div className='flex flex-col pt-20 pl-6'>
                <h1 className='text-white font-bold text-xl w-48 pb-12'>단어장</h1>
                <div className='flex flex-row gap-6 pb-12'>
                    <WordButton buttonText="북마크 한 단어" apiEndpoint="like" onClick={handleClick} selected={selectedButton === "like"} />
                    <WordButton buttonText="기출 단어" apiEndpoint="played"  onClick={handleClick} selected={selectedButton === "played"} />
                    <WordButton buttonText="찾아본 단어" apiEndpoint="searched" onClick={handleClick} selected={selectedButton === "searched"} />
                </div>
                <div className="grid grid-cols-2 gap-10 overflow-y-auto pr-24">
                    {wordList && wordList.length > 0 ? ( // wordList 배열이 비어있지 않은 경우에만 map 함수 호출
                        wordList.map((item, index) => (
                            <div key={index} className="relative" >
                                <WordCard engText={item.enText} korText1={item.korText1} korText2={item.korText2} korText3={item.korText3} example={item.example} liked={item.liked} />
                                {item.liked ? (
                                    <div onClick={() => toggleLike(index)} className='absolute top-2 right-12'>
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FF69B4" className="w-6 h-6 absolute left-2 top-2 z-40">
                                        <path d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z" />
                                        </svg>
                                    </div>
                                ) : (
                                    <div onClick={() => toggleLike(index)} className='absolute top-2 right-12'>
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="white" className="w-6 h-6 absolute left-2 top-2 z-40">
                                            <path strokeLinecap="round" strokeLinejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
                                        </svg>
                                    </div>
                                )}
                            </div>
                        ))
                    ) : (
                        <div className="text-white w-full">단어 목록이 없습니다.</div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default WordListPage;
