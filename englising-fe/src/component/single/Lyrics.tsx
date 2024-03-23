import { useEffect, useRef, useState } from "react";
import { PlayInfo, SingleData, Lyric, Word, AnswerInfo } from "../../pages/SinglePage.tsx";
import HintModal from "./HintModal.tsx";

interface Props {
    onSetInfo(currIdx: number, blank: boolean, start: number, end: number): void,
    onSetProgressInfo(type: string, data?: number): void,
    playInfo: PlayInfo,
    singleData: SingleData,
    answerInfo: AnswerInfo,
}

const Lyrics = ({ onSetInfo, onSetProgressInfo, answerInfo, playInfo, singleData }: Props) => {
    { /* 현재 문장, 제출 답안정보 */ }
    const {idx} = playInfo;
    const { answer, toggleSubmit } = answerInfo;
    
    {/* 가사 및 빈칸 Data */}
    const [lyrics, setLyrics] = useState<Lyric[]>([]);
    const [blankWord, setBlankWord] = useState<Word[]>([]);

    {/* HTML 조작 */ }
    const lyricsRef = useRef<(HTMLDivElement | null)[]>([]);
    const blanksRef = useRef<(HTMLSpanElement | null)[]>([]);
    const [preBlank, setPreBlank] = useState<number>(0);
    const [currBlank, setCurrBlank] = useState<number>(0);

    {/* 힌트 */ }
    const [showModal, setShowModal] = useState<boolean>(false);
    const [hintWord, setHintWord] = useState<string>("");
    const [hintNum, setHintNum] = useState<number>(3);
    
    // aixos 호출로 데이터 받기 -> Single Page에게 위임///////////////////
    useEffect(() => {
        const lyricsData:Lyric[] = singleData.data.lyrics;
        const blankData:Word[] = singleData.data.words;
        setLyrics([...lyricsData]);
        setBlankWord([...blankData]);
    },[])
    /////////////////////////////////////////////

    // FootVar에서 답안이 입력되었을 때, 실행되는 hook
    useEffect(() => {
        if(answer === "") return; 
        
        // 현재 재생중인 문장에서, 빈칸과 오답 개수를 각각 카운팅
        // 빈칸이 우선으로 정답이 채워지게 만들기 위함
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
            if (targetBlank) {
                // 정답시 스타일변경
                targetBlank.dataset.solve = "2";
                targetBlank.className = "text-green-800"
                
                //정답시 맞은 단어 개수 변경
                onSetProgressInfo("rightWord",);
            }

            // 문장에 정답을 모두 맞췄을때, SinglePage Data 자체를 바꿔줌 (여긴 더이상 빈칸이 없어!)
            if(blankNum == 1 && incorrectNum == 0){
                lyrics[idx].isBlank = !lyrics[idx].isBlank;
            }
        }else {
            if (targetBlank) {
                // 오답시 스타일변경
                targetBlank.dataset.solve = "1";
                targetBlank.className="text-red-800"
            }
        }
        // 마지막 빈칸을 등록했을때 or 마지막 오답이 수정되었을때 넘어감
        if(blankNum == 1 || incorrectNum <= 1 && blankNum == 0){ 
            handleLyricsClick(idx+1, lyrics[idx+1].isBlank, lyrics[idx+1].startTime, lyrics[idx+1].endTime);
        }
        
        //targetBlank?.classList.remove('backdrop-blur', 'border', 'border-secondary-500');
    },[toggleSubmit])
    
    // 현재 입력이 될 빈칸을 표시해주는 hook
    useEffect(() => {
        console.log("실행")
        // 빈칸의 dom을 가져오기
        let targetBlank = blanksRef.current.find(el => {
            const sentenceIdx = el?.dataset.sentence;
            const isSolve = el?.dataset.solve;
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


        // 현재 문장에 답이 입력될 빈칸에 효과주기.
        const wordIdx = targetBlank?.dataset.word;
        targetBlank?.classList.add('border-2', 'border-secondary-400','shadow-lg', 'shadow-secondary-200');
        
        blanksRef.current.map(el => {
            const idx = el?.dataset.word;
            if (wordIdx != idx) {
                el?.classList.remove('border-2', 'border-secondary-400', 'shadow-lg', 'shadow-secondary-200');
            }
        });

    },[idx, answer])

    useEffect(() => {
        // 모든 가사 이동이 생길때 
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
        onSetProgressInfo("hintNum", hintNum - 1);
        setHintNum(hintNum - 1);
        setShowModal(!showModal);
        //정답시 맞은 단어 개수 변경
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
                        `w-[90%] min-h-[15%] text-[1.4em] flex justify-center items-center bg-black/50 rounded-xl text-white` : 
                        `w-[90%] min-h-[15%] text-[1em] flex justify-center items-center text-primary-300`} 
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
                                    (<span 
                                        key={j} 
                                        className={"mx-2 bg-white rounded-lg text-white"}
                                        ref={(el) => blanksRef.current[blankIdx] = el}
                                        data-sentence={i}
                                        data-word={blankIdx}
                                        data-solve="0"
                                            onClick={(e) => {
                                                if (idx == i) {
                                                    const solve = e.currentTarget.getAttribute('data-solve');
                                                    if (solve != "2") {
                                                        handleHintClick(e, word)
                                                    }
                                                }
                                            }}
                                    > 
                                        {word}
                                    </span>)
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