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
    const [volume, setVolume] = useState<number>(0.5);
    const [played, setPlayed] = useState<number>(0.0); // 재생중인 구간의 비율(ratio)
    const [playedSeconds, setPlayedSeconds] = useState<number>(0); // 재생중인 구간의 시간(s)
    const [endedSeconds, setEndedSeconds] = useState<number>(0); // 음원 전체 시간(s)

    const player = useRef<ReactPlayer | null>(null);
    const percentage = (rightWord / totalWord) * 100;
  
    const handleReady = () => {
        setMuted(false);
    }
    
    const handleProgress = (e: OnProgressProps) => {
        setPlayed(e.played);
        setPlayedSeconds(e.playedSeconds);
        if(timeData[idx+1] < e.playedSeconds-0.1){
            if(!isBlank){
                onSetInfoIdx(idx+1);
            }else {
                setPlaying(false);
            }
        }
    }

    const handleDuration = (e: number) => {
        setEndedSeconds(e);
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
                    width={'60%'}
                    height={'50%'}
                    ref={player}
                    url= {url}
                    playing = {playing} // 자동재생
                    muted={muted} // 시작할 때 mute 여부
                    volume={volume}
                    loop={true} // 노래가 끝나면 loop를 돈다.
                    controls = {true} // 기본 control를 띄울 것인지 - 나중에 지울것
                    progressInterval = {100} // onProgress의 텀을 설정한다.
                    onReady={handleReady} // 재생 준비가 완료되면 호출될 함수? 재생 준비 기준이 뭔지
                    onProgress={(e) => { handleProgress(e) }}
                    onDuration={handleDuration}
                />
                <div className="bg-white w-[55%] h-[55%]">
                </div>

                <div className="w-[55%]">
                    {/* 음원 Volume Bar */ }
                    <div className="flex items-center justify-center my-2">
                        <input type="range" value={volume} min={0} max={1} step="any"
                            className="w-full h-2.5 bg-gray-200 dark:bg-gray-700 rounded-full appearance-none cursor-pointer"
                            onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                                setVolume(parseFloat(e.target.value));
                            }}
                        />
                    </div>
   
                    {/* 음원 Progress Bar */ }
                    <div className="flex items-center justify-center my-1">
                        <div className="w-full h-2.5 bg-gray-200 dark:bg-gray-700 rounded-full">
                            <div style={{width: `${played*100}%`}} className= "h-2.5  bg-secondary-500 rounded-full"></div>
                        </div>
                    </div>

                    {/* 음원 Progress Time */ }
                    <div className="flex items-center justify-between m-1">
                        <span className="text-white">{`${~~(playedSeconds / 60)}:${String(~~(playedSeconds % 60)).padStart(2, '0')}`}</span>
                        <span className="text-white">{`${~~(endedSeconds/60)}:${String(~~(endedSeconds%60)).padStart(2,'0')}`}</span>
                    </div>
                </div>
            </div>

            <div className="w-full h-2/5 flex flex-col items-center box-border ">
                 <div className="w-full h-3/5 flex flex-row justify-center items-center">
                            <div className=" flex text-[1em] text-white w-24">
                                정답
                            </div>
                        <div className="h-20 w-24 pl-18">
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
                
                <div className="flex flex-row items-center">
                        <div className="w-20 text-white text text-[1em]">
                                힌트
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
    )
}

export default MusicPlayer;