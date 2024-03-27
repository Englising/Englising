import useTimer from "../../../hooks/useTimer";

type CountdownProps = {
  time: number;
  classes?: string;
};

const Countdown = ({ time, classes }: CountdownProps) => {
  const [, remainingTime] = useTimer(time, true);

  return <p className={`${classes}`}>{remainingTime}</p>;
};

export default Countdown;
