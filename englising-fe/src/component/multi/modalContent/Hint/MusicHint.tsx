import Countdown from "../Countdown";

const MusicHint = ({ speed }: { speed: number }) => {
  return (
    <>
      <p className="text-2xl font-bold">{speed}배속 듣기</p>
      <p className="text-lg">3초 후 음악이 {speed}배속으로 재생됩니다.</p>
      <Countdown time={3} classes={"mt-6 text-secondary-400 font-extrabold text-7xl"} />
    </>
  );
};

export default MusicHint;
