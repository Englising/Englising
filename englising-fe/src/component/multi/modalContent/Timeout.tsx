import { ReactNode } from "react";
import Countdown from "./Countdown";

type ModalContentProps = {
  time: number;
  children: ReactNode;
};

function Timeout({ time, children }: ModalContentProps) {
  console.log("round time", time);
  return (
    <>
      <div className="min-w-[45rem] px-16 py-16 text-center">
        {children}
        {time > 0 && <Countdown time={time} classes={"text-secondary-400 font-extrabold text-7xl"} />}
      </div>
    </>
  );
}

export default Timeout;
