import Countdown from "./Countdown";

function Fail({ time }: { time: number }) {
  return (
    <>
      <div className="py-10 text-center font-bold">
        <p className="mb-8 text-secondary-400 text-5xl">실패</p>
        <p className="mb-2 text-xl">정답이 아닙니다.</p>
        <p className="mb-12 text-xl">잠시 후, 다음 라운드가 시작됩니다.</p>
        <Countdown time={time} classes={"text-secondary-400 font-extrabold text-7xl"} />
      </div>
    </>
  );
}

export default Fail;
