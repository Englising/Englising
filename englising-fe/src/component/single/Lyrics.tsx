import { useEffect, useRef, useState } from "react";
import { PlayInfo, SingleData, Lyric, Word, AnswerInfo} from "../../pages/SinglePage.tsx";

interface Props {
    onSetInfo(currIdx: number,  blank: boolean, start: number, end: number): void,
    playInfo: PlayInfo,
    singleData: SingleData,
    answerInfo: AnswerInfo,
}

const Lyrics = ({onSetInfo, answerInfo, playInfo, singleData}:Props) => {
    const {idx, startTime, endTime, toggleNext} = playInfo;
    const {answer, toggleSubmit} = answerInfo;
    const [lyrics, setLyrics] = useState<Lyric[]>([]);
    const [blankWord, setBlankWord] = useState<Word[]>([]);
    const lyricsRef = useRef<(HTMLDivElement | null)[]>([]);
    const blanksRef = useRef<(HTMLSpanElement | null)[]>([]);

    // aixos 호출로 데이터 받기 ///////////////////
    useEffect(() => {
        const lyricsData:Lyric[] = singleData.data.lyrics;
        const blankData:Word[] = singleData.data.words;
        setLyrics([...lyricsData]);
        setBlankWord([...blankData]);
    },[])
    /////////////////////////////////////////////

    // FootVar에서 답안이 입력되었을 때
    useEffect(() => {
        if(answer === "") return; // 토글로 바꿔라..
        // 현재 답이랑 입력된 답이랑 비교한뒤에 같을경우1 스타일변경 다를경우 스타일2 건너뛰기는 다른버튼으로
        // 그리고 빈칸이 없는 곳은 입력버튼 비활성화
        let blankNum = 0;
        blanksRef.current.forEach(el => {
            const sentenceIdx = el?.dataset.sentence;
            const isSolve = el?.dataset.solve;
            if (sentenceIdx == `${idx}` && isSolve === '0') {
                blankNum++;
            }
        });

        let targetBlank = blanksRef.current.find(el => {
            const sentenceIdx = el?.dataset.sentence;
            const isSolve = el?.dataset.solve;
            console.log("isSolve,", isSolve);
            return sentenceIdx == `${idx}` && (isSolve == `0` || isSolve == '1');
        }) ?? null;

        
        

        // 만약 다음게 존재하면 다시 재생? 아님 그냥 멈추기 이런식으로
        // 구간 다시재생
        const solution = targetBlank?.textContent;

        if(answer == solution){
            if(targetBlank){
                targetBlank.dataset.solve = "2";
                targetBlank.className="text-green-800"
            }
        }else {
            if(targetBlank){
                targetBlank.dataset.solve = "1";
                targetBlank.className="text-red-800"
            }
        }
        
        if(blankNum < 1){ // blankNum이 0 일때는 스무스하게 넘어가도록 -> 이거 그냥 그 index셋 주면됨
            handleLyricsClick(idx+1, lyrics[idx+1].isBlank, lyrics[idx+1].startTime, lyrics[idx+1].endTime);
        }
       
    },[toggleSubmit])

    useEffect(() => {
        lyricsRef.current[idx]?.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }, [idx])
    
    const handleLyricsClick = (currIdx: number, blank: boolean, start: number, end: number) => {
        /*
        선택된 Element
        console.log(lyricsRef.current[index]);
        */
        lyricsRef.current[currIdx]?.scrollIntoView({ behavior: 'smooth', block: 'center' });

        // single page -> player (가사의 시작시간, 종료시간)
        onSetInfo(currIdx, blank, start, end);
    }
    

    return(
        <div className="h-96 overflow-x-auto">
            {lyrics.map((lyric, i) => {
                return(
                    <div 
                    key={i} 
                    className={idx == i ? "h-10 text-xl bg-primary-800 text-white" : "h-10"} 
                    ref={(el) => lyricsRef.current[i] = el} 
                    onClick={() => 
                    handleLyricsClick(i, lyric.isBlank, lyric.startTime, lyric.endTime)}>
                        {lyric.lyric.map((word, j) => {
                            let isBlank:boolean = false;
                            let blankIdx:number = 0;

                            blankWord.forEach((blank) => {
                                if(word == blank.word && i == blank.sentenceIndex && j == blank.wordIndex){
                                    isBlank = true;
                                    blankIdx = blank.singlePlayWordId; //이거 고유 인덱스인지 확인
                                }
                            })
                            //만약 해당 단어가 빈칸이 필요하다면 -> isBlank 속성 값 결정
                            //data-isSolve: 0=미해결, 1=오답, 2=정답
                            return (
                                isBlank ? 
                                (<span 
                                key={j} 
                                className="bg-secondary-800 rounded-3xl text-secondary-800"
                                ref={(el) => blanksRef.current[blankIdx] = el}
                                data-sentence ={i}
                                data-solve="0"> 
                                {word}
                                </span>) 
                                : (<span key={j}> {word} </span>)
                            );
                        })}
                    </div>
                );
            })}
        </div>
    )
}

export default Lyrics;