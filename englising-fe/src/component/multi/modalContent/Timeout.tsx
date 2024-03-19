import Countdown from "./Countdown";

function Timeout({ time }: { time: number }) {
  return (
    <>
      <div className="min-w-[45rem] px-16 py-16 text-center">
        <p className="text-secondary-400 font-bold text-3xl">답변 제출 시간이 종료되었습니다!</p>
        <p className="mt-6 mb-12 font-bold text-xl">잠시 후, 이번 라운드의 결과가 공개됩니다.</p>
        <Countdown time={time} classes={"text-secondary-400 font-extrabold text-7xl"} />
      </div>
    </>
  );
}

export default Timeout;
