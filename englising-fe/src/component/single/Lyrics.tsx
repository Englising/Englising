import { useEffect, useRef, useState } from "react";
import { singleData } from "./example.tsx"

interface Props {
    onSetInfo(currIdx: number, start: number, end: number): void;
}

const Lyrics = ({onSetInfo}:Props) => {
    const [lyrics, setLyrics] = useState<Lyric[]>([]);
    const scrollRef = useRef<(HTMLDivElement | null)[]>([]);
    
    interface Lyric {
        start_time: number;
        end_time: number;
        lyric: string[];
    }
    
    // 실제론 axios를 사용한 호출이 필요함 (sing)
    const data:Lyric[] = singleData.data.lyrics;

    useEffect(() => {
        setLyrics([...data]);
    },[])
    
    const handleLyricsClick = (currIdx: number, start: number, end: number) => {
        /*
        선택된 Element
        console.log(scrollRef.current[index]);
        */
        scrollRef.current[currIdx]?.scrollIntoView({ behavior: 'smooth', block: 'center' });

        // single page -> player (가사의 시작시간, 종료시간)
        onSetInfo(currIdx, start, end);
    }
    return(
        <div className="h-96 overflow-x-auto">
            {lyrics.map((lyric, i) => {
                return(
                    <div key={i} 
                    className="h-10" 
                    ref={(el) => scrollRef.current[i] = el} 
                    onClick={() => 
                    handleLyricsClick(i, lyric.start_time, lyric.end_time)}>
                        {lyric.lyric.map((word, i) => {
                            return <span key={i}> {word} </span>
                        })}
                    </div>
                );
            })}
        </div>
    )
}

export default Lyrics;