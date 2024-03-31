import { useEffect, useRef, useState } from "react";
import { PlayInfo, SingleData, Lyric, Word, AnswerInfo } from "../../pages/SinglePage.tsx";
import HintModal from "./HintModal.tsx";
import { singlePlayWordCheck } from "../../util/SinglePlayAPI.tsx";
import useSpeek from "../../hooks/useSpeek.tsx";

interface Props {
    onSetInfo(currIdx: number, blank: boolean, start: number, end: number): void,
    onSetProgressInfo(type: string, data?: number): void,
    onSetIsBlank(sentenceIdx: number): void
    playInfo: PlayInfo,
    singleData: SingleData|undefined,
    answerInfo: AnswerInfo,
    showStartModal: boolean;
    toggleCurrReplay: number;
}

const blankSort = (a:Word, b:Word) => {
    if (a.sentenceIndex == b.sentenceIndex) {
        return a.wordIndex - b.wordIndex;
    } else {
        return a.sentenceIndex - b.sentenceIndex;
    }
}

const Lyrics = ({ onSetInfo, onSetProgressInfo, onSetIsBlank, answerInfo, playInfo, singleData, showStartModal, toggleCurrReplay }: Props) => {
    { /* 현재 문장, 제출 답안정보 */ }
    const {idx} = playInfo;
    const { answer, toggleSubmit } = answerInfo;
    
    {/* 가사 및 빈칸 Data */}
    const [lyrics, setLyrics] = useState<Lyric[] | undefined>([]);
    const [blankWord, setBlankWord] = useState<Word[] | undefined>([]);

    {/* HTML 조작 */ }
    const lyricsRef = useRef<(HTMLDivElement | null)[]>([]);
    const blanksRef = useRef<(HTMLSpanElement | null)[]>([]);

    {/* 힌트 */ }
    const [showHintModal, setShowHintModal] = useState<boolean>(false);
    const [hintWord, setHintWord] = useState<string>("");
    const [hintNum, setHintNum] = useState<number>(3);
    
    {/* 오답간 구분을 위한 변수*/ }
    const preIdx = useRef<number>(-1);

    useEffect(() => {
        const lyricsData:Lyric[] | undefined = singleData?.lyrics;
        const blankData:Word[] | undefined = singleData?.words.sort(blankSort);
        if (lyricsData != undefined && blankData != undefined) {
            setLyrics([...lyricsData]);
            setBlankWord([...blankData]);
        }
    },[singleData])

    // FootVar에서 답안이 입력되었을 때, 실행되는 hook
    useEffect(() => {
        if(answer === "") return; 
        
        // 현재 재생중인 문장에서, 빈칸과 오답 개수를 각각 카운팅
        // 빈칸이 우선으로 정답이 채워지게 만들기 위함
        let blankNum = 0; 
        let incorrectNum = 0;
        let twiceIncorrectNum = 0;
        blanksRef.current.forEach(el => {
            const sentenceIdx = el?.dataset.sentence;
            const isSolve = el?.dataset.solve;
            if (sentenceIdx == `${idx}`) {
                if (isSolve === '0') blankNum++;
                if (isSolve === '1') incorrectNum++;
                if (isSolve === '3') twiceIncorrectNum++;
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

        if (targetBlank == null) return;
        
        const targetIsSolve = targetBlank.dataset.solve;

        // 빈칸에 들어갈 정답 가져오기
        
        const solutionIdx:string = targetBlank?.dataset.index || "0";
        let solution:string = "";
        if (lyrics != undefined ) {
            solution = lyrics[idx].lyric[parseInt(solutionIdx)];
        }
        
        const data = {
            singleplayId: singleData?.singlePlayId || 0, 
            singleplayWordId: parseInt(targetBlank?.dataset.wordid || "0"),
            word: answer.toLowerCase()
        }
        console.log(data.singleplayId, targetBlank?.dataset.wordid, data.word)
        singlePlayWordCheck(data);

        if(answer.toLowerCase() == solution.toLowerCase()){
            if (targetBlank) {
                // 정답시 스타일변경
                targetBlank.dataset.solve = "2";
                targetBlank.className = "text-[#6DD66D] font-bold"
                targetBlank.textContent = solution;

                //정답시 맞은 단어 개수 변경
                onSetProgressInfo("rightWord",);
            }

             // 문장에 정답을 모두 맞췄을때, SinglePage Data 자체를 바꿔줌 (여긴 더이상 빈칸이 없어!)
            if(twiceIncorrectNum == 0 && (blankNum == 1 && incorrectNum == 0 || blankNum == 0 && incorrectNum == 1)){
                onSetIsBlank(idx);
            }

        }else {
            if (targetBlank) {
                let wrongAnswer = answer;
                if (wrongAnswer.length > solution?.length) {
                    wrongAnswer = wrongAnswer.slice(0, solution?.length) + "..";
                }
                if (targetIsSolve == "1") {
                    // 오답을 또 들렸을 경우
                    targetBlank.dataset.solve = "3";
                    targetBlank.className = "rounded-lg text-[#FF4646] font-bold"
                    targetBlank.textContent = wrongAnswer;
                } else {
                    // 오답시 스타일변경
                    targetBlank.dataset.solve = "1";
                    targetBlank.className = "rounded-lg text-[#FF4646] font-bold"
                    targetBlank.textContent = wrongAnswer;
                }
            }
        }
        // 마지막 빈칸을 등록했을때 or 마지막 오답이 수정되었을때 넘어감
        if (blankNum == 1 || incorrectNum <= 1 && blankNum == 0) {
            if(lyrics != undefined) handleLyricsClick(idx+1, lyrics[idx+1].isBlank, lyrics[idx+1].startTime, lyrics[idx+1].endTime);
        }
        
        //targetBlank?.classList.remove('backdrop-blur', 'border', 'border-secondary-500');
    },[toggleSubmit])
    
    // 현재 답안이 입력 될 빈칸을 표시해주는 hook
    useEffect(() => {
        if (idx != preIdx.current) {
            preIdx.current = idx;
            blanksRef.current.map(el => {
                const isSolve = el?.dataset.solve;
                if (isSolve == "3") {
                    if(el) el.dataset.solve = "1";
                }
            })
        }
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
        targetBlank?.classList.add('px-3', 'border-2', 'border-cyan-400', 'shadow-lg', 'shadow-cyan-200');
        targetBlank?.classList.replace('bg-opacity-60', 'bg-opacity-20');
        blanksRef.current.map(el => {
            const idx = el?.dataset.word;
            if (wordIdx != idx) {
                el?.classList.remove('px-3', 'border-2', 'border-cyan-400', 'shadow-lg', 'shadow-cyan-200');
                el?.classList.replace('bg-opacity-20', 'bg-opacity-60');
            }
        });

    }, [idx, toggleSubmit, showStartModal])

    useEffect(() => {
        // 모든 가사 이동이 생길때 
        lyricsRef.current[idx]?.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }, [idx])

    useEffect(() => {
        if (lyrics == undefined) return;
        lyricsRef.current[idx]?.scrollIntoView({ behavior: 'smooth', block: 'center' });
        onSetInfo(idx, lyrics[idx]?.isBlank, lyrics[idx]?.startTime, lyrics[idx]?.endTime);
    },[toggleCurrReplay])
    
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
        setShowHintModal(true);
    }

    const onUse = (word: string) => { //힌트 사용 
        onSetProgressInfo("hintNum", hintNum - 1);
        setHintNum(hintNum - 1);
        setShowHintModal(!showHintModal);
        //정답시 맞은 단어 개수 변경
        useSpeek(word);
    }

    const onCancel = () => { //힌트 취소 -> 힌트 모두 소진시 onCancel만 쓸거임
        setShowHintModal(!showHintModal);
    }

    return(
        <div className="w-full h-[90%] flex flex-col items-center py-10 px-5 box-border text-center overflow-y-scroll scrollbar-webkit">
            {showHintModal ? (<div className="relative">
                <HintModal hintWord={hintWord} hintNum={hintNum} onUse={onUse} onCancel={onCancel} />
            </div>) : (<></>)}
            {lyrics?.map((lyric, i) => {
                return(
                    <div 
                    key={i} 
                    className={idx == i ? 
                        `w-full min-h-[15%] text-[1.4em] flex justify-center items-center bg-black/50 rounded-xl text-white` : 
                        `w-full min-h-[15%] text-[1em] flex justify-center items-center text-primary-300`} 
                    ref={(el) => lyricsRef.current[i] = el} 
                    onClick={() => 
                    handleLyricsClick(i, lyric.isBlank, lyric.startTime, lyric.endTime)}>
                        <div className="flex">
                            {lyric.lyric.map((word, j) => {
                            if(word == " ") return <div>&nbsp;</div>
                            let isBlank:boolean = false;
                            let blankIdx:number = 0;
                            let singlePlayWordId: number = 0;
                                
                            blankWord?.forEach((blank, blankId) => {
                                //console.log("단어 idx:", blank.sentenceIndex, "result:", word.toLowerCase(), blank.word.toLowerCase());
                                if(word.toLowerCase().includes(blank.word.toLowerCase())  && i == blank.sentenceIndex && j == blank.wordIndex){
                                    isBlank = true; 
                                    blankIdx = blankId; 
                                    singlePlayWordId = blank.singleplayWordId;
                                }
                            })
                            //만약 해당 단어가 빈칸이 필요하다면 -> isBlank 속성 값 결정
                            //data-isSolve: 0=미해결, 1=오답, 2=정답, 3=우선 순위 낮은 오답
                            return (
                                isBlank ? 
                                    (<div 
                                        key={j} 
                                        className={"mx-2 bg-white rounded-lg text-white bg-opacity-60 text-opacity-0"}
                                        ref={(el) => blanksRef.current[blankIdx] = el}
                                        data-wordid={singlePlayWordId}
                                        data-index={j}
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
                                    </div>)
                                : (<div key={j}> {word} </div>)
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