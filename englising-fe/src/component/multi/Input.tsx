import { ChangeEvent, useRef, useState } from "react";
import Tooltip from "./Tooltip";
import styles from "./Multi.module.css";

type InputProps = {
  answer: string;
  index: number;
};

function Input({ answer, index }: InputProps) {
  const inputRef = useRef<HTMLInputElement>(null);
  const [isEmpty, setIsEmpty] = useState(true);
  const [tooltipOpen, setTooltipOpen] = useState(false);

  function isHangeul(char: string) {
    const reg = /[^0-9a-zA-Z]/g;
    return reg.test(char);
  }

  function handleInputChange(e: ChangeEvent<HTMLInputElement>) {
    if (isHangeul(e.target.value)) {
      setTooltipOpen(true);

      if (e.target.value.length > 1) {
        console.log("??");
        inputRef.current.value = e.target.value.charAt(0);
      } else {
        inputRef.current.value = "";
        setIsEmpty(true);
      }

      setTimeout(() => {
        setTooltipOpen(false);
      }, 3 * 1000);

      return;
    }

    if (e.target.value.length > 1) {
      inputRef.current.value = e.target.value.charAt(1);
    }

    if (e.target.value.length > 0) {
      setIsEmpty(false);
    } else {
      setIsEmpty(true);
    }
  }

  return (
    <>
      <div className={`flex flex-col justify-center ${tooltipOpen ? "relative" : ""}`}>
        <label className="min-h-5 text-center text-sm">{index > 0 ? index : ""}</label>
        {index > 0 ? (
          <input
            type="text"
            className={`w-10 p-2.5 font-bold text-xl text-black text-center rounded-lg focus:outline-none ${isEmpty ? "bg-gray-500" : "bg-secondary-100"}`}
            onChange={handleInputChange}
            ref={inputRef}
          />
        ) : (
          <input
            type="text"
            className={`w-4 py-2.5 bg-transparent font-bold text-xl text-center rounded-lg focus:outline-none ${styles.symbol}`}
            readOnly
            value={answer}
          />
        )}
        {tooltipOpen && <Tooltip>영어, 숫자만 입력할 수 있습니다</Tooltip>}
      </div>
    </>
  );
}

export default Input;
