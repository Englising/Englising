import { useEffect, useState } from "react";

function useTimer(time: number, modalStatus: boolean) {
  const [openModal, setOpenModal] = useState(modalStatus);
  const [remainingTime, setRemainingTime] = useState(time);

  useEffect(() => {
    const interval = setInterval(() => {
      setRemainingTime((prev) => prev - 1);
    }, 1000);

    setTimeout(() => {
      clearInterval(interval);
      setOpenModal(!modalStatus);
    }, time * 1000);

    return () => clearInterval(interval);
  }, []);

  return [openModal, remainingTime];
}

export default useTimer;
