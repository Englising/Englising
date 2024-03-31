import ReactPlayer from "react-player";
import { useEffect, useRef, useState } from "react";
import { PlayInfo, ProgressInfo } from "../../pages/SinglePage.tsx";
import { OnProgressProps } from "react-player/base";
import { CircularProgressbar, buildStyles} from "react-circular-progressbar";
import { useLocation, useParams } from "react-router-dom";
import "react-circular-progressbar/dist/styles.css";
import { getStartimeData } from "../../util/SinglePlayAPI.tsx";

interface Props {
    onSetInfoIdx(currIdx: number):void,
    playInfo: PlayInfo,
    progressInfo: ProgressInfo,
    showStartModal: boolean,
    togglePlayerControl: number
}

const hintStyle = "w-[1.5em] h-[1.5em] mx-[0.4em] rounded-full"

const MusicPlayer = ({onSetInfoIdx, playInfo, progressInfo, showStartModal, togglePlayerControl}:Props ) => {
    // 현재 재생중인 가사 정보
    let { idx, isBlank, startTime, toggleNext } = playInfo
    
    // 문제 진행도, 힌트
    let { totalWord, rightWord, hintNum } = progressInfo
    
    const { trackId, youtubeId } = useParams<{
        trackId: string,
        youtubeId: string
    }>();

    // 유튜브 아이디를 주는 axios 넣기//
    const url = `https://www.youtube.com/watch?v=${youtubeId}`;
    const [playing, setPlaying] = useState<boolean>(false);
    const [volume, setVolume] = useState<number>(0.5);
    const [played, setPlayed] = useState<number>(0.0); // 재생중인 구간의 비율(ratio)
    const [playedSeconds, setPlayedSeconds] = useState<number>(0); // 재생중인 구간의 시간(s)
    const [endedSeconds, setEndedSeconds] = useState<number>(0); // 음원 전체 시간(s)
    const timeData = useRef<number[]>([]);


    {/** 프로필 */}
    const { state } = useLocation();
    const { title, img, artists } = state;

    const player = useRef<ReactPlayer | null>(null);

    {/** 진행률 */}
    const [percentage, setPercentage] = useState<number>(0);

    {/** playButton */}
    const [togglePlayButton, setTogglePlayButton] = useState<boolean>(true);

    
    const handleProgress = (e: OnProgressProps) => {
        setPlayed(e.played);
        setPlayedSeconds(e.playedSeconds);
        if(timeData.current[idx+1] < e.playedSeconds+0.5){
            if(!isBlank){
                onSetInfoIdx(idx+1);
            }else {
                setPlaying(false);
            }
        }
    }
    const handlePlay = () => {
        if (!playing) setPlaying(true);
        setTogglePlayButton(false);
    }

    const handlePuase = () => {
        if (playing) setPlaying(false);
        setTogglePlayButton(true);
    }
    
    const handleError = () => {
        if(playing) setPlaying(false)
        setTogglePlayButton(true);
    }
    
    {/** 전체 playTime 받아오는 이벤트함수 */}
    const handleDuration = (e: number) => {
        setEndedSeconds(e);
    }

    {/** 재생 / 일시정지 */ }
    const handlePlayClick = () => {
        setTogglePlayButton(false);
        setPlaying(true);
        
        if (isBlank) {
            if(timeData.current[idx+1] < playedSeconds+0.5){
            onSetInfoIdx(idx + 1);
            }      
        }
    }

    const handlePauseClick = () => {
        setTogglePlayButton(true);
        setPlaying(false);
    }

    {/** 음소거 on off*/ }
    const handleSoundClick = () => {
        setVolume(0.5);
    }

    const handleMuteClick = () => {
        setVolume(0);
    }

     //
    useEffect(() => {
        const getData = async () => {
            if (trackId != undefined) {
                const startTimeData = await getStartimeData(parseInt(trackId));
                timeData.current = startTimeData.data.startTime;
            }
        }
        getData();
    }, [])

    useEffect(() => {
        if (showStartModal) return;
        setPlaying(true);
        onSetInfoIdx(0);
    },[showStartModal])
    
    // 특정 구간 가사를 누를 때 발생하는 이벤트
    useEffect(() => {
        if (showStartModal) return;
        setPlaying(true);
        player.current?.seekTo(startTime);
    }, [toggleNext])
    
    useEffect(() => {
        if (playing) setTogglePlayButton(false);
        else setTogglePlayButton(true);
    }, [playing])
    
    useEffect(() => {
        setPercentage((rightWord / totalWord) * 100);
    },[progressInfo])
    
    useEffect(() => {
        if (showStartModal == true) return;
        if (togglePlayButton) {
            handlePlayClick();
        } else {
            handlePauseClick();
        }
    },[togglePlayerControl])
    return(
        <div className="w-full h-full flex flex-col items-center">
            <div className="w-full h-3/5 flex flex-col items-center justify-center">
                <div className="text-[1.25em] text-white text-center">{title}</div>
                <div className="text-[1em] mb-[1em] text-white text-center">{artists}</div>
                <div className="hidden">
                    <ReactPlayer
                    ref={player}
                    url= {url}
                    playing = {playing} // 자동재생
                    volume={volume} // volume
                    loop={true} // 노래가 끝나면 loop를 돈다.
                    controls = {true} // 기본 control를 띄울 것인지 - 나중에 지울것
                    progressInterval = {100} // onProgress의 텀을 설정한다.
                    onProgress={(e) => { handleProgress(e) }}
                    onPlay={handlePlay}
                    onPause={handlePuase}
                    onDuration={handleDuration}
                    onError={handleError}
                    />
                </div>
                <div className="w-[55%] h-[55%] relative">
                    <div className="absolute inset-0">
                        <img src={img} alt={title} className="w-full h-full object-cover rounded-xl" />
                    </div>
                </div>
                <div className="w-[55%]">

                    {/* 음원 Volume Bar */ }
                    <div className="flex items-center justify-center my-2">
                        <div className="w-[10%]">
                            {volume != 0 ?
                                <img className="h-4 mr-2 cursor-pointer" src="https://englising-bucket.s3.ap-northeast-2.amazonaws.com/volume.png" onClick={handleMuteClick}></img> :
                                <img className="h-4 mr-2" src="https://englising-bucket.s3.ap-northeast-2.amazonaws.com/mute.png" onClick={handleSoundClick}></img>
                            }
                        </div>
                        <input type="range" value={volume} min={0} max={1} step="any"
                            className="w-[90%] h-2.5 bg-gray-200 dark:bg-gray-700 rounded-full appearance-none cursor-pointer"
                            onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                                setVolume(parseFloat(e.target.value));
                            }}
                        />
                    </div>
   
                    {/* 음원 Progress Bar */ }
                    <div className="flex items-center justify-center my-1">
                        <div className="w-[10%]">
                            {togglePlayButton ? 
                                <img className="h-3.5 mr-2 cursor-pointer" src="https://englising-bucket.s3.ap-northeast-2.amazonaws.com/play.png" onClick={handlePlayClick}></img> :
                                <img className="h-3.5 mr-2 cursor-pointer" src="https://englising-bucket.s3.ap-northeast-2.amazonaws.com/pause.png" onClick={handlePauseClick}></img>
                            }
                        </div>
                        <div className="w-[90%] h-2.5 bg-gray-200 dark:bg-gray-700 rounded-md">
                            <div style={{width: `${played*100}%`}} className= "w-full h-full  bg-secondary-500 rounded-md"></div>
                        </div>
                    </div>

                    {/* 음원 Progress Time */}
                    <div className="w-full flex justify-end">
                        <div className="w-[90%] flex items-center justify-between m-1">
                            <span className="text-white">{`${~~(playedSeconds / 60)}:${String(~~(playedSeconds % 60)).padStart(2, '0')}`}</span>
                            <span className="text-white">{`${~~(endedSeconds/60)}:${String(~~(endedSeconds%60)).padStart(2,'0')}`}</span>
                        </div>
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