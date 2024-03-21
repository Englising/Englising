import { useState } from "react";
import Lyrics from "../component/single/Lyrics";
import MusicPlayer from "../component/single/MusicPlayer";
import FooterVar from "../component/single/FooterVar";
import { singleData } from "../component/single/example"
// 싱글데이터를 가져올 수 있는 api를 설계

export interface PlayInfo {
    idx: number,
    isBlank: boolean,
    startTime: number,
    endTime: number,
    toggleNext: number
}

export interface AnswerInfo {
    answer: string,
    toggleSubmit: number
}

export interface ProgressInfo {
    totalWord: number,
    rightWord: number,
    hintNum: number
}

export interface Lyric {
    isBlank: boolean,
    startTime: number;
    endTime: number;
    lyric: string[];
}

export interface Word {
    singlePlayWordId: number;
    sentenceIndex: number;
    wordIndex: number;
    word: string;
    isRight: boolean;
}

export interface SingleData {
    status: number;
    message: string;
    data: {
        singleplay_id: number;
        lyrics: Lyric[];
        words: Word[];
        total_word_cnt: number;
        right_word_cnt: number;
    };
}

const SinglePage = () => {
    const [playInfo, setPlayInfo] = useState<PlayInfo>({
        idx: 0,
        isBlank: false,
        startTime: 0,
        endTime: 0,
        toggleNext: 0
    });

    const [answerInfo, setAnswerInfo] = useState<AnswerInfo>({
        answer: "",
        toggleSubmit: 0
    });

    const [progressInfo, setProgressInfo] = useState<ProgressInfo>({
        totalWord: singleData.data.total_word_cnt, // 나중에 axios로 받아올 것
        rightWord: 0,
        hintNum: 3,
    });


    const onSetInfo = (currIdx: number, blank: boolean, start: number, end: number): void => {
        setPlayInfo({
            idx: currIdx,
            isBlank: blank,
            startTime: start,
            endTime: end,
            toggleNext: (playInfo.toggleNext+1)%2 // 일시정지 -> 재생
        })
    }

    const onSetInfoIdx = (currIdx: number): void => {
        const lyric = singleData.data.lyrics[currIdx];
        setPlayInfo({
            idx: currIdx,
            isBlank: lyric.isBlank,
            startTime: lyric.startTime,
            endTime: lyric.endTime,
            toggleNext: playInfo.toggleNext // 계속 재생상태
        })
    }

    const onSetProgressInfo = (type: string, data: number = 1): void => {
        if (type == "rightWord") {
            setProgressInfo({
                ...progressInfo,
                rightWord: progressInfo.rightWord + data
            });
        } else if (type == "hintNum") {
            setProgressInfo({
                ...progressInfo,
                hintNum: data
            });
        }
    }

    const onSetAnswer = (answer: string): void => {
        setAnswerInfo({
            answer: answer,
            toggleSubmit: (answerInfo.toggleSubmit+1)%2
        });
    }

    const onSkip = async ():Promise<void> => {
        const lyric = singleData.data.lyrics[playInfo.idx+1];
        onSetInfo(playInfo.idx+1, lyric.isBlank, lyric.startTime, lyric.endTime);
    }

    //동적으로 url 구성
    const ur1 = 'src/assets/2002.jpg';

    return (
        <div className={`bg-[url('src/assets/bam.PNG')] bg-cover bg-center h-svh w-screen p-0 m-0`}>            
            <div className="flex flex-col  bg-black h-svh w-screen bg-opacity-80">
                <div className="flex h-[90%]">
                    <div className="w-1/3">
                        <MusicPlayer onSetInfoIdx={onSetInfoIdx} playInfo={playInfo} progressInfo={progressInfo} /> 
                    </div>
                    <div className="w-2/3 flex items-center justify-center">
                        <Lyrics onSetInfo = {onSetInfo} onSetProgressInfo = {onSetProgressInfo} playInfo = {playInfo} answerInfo = {answerInfo} singleData={singleData}/>
                    </div>
                </div>
                <div className="h-[10%] bg-black flex justify-center">
                    <FooterVar onSetAnswer = {onSetAnswer} onSkip = {onSkip}/>
                </div>
                </div>
        </div>
    );
};

export default SinglePage;