import { useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import { getSinglePlayData } from "../util/SinglePlayAPI";
import Lyrics from "../component/single/Lyrics";
import MusicPlayer from "../component/single/MusicPlayer";
import FooterVar from "../component/single/FooterVar";
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
    lyrics: Lyric[];
    rightWordCnt: number;
    singlePlayId: number;
    totalWordCnt: number;
    words: Word[];
}

const SinglePage = () => {
    const { trackId, level } = useParams<{
        trackId: string,
        level: string,
    }>();

    
    const { state } = useLocation();
    const { img } = state;

    const [bgImg, setBgImg] = useState<string>("");

    const [singleData, setSingleData] = useState<SingleData>({
        lyrics: [],
        rightWordCnt: 0,
        singlePlayId: 0,
        totalWordCnt: 0,
        words: []
    });

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
        totalWord: 0, // 나중에 axios로 받아올 것
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
        const lyric = singleData?.lyrics[currIdx];
        if (lyric != undefined) {
            setPlayInfo({
            idx: currIdx,
            isBlank: lyric.isBlank,
            startTime: lyric.startTime,
            endTime: lyric.endTime,
            toggleNext: playInfo.toggleNext // 계속 재생상태
        })
        }
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
        const lyric = singleData?.lyrics[playInfo.idx + 1];
        if (lyric != undefined) {
            onSetInfo(playInfo.idx+1, lyric.isBlank, lyric.startTime, lyric.endTime);
        }
    }

    useEffect(() => {

        const data = {
            "trackId": parseInt(trackId || "0"),
            "level": parseInt(level || "1")
        }

      
        const getData = async () => {
            try {
                const singleData = await getSinglePlayData(data);
                setSingleData(singleData.data);
            } catch (error) {
                console.error('Error fetching data:', error)
            }
        }
        setBgImg(`bg-[url('${img}')] bg-cover bg-center h-screen w-screen p-0 m-0`);
        getData();
    },[])

    useEffect(() => {
        setProgressInfo({
            totalWord: singleData?.totalWordCnt, // 나중에 axios로 받아올 것
            rightWord: 0,
            hintNum: 3,            
        });
    }, [singleData])


return (
        <div className={bgImg}>            
            <div className="h-svh w-screen flex flex-col bg-black bg-opacity-80 items-center">
                <div className="h-[90%] w-9/12 flex">
                    <div className="w-2/5 h-full items-center">
                    <MusicPlayer onSetInfoIdx={onSetInfoIdx} playInfo={playInfo} progressInfo={progressInfo}/> 
                    </div>
                    <div className="w-3/5 flex items-center justify-center">
                        <Lyrics onSetInfo = {onSetInfo} onSetProgressInfo = {onSetProgressInfo} playInfo = {playInfo} answerInfo = {answerInfo} singleData={singleData}/>
                    </div>
                </div>
                <div className="w-full h-[10%] bg-black flex justify-center">
                <FooterVar onSetAnswer={onSetAnswer} onSkip={onSkip} idx={playInfo.idx} />
                </div>
                </div>
        </div>
    );
};

export default SinglePage;