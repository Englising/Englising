import { useEffect, useRef, useState } from "react";
import ReactPlayer from "react-player";

interface Props {
    startTime: number;
    endTime: number;
    toggle: number;
}

const MusicPlayer = ({startTime, endTime, toggle}:Props ) => {

    const [url, setUrl] = useState<string>("https://www.youtube.com/watch?v=EVJjmMW7eII");
    const [playing, setPlaying] = useState<boolean>(false);
    const player = useRef<ReactPlayer | null>(null);

    const handleReady = () => {
        player.current?.seekTo(1);
    }

    useEffect(() => {
        player.current?.seekTo(startTime);
    },[toggle])

    return(
        <div>
            <ReactPlayer
                ref={player}
                url= {url}
                playing = {true} //
                controls = {true} // 기본 control를 띄울 것인지 - 나중에 지울것
                loop = {true} // 노래 재생이 끝나면 loop를 돌리는지
                onReady={handleReady} // 재생 준비가 완료되면 호출될 함수? 재생 준비 기준이 뭔지
            />         
        </div>
    )
}

export default MusicPlayer;