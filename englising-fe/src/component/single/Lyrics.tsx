import { useEffect, useRef, useState } from "react";
import { PlayInfo, SingleData, Lyric, Word, AnswerInfo } from "../../pages/SinglePage.tsx";
import HintModal from "./HintModal.tsx";

interface Props {
    onSetInfo(currIdx: number,  blank: boolean, start: number, end: number): void,
    playInfo: PlayInfo,
    singleData: SingleData,
    answerInfo: AnswerInfo,
}

const Lyrics = ({onSetInfo, answerInfo, playInfo, singleData}:Props) => {
    const {idx} = playInfo;
    const {answer, toggleSubmit} = answerInfo;
    const [lyrics, setLyrics] = useState<Lyric[]>([]);
    const [blankWord, setBlankWord] = useState<Word[]>([]);
    const lyricsRef = useRef<(HTMLDivElement | null)[]>([]);
    const blanksRef = useRef<(HTMLSpanElement | null)[]>([]);
    const [showModal, setShowModal] = useState<boolean>(false);
    const [hintWord, setHintWord] = useState<string>("");
    const [hintNum, setHintNum] = useState<number>(3);
    
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
        if(answer === "") return; 
        
        // 같은 문장내에 빈칸의 개수 // 
        // 오답이랑 빈칸 나눠서 해야할 듯
        let blankNum = 0; 
        let incorrectNum = 0;
        blanksRef.current.forEach(el => {
            const sentenceIdx = el?.dataset.sentence;
            const isSolve = el?.dataset.solve;
            if (sentenceIdx == `${idx}`) {
                if (isSolve === '0') blankNum++;
                if (isSolve === '1') incorrectNum++;
            }
        });

        // 빈칸의 dom을 가져오기
        let targetBlank = blanksRef.current.find(el => {
            const sentenceIdx = el?.dataset.sentence;
            const isSolve = el?.dataset.solve;
            console.log("isSolve,", isSolve);
            return sentenceIdx == `${idx}` && isSolve == `0`; 
        }) ?? null;

        // 빈칸을 먼저 찾고 없으면 그때 오답인거 가져오기
        if(targetBlank == null) {
            targetBlank = blanksRef.current.find(el => {
                const sentenceIdx = el?.dataset.sentence;
                const isSolve = el?.dataset.solve;
                return sentenceIdx == `${idx}` && isSolve == '1'; 
            }) ?? null;
        }

        // 빈칸에 들어갈 정답 가져오기
        const solution = targetBlank?.textContent;

        if(answer == solution){
            if(targetBlank){
                targetBlank.dataset.solve = "2";
                targetBlank.className="text-green-800"
            }

            // 문장에 정답을 모두 맞췄을때, SinglePage Data 자체를 바꿔줌 (여긴 더이상 빈칸이 없어!)
            if(blankNum == 1 && incorrectNum == 0){
                lyrics[idx].isBlank = !lyrics[idx].isBlank;
            }
        }else {
            if(targetBlank){
                targetBlank.dataset.solve = "1";
                targetBlank.className="text-red-800"
            }
        }
        // 마지막 빈칸을 등록했을때 or 마지막 오답이 수정되었을때 넘어감
        if(blankNum == 1 || incorrectNum <= 1 && blankNum == 0){ 
            handleLyricsClick(idx+1, lyrics[idx+1].isBlank, lyrics[idx+1].startTime, lyrics[idx+1].endTime);
        }
       
    },[toggleSubmit])

    useEffect(() => {
        lyricsRef.current[idx]?.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }, [idx])
    
    const handleLyricsClick = (currIdx: number, blank: boolean, start: number, end: number): void => {
        /*
        선택된 Element
        console.log(lyricsRef.current[index]);
        */
        lyricsRef.current[currIdx]?.scrollIntoView({ behavior: 'smooth', block: 'center' });

        // single page -> player (가사의 시작시간, 종료시간)
        onSetInfo(currIdx, blank, start, end);
    }
    
    const handleHintClick = (e: React.MouseEvent, word: string): void => {
        e.stopPropagation();
        setHintWord(word);
        setShowModal(true);
    }

const speak = (word: string): void => {
    const voicesChangedHandler = () => {
        const voices: SpeechSynthesisVoice[] = speechSynthesis.getVoices();
        
        // 목소리를 설정하고 발화
        const utter: any = new SpeechSynthesisUtterance(word);
        utter.voice = voices[2]; 
        speechSynthesis.speak(utter);

        // 이벤트 핸들러 제거
        speechSynthesis.onvoiceschanged = null;
    };

    // 이벤트 핸들러 등록
    speechSynthesis.onvoiceschanged = voicesChangedHandler;

    // 현재 목소리를 가져와서 발화
    const voices: SpeechSynthesisVoice[] = speechSynthesis.getVoices();
    if (voices.length > 0) {
        const utter: any = new SpeechSynthesisUtterance(word);
        utter.voice = voices[2]; 
        speechSynthesis.speak(utter);
    }
};

    const onUse = (word: string) => { //힌트 사용 
        setHintNum(hintNum - 1);
        setShowModal(!showModal);
        speak(word);
    }

    const onCancel = () => { //힌트 취소 -> 힌트 모두 소진시 onCancel만 쓸거임
        setShowModal(!showModal);
    }

    return(
        <div className="w-[95%] h-[90%] flex flex-col items-center py-10 px-20 box-border text-center overflow-y-scroll select-none">
            {showModal ? (<div className="relative">
                <HintModal hintWord={hintWord} hintNum={hintNum} onUse={onUse} onCancel={onCancel} />
            </div>) : (<></>)}
            {lyrics.map((lyric, i) => {
                return(
                    <div 
                    key={i} 
                    className={idx == i ? 
                        `w-[80%] min-h-40 flex justify-center items-center text-4xl bg-black/50 rounded-xl text-white` : 
                        `w-[80%] min-h-36 flex justify-center items-center text-3xl text-primary-300`} 
                    ref={(el) => lyricsRef.current[i] = el} 
                    onClick={() => 
                    handleLyricsClick(i, lyric.isBlank, lyric.startTime, lyric.endTime)}>
                        <div>
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
                                    (idx == i ?
                                        (<span 
                                        key={j} 
                                        className={"mx-2 bg-white rounded-lg text-white"}
                                        ref={(el) => blanksRef.current[blankIdx] = el}
                                        data-sentence ={i}
                                        data-solve="0"
                                        onClick={(e) => handleHintClick(e, word)}
                                        > 
                                        {word}
                                        </span>) :
                                        (<span 
                                        key={j} 
                                        className={"mx-2 bg-white rounded-lg text-white"}
                                        ref={(el) => blanksRef.current[blankIdx] = el}
                                        data-sentence ={i}
                                        data-solve="0"
                                        > 
                                        {word}
                                        </span>))
                                 
                                : (<span key={j}> {word} </span>)
                            );
                        })}
                        </div>
                    </div>
                );
            })}
        </div>
    )
}

export default Lyrics;