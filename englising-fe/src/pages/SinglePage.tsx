import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { getSinglePlayData } from "../util/SinglePlayAPI";
import Lyrics from "../component/single/Lyrics";
import MusicPlayer from "../component/single/MusicPlayer";
import FooterVar from "../component/single/FooterVar";
import StartModal from "../component/single/StartModal";
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
    singleplayWordId: number;
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
    const navigate = useNavigate();

    const { state } = useLocation();
    const { img } = state;

    const { trackId, level } = useParams<{
        trackId: string,
        level: string,
    }>();

    const [toggleCurrReplay, setToggleCurrReplay] = useState<number>(0);

    const [showStartModal, setShowStartModal] = useState<boolean>(true);

    const [singlePlayId, setSingPlayId] = useState<number>(0);

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
        hintNum: 0,
    });

    const onCurrReplay = (): void => {
        setToggleCurrReplay((toggleCurrReplay + 1) % 2);
    }


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

    const onSetIsBlank = (sentenceIdx: number): void => {
        let temp = singleData;
        temp.lyrics[sentenceIdx].isBlank = false;

        setSingleData({
            ...temp
        });
    }

    const onLyricMove = (index: number): void => {
        const lyric = singleData?.lyrics[index];
        if (lyric != undefined) {
            onSetInfo(index, lyric.isBlank, lyric.startTime, lyric.endTime);
        }
    }

    const onGameStart = (): void => {
        setShowStartModal(false);
    }

    useEffect(() => {

        const data = {
            "trackId": parseInt(trackId || "0"),
            "level": parseInt(level || "1")
        }
      
        const getData = async () => {
            try {
                const singleData = await getSinglePlayData(data);
                setSingPlayId(singleData.data.singlePlayId);
                setSingleData(singleData.data);
                setProgressInfo({
                    totalWord: singleData.data.totalWordCnt, // 나중에 axios로 받아올 것
                    rightWord: 0,
                    hintNum: 3,            
                });
            } catch (error) {
                console.error('Error fetching data:', error)
            }
        }
        getData();
    }, [])
    
    useEffect(() => {
        if (progressInfo.totalWord == 0) return;
        if (progressInfo.rightWord == progressInfo.totalWord) {
            navigate(`/SinglePlay/result/${singlePlayId}`, { state: { ...state } } );
        }
    }, [progressInfo.totalWord])

return (
    <div className="bg-cover bg-center h-screen w-screen p-0 m-0 relative z-10" style={{ backgroundImage: `url(${img})` }}>
        <div className="h-svh w-screen flex flex-col bg-black bg-opacity-80 items-center select-none">
            {showStartModal ? (<div className="relative z-10">
                <StartModal onGameStart={onGameStart} />
            </div>) : (<></>)}
            <div className="h-[90%] w-9/12 flex">
                <div className="w-2/5 h-full items-center">
                    <MusicPlayer onSetInfoIdx={onSetInfoIdx} playInfo={playInfo} progressInfo={progressInfo} showStartModal={showStartModal} /> 
                </div>
                <div className="w-3/5 flex items-center justify-center">
                    <Lyrics onSetInfo={onSetInfo} onSetProgressInfo={onSetProgressInfo} onSetIsBlank={onSetIsBlank} playInfo={playInfo} answerInfo={answerInfo} singleData={singleData} showStartModal={showStartModal} toggleCurrReplay={toggleCurrReplay} />
                </div>
            </div>
            <div className="w-full h-[10%] bg-black flex justify-center">
                <FooterVar onSetAnswer={onSetAnswer} onLyricMove={onLyricMove} onCurrReplay={onCurrReplay} idx={playInfo.idx} singlePlayId={singlePlayId}/>
            </div>
        </div>
    </div>
    );
};

export default SinglePage;