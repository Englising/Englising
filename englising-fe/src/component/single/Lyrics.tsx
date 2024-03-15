import { useEffect, useRef, useState } from "react";
import { PlayInfo, SingleData, Lyric, Word} from "../../pages/SinglePage.tsx";

interface Props {
    onSetInfo(currIdx: number, start: number, end: number): void,
    playInfo: PlayInfo,
    singleData: SingleData,
    answer: string,
}


const Lyrics = ({onSetInfo, playInfo, answer, singleData}:Props) => {
    const [lyrics, setLyrics] = useState<Lyric[]>([]);
    const [blank, setBlank] = useState<Word[]>([]);
    const scrollRef = useRef<(HTMLDivElement | null)[]>([]);
    const {idx, startTime, endTime, toggle} = playInfo;
    
    // aixos 호출로 데이터 받기 ///////////////////
   
    

    useEffect(() => {
        const lyricsData:Lyric[] = singleData.data.lyrics;
        const blankData:Word[] = singleData.data.words;
        setLyrics([...lyricsData]);
        setBlank([...blankData]);
    },[])

    /////////////////////////////////////////////

    // FootVar에서 답안이 입력되었을 때
    useEffect(() => {
        if(answer === "") return;
        handleLyricsClick(idx+1, lyrics[idx+1].startTime, lyrics[idx+1].endTime)
    },[answer])
    
    const handleLyricsClick = (currIdx: number, start: number, end: number) => {
        /*
        선택된 Element
        console.log(scrollRef.current[index]);
        */
        scrollRef.current[currIdx]?.scrollIntoView({ behavior: 'smooth', block: 'center' });

        // single page -> player (가사의 시작시간, 종료시간)
        onSetInfo(currIdx, start, end);
    }
    
    const testClick = () => {
        lyrics[0].lyric[0]
        
    }
    return(
        <div className="h-96 overflow-x-auto">
            {lyrics.map((lyric, i) => {
                return(
                    <div 
                    key={i} 
                    className="h-10" 
                    ref={(el) => scrollRef.current[i] = el} 
                    onClick={() => 
                    handleLyricsClick(i, lyric.startTime, lyric.endTime)}>
                        {lyric.lyric.map((word, j) => {
                            let isBlank:boolean = false;

                            blank.forEach((curr) => {
                                if(word == curr.word) isBlank = true;
                            })
                            //만약 해당 단어가 빈칸이 필요하다면 -> isBlank 속성 값 결정
                            return (
                                isBlank ? 
                                (<span 
                                key={j} 
                                data-sntIdx={i} 
                                data-wdInx={j} 
                                className="bg-secondary-800 rounded-3xl text-secondary-800"> 
                                {word}
                                </span>) 
                                : (<span key={j} data-blank={isBlank}> {word} </span>)
                            );
                        })}
                    </div>
                );
            })}
        </div>
    )
}

export default Lyrics;