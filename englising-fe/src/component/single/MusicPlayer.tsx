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
    const {idx, startTime, endTime, toggle} = playInfo


    const [url, setUrl] = useState<string>("https://www.youtube.com/watch?v=EVJjmMW7eII");
    const [playing, setPlaying] = useState<boolean>(true);
    const [muted, setMuted] = useState<boolean>(true);
    const player = useRef<ReactPlayer | null>(null);

    // onProgress 속성 테스트 -> 재생후 1초 단위로 현재 재생중인 시간대 리턴
    // 개선 방향 -> 어차피 끝나는 endTIme을 주니까 그걸 기준으로 재생하고 그다음 진헹 (await 쓰자)
    const [test, setTest] = useState<number>(0);

    const handleReady = () => {
        setMuted(false);
    }

    const handleProgress = (e: OnProgressProps) => {
        // 속성: onProgress={(event) => {handleProgress(event)}}
        // 현재 재생중인 위치(비율/시간), 현재 로드된 위치(비율/시간)
        // loaded 는 브라우저에 로드된 부분 (유튜브 흰색게이지)
        /* console.log("played", e.played); 
         * console.log("loaded", e.loaded); 
         * console.log("playedSeconds", e.playedSeconds);
         * console.log("loadedSeconds", e.loadedSeconds);
         */

        //onChange로 테스트 진행하기
        setTest(e.playedSeconds);
        if(timeData[idx+1] < e.playedSeconds+0.5){
            setPlaying(false);
        }
    }

    // 특정 구간 가사를 누를 때 발생하는 이벤트
    useEffect(() => {
        setPlaying(true);
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
            <div >{test}</div> 
        </div>
    )
}

export default MusicPlayer;