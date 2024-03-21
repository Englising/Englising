import WordCard from "../component/main/WordCard";

interface Word {
    word_id : number;
    eng_text : string;
    kor_text1 : string;
    kor_text2 : string;
    kor_text3 : string;
    example : string;
    is_liked : boolean;
}

const WordListPage = () => {
    const word: Word[] = [{
        word_id: 1,
        eng_text : 'word',
        kor_text1 : '단어~',
        kor_text2 : '단어!',
        kor_text3 : '단어?',
        example : 'Jiwoo memorizes English words.',
        is_liked : true,
    },{
        word_id: 2,
        eng_text : 'english',
        kor_text1 : '영어~',
        kor_text2 : '영어!',
        kor_text3 : '영어?',
        example : 'Ayoung is good at English',
        is_liked : false,
    },{
        word_id: 3,
        eng_text : 'chocolate',
        kor_text1 : '초콜렛',
        kor_text2 : '맛있겠다',
        kor_text3 : '초코!!',
        example : 'Mingi likes iced chocolate.',
        is_liked : false,
    },{
        word_id: 4,
        eng_text : 'bread',
        kor_text1 : '빵',
        kor_text2 : '맘모스빵',
        kor_text3 : '소보로빵',
        example : 'Hyunji loves bread.',
        is_liked : true,
    },{
        word_id: 5,
        eng_text : 'green',
        kor_text1 : '초록색',
        kor_text2 : '초록',
        kor_text3 : '초록!!!!!',
        example : 'Haelim looks good in green.',
        is_liked : false,
    },{
        word_id: 6,
        eng_text : 'singer',
        kor_text1 : '가수',
        kor_text2 : '가수가수',
        kor_text3 : '가수가수',
        example : 'Eunbi is a singer.',
        is_liked : true,
    }
]

    return (
        <div className="bg-black h-svh w-screen m-0 p-0 flex">
            <div className='flex flex-col pt-20 pl-6'>
                <h1 className='text-white font-bold text-xl w-48 pb-12'>단어장</h1>
                <div className='flex flex-row gap-6 pb-12'>
                    <button className='text-black bg-secondary-500 w-24 h-7 rounded-full text-sm hover:opacity-50'>북마크 한 단어</button>
                    <button className='text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50'>기출 단어</button>
                    <button className='text-white border-2 border-primary-200 w-24 h-7 rounded-full text-sm hover:opacity-50'>찾아본 단어</button>
                </div>
                <div className="grid grid-cols-2 gap-6 overflow-y-auto pr-24">
                    {word.map((item, index) => (
                        <div key={index} >
                            <WordCard eng_text={item.eng_text} kor_text1={item.kor_text1} kor_text2={item.kor_text2} kor_text3={item.kor_text3} example={item.example} is_liked={item.is_liked} />
                        </div>
                    ))}    
                </div>       
            </div>
        </div>
    );
};

export default WordListPage;