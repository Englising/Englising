import { useEffect, useState, useRef } from "react";
import ReactPlayer from "react-player";
import { OnProgressProps } from "react-player/base";
import { PlayInfo } from "../../pages/MultiplayPage";

interface Props {
  playInfo: PlayInfo;
}

const MultiMusicPlayer = ({ playInfo }: Props) => {
  const { url, startTime, endTime, speed, onPlay } = playInfo;
  const [playing, setPlaying] = useState<boolean>(true);
  const [muted, setMuted] = useState<boolean>(true);
  const player = useRef<ReactPlayer | null>(null);

  useEffect(() => {
    if (startTime == -1) return;
    player.current?.seekTo(startTime);
    setMuted(false);
    setPlaying(true);
  }, [onPlay]);

  const handleProgress = (e: OnProgressProps) => {
    if (e.playedSeconds > endTime) {
      setMuted(true);
      setPlaying(false);
    }
  };

  return (
    <>
      <div>
        <ReactPlayer
          url={url}
          ref={player}
          playing={playing} // 재생여부
          muted={muted}
          loop={true}
          controls={true}
          playbackRate={speed}
          progressInterval={100}
          onProgress={(e) => handleProgress(e)}
        />
      </div>
    </>
  );
};
export default MultiMusicPlayer;
