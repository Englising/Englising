import { useEffect, useState, useRef } from "react";
import ReactPlayer from "react-player";
import { OnProgressProps } from "react-player/base";
import { PlayInfo } from "../../pages/MultiplayPage";

interface Props {
  playInfo: PlayInfo;
}

const MultiMusicPlayer = ({ playInfo }: Props) => {
  const { url, startTime, endTime, speed, onPlay } = playInfo;
  const [playing, setPlaying] = useState<boolean>(false);
  const player = useRef<ReactPlayer | null>(null);

  useEffect(() => {
    if (startTime == -1) return;
    player.current?.seekTo(startTime);
    if (!playing) setPlaying(true);
  }, [onPlay]);

  const handleProgress = (e: OnProgressProps) => {
    if (e.playedSeconds > endTime - 0.01) {
      if (playing) setPlaying(false);
    }
  };

  const handleOnPause = () => {
    if (playing) {
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
          controls={true}
          playbackRate={speed}
          progressInterval={100}
          onProgress={(e) => handleProgress(e)}
          onPause={handleOnPause}
        />
      </div>
    </>
  );
};
export default MultiMusicPlayer;
