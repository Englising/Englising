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
    toggle: number
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
        toggle: 0
    });

    const [answer, setAnswer] = useState<string>("");

    const onSetAnswer = (answer: string): void => {
        setAnswer(answer);
    }

    const onSetInfo = (currIdx: number, blank: boolean, start: number, end: number): void => {
        setPlayInfo({
            idx: currIdx,
            isBlank: blank,
            startTime: start,
            endTime: end,
            toggle: (playInfo.toggle+1)%2
        })
    }

    const onSetInfoIdx = (currIdx: number): void => {
        const lyric = singleData.data.lyrics[currIdx];
        setPlayInfo({
            idx: currIdx,
            isBlank: lyric.isBlank,
            startTime: lyric.startTime,
            endTime: lyric.endTime,
            toggle: playInfo.toggle
        })
    }

    return (
        <div>            
            <div className="flex">
                <MusicPlayer onSetInfoIdx = {onSetInfoIdx} playInfo = {playInfo} />
                <Lyrics onSetInfo = {onSetInfo} playInfo = {playInfo} answer = {answer} singleData={singleData}/>
            </div>
            <div>
                <FooterVar onSetAnswer = {onSetAnswer}/>
            </div>
        </div>
    );
};

export default SinglePage;