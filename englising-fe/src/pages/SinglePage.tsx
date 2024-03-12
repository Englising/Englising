import { useState } from "react";
import Lyrics from "../component/single/Lyrics";
import MusicPlayer from "../component/single/MusicPlayer";


export interface PlayInfo {
    idx: number,
    startTime: number,
    endTime: number,
    toggle: number
}

const SinglePage = () => {
    const [playInfo, setPlayInfo] = useState<PlayInfo>({
        idx: 0,
        startTime: 0,
        endTime: 0,
        toggle: 0
    });

    const onSetInfo = (currIdx: number, start: number, end: number): void => {
        setPlayInfo({
            idx: currIdx,
            startTime: start,
            endTime: end,
            toggle: (playInfo.toggle+1)%2
        })
    }

    return (
        <div className="flex">
            <MusicPlayer playInfo = {playInfo}/>
            <Lyrics onSetInfo = {onSetInfo}/>
        </div>
    );
};

export default SinglePage;