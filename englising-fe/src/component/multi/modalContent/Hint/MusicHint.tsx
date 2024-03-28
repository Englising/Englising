import useTimer from "../../../../hooks/useTimer";
import Countdown from "../Countdown";

const MusicHint = ({ speed }: { speed: number }) => {
  const [, time] = useTimer(3, false);
  return (
    <>
      <p>{speed}배속 듣기</p>
      <p>
        {time}초 후 음악이 {speed}배속으로 재생됩니다.
      </p>
      <Countdown time={3} />
    </>
  );
};

export default MusicHint;
