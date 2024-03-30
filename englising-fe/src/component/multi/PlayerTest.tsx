import { useState } from "react";
import MultiMusicPlayer from "./MultiMusicPlayer";

export interface PlayInfo {
    url: string
    startTime: number,
    endTime: number,
    speed: number,
    onPlay: number
}

const PlayerTest = () => {
    const [playInfo, setPlayInfo] = useState <PlayInfo>({
        url: "https://www.youtube.com/watch?v=EVJjmMW7eII", //처음에 초기화 해줘야함.
        startTime: -1,
        endTime: -1,
        speed: 1,
        onPlay: 0
    })
    
    const evernt1 = () => {
        // 특정시간에 도달하면 playInfo 값 넣어주기 (url, startTime, endTime, speed)
        setPlayInfo({
            ...playInfo,
            startTime: 60,
            endTime: 65,
            speed: 1,
            onPlay: ((playInfo.onPlay + 1) % 2)
        })
    }

    const evernt2 = () => {
        // 특정시간에 도달하면 playInfo 값 넣어주기 (url, startTime, endTime, speed)
        setPlayInfo({
            ...playInfo,
            startTime: 60,
            endTime: 65,
            speed: 2,
            onPlay: ((playInfo.onPlay + 1) % 2)
        })
    }

    const evernt3 = () => {
        // 특정시간에 도달하면 playInfo 값 넣어주기 (url, startTime, endTime, speed)
        setPlayInfo({
            ...playInfo,
            startTime: 60,
            endTime: 65,
            speed: 0.7,
            onPlay: ((playInfo.onPlay + 1) % 2)
        })
    }

 
    return (
        <>
            <div className="flex">
                <div className="bg-black w-80 m-3 text-white text-center" onClick={evernt1}>특정 시간에 도달했을때 발생하는 이벤트</div>
                <div className="bg-black w-80 m-3 text-white text-center" onClick={evernt2}>2배 힌트 이벤트</div>
                <div className="bg-black w-80 m-3 text-white text-center" onClick={evernt3}>0.75배 힌트 이벤트</div>
            </div>

            <div>
                <MultiMusicPlayer playInfo={playInfo} />
            </div>
        </>
    )
}
export default PlayerTest;