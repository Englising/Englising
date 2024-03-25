import useTimer from "../../../hooks/useTimer";

type CountdownProps = {
  time: number | undefined;
  classes?: string;
};

const Countdown = ({ time, classes }: CountdownProps) => {
  const [val, remainingTime] = useTimer(time, true);

  return <p className={`${classes}`}>{remainingTime}</p>;
};

export default Countdown;
