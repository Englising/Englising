import { useEffect, useRef, useState } from "react";
import ReactPlayer from "react-player";
import { PlayInfo } from "../../pages/SinglePage.tsx";

interface Props {
    playInfo: PlayInfo
}

//임시 데이터: 음원의 id를 보내주면, 문장마다 시작하는 시간 리스트를 받아오는 코드로 대체
const timeData = [7.5, 12.0, 15.9, 20.0, 24.3, 28.3, 32.4, 36.4, 41.4, 49.7, 56.4, 60.6, 65.0, 68.8, 90.3, 98.4];
const MusicPlayer = ({playInfo}:Props ) => {
    const {idx, startTime, endTime, toggle} = playInfo


    const [url, setUrl] = useState<string>("https://www.youtube.com/watch?v=EVJjmMW7eII");
    const [playing, setPlaying] = useState<boolean>(true);
    const [muted, setMuted] = useState<boolean>(true);
    const player = useRef<ReactPlayer | null>(null);

    const handleReady = () => {
        setMuted(false);
    }

    useEffect(() => {
        player.current?.seekTo(startTime);
    },[toggle])

    return(
        <div>
            <ReactPlayer
                ref={player}
                url= {url}
                playing = {playing} 
                muted = {muted}
                controls = {true} // 기본 control를 띄울 것인지 - 나중에 지울것
                loop = {true} // 노래 재생이 끝나면 loop를 돌리는지
                onReady={handleReady} // 재생 준비가 완료되면 호출될 함수? 재생 준비 기준이 뭔지
            />         
        </div>
    )
}

export default MusicPlayer;