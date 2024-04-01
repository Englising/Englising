import { useEffect, useRef } from "react";
import { ResultWords } from "../../pages/SingleResultPage";
import { singlePlayWordLike } from "../../util/SinglePlayAPI";

interface Props {
    resultWord: ResultWords[] | undefined
    //wordId, isLike, isRight, word 이거만 씀
}

const rightStyle = "w-[7em] h-12 m-2 rounded-lg text-white text-bold bg-[#6DD66D] flex justify-center items-center opacity-70 cursor-pointer transition duration-300"
const wrongStyle = "w-[7em] h-12 m-2 rounded-lg text-white text-bold bg-[#FF4646] flex justify-center items-center opacity-70 cursor-pointer transition duration-300"

const ResultWord = ({ resultWord }: Props) => {
    const wordsRef = useRef<(HTMLDivElement | null)[]>([]);

    const handleLikeClick = (index: number) => {
        
        const wordRef = wordsRef?.current[index] || null
        const wordId = wordsRef?.current[index]?.dataset.wordid;
        const isLike = wordsRef?.current[index]?.dataset.like;

        const data = {
            "wordId": parseInt(wordId || "0"),
        }

        if (isLike === "true") {
            //좋아요 상태에선 스타일 제거
            if (wordRef != null) {
                wordRef.dataset.like = "false";
                singlePlayWordLike(data);
                wordsRef?.current[index]?.classList.remove('px-3', 'border-2', 'border-cyan-400', 'shadow-lg', 'shadow-cyan-200');
                wordsRef?.current[index]?.classList.add('opacity-70');
            }
        } else if (isLike === "false") {
            //좋아요 취소 상태에서 스타일 부여       
            if (wordRef != null) {
                wordRef.dataset.like = "true";
                singlePlayWordLike(data);
                wordsRef?.current[index]?.classList.add('px-3', 'border-2', 'border-cyan-400', 'shadow-lg', 'shadow-cyan-200');
                wordsRef?.current[index]?.classList.remove('opacity-70');
            }
        }
    }
    
    useEffect(() => {
        if (resultWord == undefined) return;
        wordsRef.current.map((el) => {
            if (el?.dataset.like == "true") {
                el.classList.add('px-3', 'border-2', 'border-cyan-400', 'shadow-lg', 'shadow-cyan-200');
            }
        })
        
    }, [resultWord])
    return (
        <div className="w-[90%] h-full flex flex-wrap justify-center overflow-y-scroll scrollbar-webkit">
            {resultWord?.map((resultWord, i) => {
                return (
                    <div key={i} ref={(el) => wordsRef.current[i]=el}  className={resultWord.isRight? rightStyle : wrongStyle} data-wordid={resultWord.wordId} data-like={resultWord.isLike} onClick={() => {handleLikeClick(i)}}>
                        <div>{resultWord.word}</div>
                    </div>
                    )
                }
            )}
        </div>
    )
}
export default ResultWord;