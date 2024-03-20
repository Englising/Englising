import ReactPlayer from "react-player";
import { useEffect, useRef, useState } from "react";
import { PlayInfo } from "../../pages/SinglePage.tsx";
import { OnProgressProps } from "react-player/base";

interface Props {
    onSetInfoIdx(currIdx: number):void,
    playInfo: PlayInfo
}

//임시 데이터: 음원의 id를 보내주면, 문장마다 시작하는 시간 리스트를 받아오는 코드로 대체
const timeData = [7.5, 12.0, 15.9, 20.0, 24.3, 28.3, 32.4, 36.4, 41.4, 49.7, 56.4, 60.6, 65.0, 68.8, 90.3, 98.4];
const MusicPlayer = ({onSetInfoIdx, playInfo}:Props ) => {
    let {idx, isBlank, startTime, toggleNext} = playInfo
    // 유튜브 아이디를 주는 axios
    const url = "https://www.youtube.com/watch?v=EVJjmMW7eII";
    const [playing, setPlaying] = useState<boolean>(true);
    const [muted, setMuted] = useState<boolean>(true);
    const player = useRef<ReactPlayer | null>(null);

    const handleReady = () => {
        setMuted(false);
    }
    
    const handleProgress = (e: OnProgressProps) => {
        if(timeData[idx+1] < e.playedSeconds-0.1){
            if(!isBlank){
                onSetInfoIdx(idx+1);
            }else {
                setPlaying(false);
            }
        }
    }

    const handleEnded = () => {
        confirm("노래가 끝났을 때 재생될 코드 넣기");
    }

    // 특정 구간 가사를 누를 때 발생하는 이벤트
    useEffect(() => {
        setPlaying(true);
        player.current?.seekTo(startTime);
    },[toggleNext])

    return(
        <div className="flex flex-col items-center">
            <div>비비 밤양갱</div>
            <div>
                <ReactPlayer
                    width={'600px'}
                    height={'400px'}
                    ref={player}
                    url= {url}
                    playing = {playing} 
                    muted = {muted}
                    controls = {true} // 기본 control를 띄울 것인지 - 나중에 지울것
                    progressInterval = {100} // onProgress의 텀을 설정한다.
                    onReady={handleReady} // 재생 준비가 완료되면 호출될 함수? 재생 준비 기준이 뭔지
                    onProgress={(e) => {handleProgress(e)}}
                    onEnded={handleEnded}
                />   
            </div>     
        </div>
    )
}

export default MusicPlayer;