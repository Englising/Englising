import { useState } from "react";
import Lyrics from "../component/single/Lyrics";
import MusicPlayer from "../component/single/MusicPlayer";
import FooterVar from "../component/single/FooterVar";
import { singleData } from "../component/single/example"


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

    const onSetAnswer = (answer: string): void => {
        setAnswerInfo({
            answer: answer,
            toggleSubmit: (answerInfo.toggleSubmit+1)%2
        });
    }

    return (
        <div>            
            <div className="flex">
                <MusicPlayer onSetInfoIdx = {onSetInfoIdx} playInfo = {playInfo} />
                <Lyrics onSetInfo = {onSetInfo} playInfo = {playInfo} answerInfo = {answerInfo} singleData={singleData}/>
            </div>
            <div>
                <FooterVar onSetAnswer = {onSetAnswer}/>
            </div>
        </div>
    );
};

export default SinglePage;