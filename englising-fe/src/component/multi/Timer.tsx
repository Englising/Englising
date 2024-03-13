import { forwardRef, useEffect, useState } from "react";
import styles from "./Multi.module.css";

type TimerProps = {
  roundTime: number;
};

const RADIUS = Math.PI * 2 * 50;

const Timer = forwardRef(function Timer({ roundTime }: TimerProps, ref) {
  const [time, setTime] = useState(roundTime);

  function calculateTime(time: number) {
    const min = Math.floor(time / 60);
    const sec = Math.floor(time - 60 * min);

    return min + " : " + String(sec).padStart(2, "0");
  }

  useEffect(() => {
    const interval = setInterval(() => {
      setTime((prev) => {
        return prev - 0.5;
      });
    }, 1000);

    setTimeout(() => {
      clearInterval(interval);
      ref.current?.showModal();
      console.log("모달 오픈");
    }, roundTime * 1000);
  }, []);

  return (
    <div className="relatvie w-20 h-20 mt-4 flex justify-center items-center border-4 border-white rounded-full">
      <svg width={"4rem"} height={"4rem"} viewBox="0 0 100 100">
        <circle cx="50" cy="50" r="25" fill="transparent" stroke="white" strokeWidth="50" />
      </svg>
      <svg width={"4.125rem"} height={"4.125rem"} className="absolute" viewBox="0 0 100 100">
        <circle
          cx="50"
          cy="50"
          r="25"
          fill="transparent"
          stroke="rgb(31 41 55)"
          strokeWidth="50"
          strokeDasharray={`${(RADIUS - (time / roundTime) * RADIUS) / 2} 1000`}
          transform="rotate(-90) translate(-100 0)"
        />
      </svg>
      <p className={`absolute text-xl font-bold text-secondary-500 drop-shadow-md z-10`}>{calculateTime(time)}</p>
      <p className={`absolute text-xl font-bold text-secondary-500 drop-shadow-md ${styles["text-shadow"]}`}>
        {calculateTime(time)}
      </p>
    </div>
  );
});

export default Timer;
