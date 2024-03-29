import { useEffect, useRef } from "react";

function HintRoulette({ hint, onRoulette }: { hint: number; onRoulette: () => void }) {
  const backgroundRef = useRef<HTMLDivElement>(null);
  console.log("hint num:", hint);

  useEffect(() => {
    const width = Number(backgroundRef.current?.offsetWidth);
    const height = Number(backgroundRef.current?.offsetHeight);
    const deltaWidth = [0, width, width, 0];
    const deltaHeight = [0, 0, height, height];
    let idx = 0;
    let cnt = 0;
    const interval = setInterval(() => {
      if (backgroundRef.current) {
        backgroundRef.current.style.transform = `translate(${deltaWidth[idx]}px, ${deltaHeight[idx]}px)`;
      }
      idx++;

      if (cnt == 5 && idx == hint) {
        clearInterval(interval);

        setTimeout(() => {
          onRoulette();
        }, 1000);
      }

      if (idx == 4) {
        idx = 0;
        cnt++;
      }
    }, 70);
  }, []);

  return (
    <>
      <div className="relative pt-6 pb-12 px-16 min-w-[45rem]">
        <div className="absolute bottom-4 right-4 flex items-center gap-1 text-sm text-white/50">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth="1.5"
            stroke="currentColor"
            className="w-4 h-4"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="m11.25 11.25.041-.02a.75.75 0 0 1 1.063.852l-.708 2.836a.75.75 0 0 0 1.063.853l.041-.021M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9-3.75h.008v.008H12V8.25Z"
            />
          </svg>
          <p>4개의 힌트 중 하나가 랜덤으로 제공됩니다.</p>
        </div>
        <p className="text-2xl font-bold text-center text-secondary-400">HINT</p>
        <div className="relative grid grid-cols-2 mx-6 my-4">
          <div className="absolute -z-10 bg-secondary-500/20 w-1/2 h-1/2" ref={backgroundRef}></div>
          <div className="min-h-16 flex flex-col items-center gap-4 p-4 border-e border-b">
            <p className="text-lg font-bold">0.7배속 듣기</p>
            <p className="text-secondary-400 text-6xl font-bold">× 0.7</p>
            <p className="text-sm">노래가 0.7배속으로 천천히 재생됩니다</p>
          </div>
          <div className="min-h-16 flex flex-col items-center gap-4 p-4 border-b">
            <p className="text-lg font-bold">2배속 듣기</p>
            <p className="text-secondary-400 text-6xl font-bold">× 2</p>
            <p className="text-sm">노래가 2배속으로 빠르게 재생됩니다</p>
          </div>
          <div className="min-h-16 flex flex-col items-center gap-4 p-4 border-e">
            <p className="text-lg font-bold">랜덤 위치 5개 공개</p>
            <div className="flex gap-4 py-2 text-secondary-400 text-lg font-bold">
              <p className="py-2 px-3 bg-gray-500 rounded-sm">A</p>
              <p className="py-2 px-3 bg-gray-500 rounded-sm">B</p>
              <p className="py-2 px-3 bg-gray-500 rounded-sm">C</p>
              <p className="py-2 px-3 bg-gray-500 rounded-sm">D</p>
              <p className="py-2 px-3 bg-gray-500 rounded-sm">E</p>
            </div>
            <p className="text-sm">랜덤으로 5개 칸의 정답이 공개됩니다</p>
          </div>
          <div className="min-h-16 flex flex-col items-center justify-between p-4">
            <p className="text-lg font-bold">오답 수</p>
            <div className="text-secondary-400 text-5xl font-bold">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth="2"
                stroke="currentColor"
                className="w-16 h-16"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M18.364 18.364A9 9 0 0 0 5.636 5.636m12.728 12.728A9 9 0 0 1 5.636 5.636m12.728 12.728L5.636 5.636"
                />
              </svg>
            </div>
            <p className="text-sm">이전 라운드에 제출한 답변의 오답 수가 공개됩니다</p>
          </div>
        </div>
      </div>
    </>
  );
}

export default HintRoulette;
