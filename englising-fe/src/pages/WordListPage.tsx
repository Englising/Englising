import WordCard from "../component/main/WordCard";
import React, {useState, useEffect} from 'react';
import axios from 'axios';

interface Word {
    word_id : number;
    eng_text : string;
    kor_text1 : string;
    kor_text2 : string;
    kor_text3 : string;
    example : string;
    is_liked : boolean;
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
        // 페이지가 처음 렌더링될 때 "like" 버튼을 선택된 상태로 설정
        setSelectedButton("like");
    }, []); 

    const handleClick = async (endpoint: string) => {
        try {
            // API 호출
            const response = await axios.get(`https://j10a106.p.ssafy.io/api/word/list?type=${endpoint}&page=1&size=20`);

            // 응답 받아서 리스트에 넣기
            setWordList(response.data.data);
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

    return (
        <div className="bg-black h-svh w-screen m-0 p-0 flex">
            <div className='flex flex-col pt-20 pl-6'>
                <h1 className='text-white font-bold text-xl w-48 pb-12'>단어장</h1>
                <div className='flex flex-row gap-6 pb-12'>
                    <WordButton buttonText="북마크 한 단어" apiEndpoint="like" onClick={handleClick} selected={selectedButton === "like"} />
                    <WordButton buttonText="기출 단어" apiEndpoint="played"  onClick={handleClick} selected={selectedButton === "played"} />
                    <WordButton buttonText="찾아본 단어" apiEndpoint="searched" onClick={handleClick} selected={selectedButton === "searched"} />

                </div>
                <div className="grid grid-cols-2 gap-6 overflow-y-auto pr-24">
                    {wordList && wordList.length > 0 ? ( // wordList 배열이 비어있지 않은 경우에만 map 함수 호출
                        wordList.map((item, index) => (
                            <div key={index} >
                                <WordCard eng_text={item.eng_text} kor_text1={item.kor_text1} kor_text2={item.kor_text2} kor_text3={item.kor_text3} example={item.example} is_liked={item.is_liked} />
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
