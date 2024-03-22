import ReactPlayer from "react-player";
import { useEffect, useRef, useState } from "react";
import { PlayInfo, ProgressInfo } from "../../pages/SinglePage.tsx";
import { OnProgressProps } from "react-player/base";
import { CircularProgressbar, buildStyles} from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";

interface Props {
    onSetInfoIdx(currIdx: number):void,
    playInfo: PlayInfo,
    progressInfo: ProgressInfo,
    
}
//임시 데이터: 음원의 id를 보내주면, 문장마다 시작하는 시간 리스트를 받아오는 코드로 대체
const timeData = [7.5, 12.0, 15.9, 20.0, 24.3, 28.3, 32.4, 36.4, 41.4, 49.7, 56.4, 60.6, 65.0, 68.8, 90.3, 98.4];
const hintStyle = "w-[1.5em] h-[1.5em] mx-[0.4em] rounded-full"

const MusicPlayer = ({onSetInfoIdx, playInfo, progressInfo}:Props ) => {
    // 현재 재생중인 가사 정보
    let { idx, isBlank, startTime, toggleNext } = playInfo
    
    // 문제 진행도, 힌트
    let { totalWord, rightWord, hintNum } = progressInfo

    // 유튜브 아이디를 주는 axios 넣기//
    const url = "https://www.youtube.com/watch?v=EVJjmMW7eII";
    const [playing, setPlaying] = useState<boolean>(true);
    const [muted, setMuted] = useState<boolean>(true);
    const player = useRef<ReactPlayer | null>(null);
    const percentage = (rightWord / totalWord) * 100;
  
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

    // 힌트창이 켜졌을때 노래 일시중지

    return(
        <div className="w-full h-full flex flex-col items-center">
            <div className="w-full h-3/5 flex flex-col items-center justify-center">
                <div className="text-[1.25em] my-[1em] text-white text-center">비비-밤양갱</div>
                <ReactPlayer
                    width={'75%'}
                    height={'75%'}
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
            <div className="w-full h-2/5 flex flex-col items-center box-border py-[4em]">
                <div className="w-1/2 h-3/5 flex flex-col justify-center">
                    <div className="my-[1.5em] flex items-center">
                        <div className="w-[4em] h-[2em] flex items-center text-white">
                            <div className="text-[1em]">
                                정답
                            </div>
                        </div>
                        <div className="w-[7em] h-[7em]">
                            <CircularProgressbar
                                value={(percentage == 0) ? 0.5 : percentage}
                                
                                text={`${rightWord}/${totalWord}`}
                                strokeWidth={10} // 진행 바의 폭 조절

                                styles={buildStyles({
                                    // 게이지의 처음 위치
                                    rotation: 0,

                                    // 진행바 모서리 모양 'butt' or 'round'
                                    strokeLinecap: 'round',

                                    // 텍스트 사이즈
                                    textSize: '1em',

                                    // percent 게이지 차는 속도
                                    pathTransitionDuration: 1,

                                    // 색상
                                    pathColor: `rgba(0, 255, 255, ${(percentage==0) ? 10 : percentage})`,
                                    textColor: '#ffffff',
                                    trailColor: '#d6d6d6', 
                                    backgroundColor: '#00ffff',
                                })}
                            />
                        </div>
                    </div>
                    <div className="my-[1.5em] flex items-center">
                        <div className="w-[4em] h-[2em] flex items-center text-white">
                            <div className="text-[1em]">
                                힌트
                            </div>
                        </div>
                        <div className={(hintNum >= 1) ? `${hintStyle} + bg-secondary-500` : `${hintStyle} + bg-white`}>
                        </div>
                        <div className={(hintNum >= 2) ? `${hintStyle} + bg-secondary-500` : `${hintStyle} + bg-white`}>
                        </div>
                        <div className={(hintNum >= 3) ? `${hintStyle} + bg-secondary-500` : `${hintStyle} + bg-white`}>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MusicPlayer;