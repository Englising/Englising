import { Lyric } from "../../pages/SinglePage";
import { ResultWords } from "../../pages/SingleResultPage";

interface Props {
    lyrics: Lyric[] | undefined
    resultWord: ResultWords[] | undefined
}

const ResultLyrics = ({ lyrics, resultWord }: Props) => {
    return (
        <div className="w-[80%] h-[90%] rounded-[2em] bg-black bg-opacity-50 flex items-center">
            <div className="w-full h-[85%] flex flex-col items-center py-10 box-border overflow-y-scroll scrollbar-webkit">
                {lyrics?.map((lyric, i) => {
                    return(
                        <div 
                        key={i} 
                        className="w-full min-h-[15%] text-[1.3em] flex justify-center items-center text-primary-100">
                            <div className="flex">
                                {lyric.lyric.map((word, j) => {
                                if(word == " ") return <div>&nbsp;</div>
                                let isBlank:boolean = false;
                                let isRight:boolean = false;

                                resultWord?.forEach((blank) => {
                                    //console.log("단어 idx:", blank.sentenceIndex, "result:", word.toLowerCase(), blank.word.toLowerCase());
                                    if(word.toLowerCase().includes(blank.word.toLowerCase())  && i == blank.sentenceIndex && j == blank.wordIndex){
                                        isBlank = true;
                                        if(blank.isRight) isRight = true;
                                    }
                                })
                                //만약 해당 단어가 빈칸이 필요하다면 -> isBlank 속성 값 결정
                                //data-isSolve: 0=미해결, 1=오답, 2=정답
                                return (
                                    isBlank ? 
                                        (
                                            isRight ? (
                                        <div 
                                            key={j} 
                                            className={"text-[#6DD66D] font-bold"}
                                        > 
                                            {word}
                                                </div>) :
                                        (<div 
                                            key={j} 
                                            className={"text-[#FF4646] font-bold"}
                                        > 
                                            {word}
                                                </div>))
                                    : (<div key={j}> {word} </div>)
                                );
                            })}
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    )
}
export default ResultLyrics;