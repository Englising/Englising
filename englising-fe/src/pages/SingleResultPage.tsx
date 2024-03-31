import { useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import { getSinglePlayResultData } from "../util/SinglePlayAPI";
import { Lyric, Word } from "./SinglePage";
import ResultLyrics from "../component/single/ResultLyrics";
import ResultWord from "../component/single/ResultWord";

export interface ResultWords extends Word {
    wordId: number; // 좋아요에 필요한 wordId
    isLike: boolean; // 좋아요 여부
}

const SingleResultPlage = () => {
    // 1. 싱글플레이 아이디 넘어와야 함 (이건 쿼리스트링)
    // 2. 똑같이 배경이랑 타이틀 이름 넘어와야 함 (location의 데이터로)
    // 3. 뒤로가기 막아보기 - 후작업
    const { state } = useLocation(); 
    const { title, img, artists } = state;

    const { singlePlayId } = useParams<{
        singlePlayId: string;
    }>();

    const [lyrics, setLyrics] = useState<Lyric[]>();
    const [resultWord, setResultWord] = useState<ResultWords[]>();

    //const { resultData, setResultData } = useState<number>();
    useEffect(() => {
        const data = {
            "singlePlayId": parseInt(singlePlayId || "0"),
        }
      
        const getData = async () => {
            try {
                const singleResultData = await getSinglePlayResultData(data);
                console.log("결과 가사", singleResultData.data.lyrics);
                setLyrics([...singleResultData.data.lyrics]);

                console.log("결과 단어", singleResultData.data.words);
                setResultWord([...singleResultData.data.words]);
            } catch (error) {
                console.error('Error fetching data:', error)
            }
        }
        getData();
    }, [])
    
    return (
        <div className="bg-cover bg-center h-screen w-screen p-0 m-0 relative" style={{ backgroundImage: `url(${img})` }}>
            <div className="h-svh w-screen flex flex-col bg-black bg-opacity-80 items-center select-none">
                <div className="w-full h-[35%] flex justify-center items-center">
                    <div className="w-[16%] h-[90%] mr-[2em] relative">
                        <div className="absolute inset-0">
                            <img src={img} alt={title} className="w-full h-full p-3 object-cover rounded-full box-border" />
                        </div>                        
                    </div>
                    <div className="text-white text-center">
                        <div className="text-[1.5em] font-bold">{title}</div>
                        <div className="text-[1em]">{artists}</div>
                    </div>
                </div>
                <div className="w-full h-[65%] flex">
                    <div className="w-3/5 h-full flex items-center justify-center">
                        <ResultLyrics lyrics={lyrics} resultWord={resultWord}/>
                    </div>
                    <div className="w-2/5 h-full flex flex-col items-center justify-center">
                        <div className="w-full h-[15%] text-xl text-white font-bold flex justify-center items-center">
                            <div>단어를 클릭해 내 단어장에 담아보세요!</div>
                        </div>
                        <div className="w-full h-[85%]">
                            <ResultWord resultWord={resultWord} />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )

}
export default SingleResultPlage;