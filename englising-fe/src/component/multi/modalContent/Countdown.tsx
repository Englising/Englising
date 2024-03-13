import { useEffect, useState } from "react";

type CountdownProps = {
  time: number;
  classes: string;
};

function Countdown({ time, classes }: CountdownProps) {
  const [remainingTime, setRemainingTime] = useState(time + 1);

  useEffect(() => {
    const interval = setInterval(() => {
      setRemainingTime((prev) => prev - 1);
    }, 1000);

    const timer = setTimeout(() => {
      clearInterval(interval);
    }, time * 1000);

    return () => {
      clearInterval(interval);
      clearTimeout(timer);
    };
  }, []);

  return <p className={`${classes}`}>{remainingTime}</p>;
}

export default Countdown;
