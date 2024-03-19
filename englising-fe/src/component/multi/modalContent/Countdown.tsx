import useTimer from "../../../hooks/useTimer";

type CountdownProps = {
  time: number;
  classes: string;
};

function Countdown({ time, classes }: CountdownProps) {
  console.log(time);
  const [val, remainingTime] = useTimer(time, true);

  return <p className={`${classes}`}>{remainingTime}</p>;
}

export default Countdown;
