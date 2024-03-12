import { useState } from "react";
import Lyrics from "../component/single/Lyrics";
import MusicPlayer from "../component/single/MusicPlayer";


const SinglePage = () => {
    const [startTime, setStartTime] = useState<number>(0);
    const [endTime, setEndTime] = useState<number>(0);
    const [toggle, setToggle] = useState<number>(0);

    const onSetTime = (start: number, end: number): void => {
        setStartTime(start);
        setEndTime(end);
        setToggle((toggle+1)%2);
    }

    return (
        <div className="flex">
            <MusicPlayer startTime={startTime} endTime={endTime} toggle={toggle}/>
            <Lyrics onSetTime = {onSetTime}/>
        </div>
    );
};

export default SinglePage;