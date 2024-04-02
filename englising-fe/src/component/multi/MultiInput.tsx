import { ChangeEvent, useEffect, useRef, useState } from "react";
import Tooltip from "./Tooltip";
import { Alphabet } from "./MultiInputArea";
import styles from "./Multi.module.css";

type InputProps = {
  answer: Alphabet;
  index: number;
  changedAnswer?: Alphabet | undefined;
  hintResult?: Alphabet[] | number;
  onInputChange: (val: Alphabet) => void;
  isOpen: boolean;
};

function MultiInput({ answer, index, changedAnswer, hintResult, onInputChange, isOpen }: InputProps) {
  const inputRef = useRef<HTMLInputElement>(null);
  const [isEmpty, setIsEmpty] = useState(true);
  const [tooltipOpen, setTooltipOpen] = useState(false);

  function isHangeul(char: string) {
    const reg = /[^0-9a-zA-Z]/g;
    return reg.test(char);
  }

  function handleInputChange(e: ChangeEvent<HTMLInputElement>) {
    if (!inputRef.current) return;

    if (isHangeul(e.target.value)) {
      setTooltipOpen(true);

      if (e.target.value.length > 1) {
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

    if (onInputChange) {
      onInputChange({ alphabetIndex: answer.alphabetIndex, alphabet: inputRef.current.value });
    }

    if (e.target.value.length > 0) {
      setIsEmpty(false);
    } else {
      setIsEmpty(true);
    }
  }

  useEffect(() => {
    if (hintResult && typeof hintResult != "number") {
      if (!inputRef.current) return;

      for (const hint of hintResult) {
        if (answer.alphabetIndex == hint.alphabetIndex) {
          inputRef.current.value = answer.alphabet;
          inputRef.current?.classList.remove("bg-secondary-100");
          inputRef.current?.classList.add("bg-purple-200");
          break;
        }
      }
    }
  }, [hintResult]);

  useEffect(() => {
    if (inputRef.current && changedAnswer && index === changedAnswer.alphabetIndex) {
      inputRef.current.value = changedAnswer.alphabet;
      if (inputRef.current.value.length > 0) {
        inputRef.current.classList.remove("bg-gray-500");
        inputRef.current.classList.add("bg-secondary-100");
      } else {
        inputRef.current.classList.remove("bg-secondary-100");
        inputRef.current.classList.add("bg-gray-500");
      }
    }
  }, [changedAnswer]);

  return (
    <>
      <div className={`flex flex-col justify-center ${tooltipOpen ? "relative" : ""}`}>
        <label className="min-h-5 text-center text-sm">{answer.alphabetIndex > 0 ? answer.alphabetIndex : ""}</label>
        {answer.alphabetIndex <= 0 ? (
          <input
            type="text"
            className={`w-3 py-2 bg-transparent font-bold text-xl text-center rounded-lg focus:outline-none ${styles.symbol}`}
            readOnly
            value={answer.alphabet}
          />
        ) : (
          <input
            type="text"
            className={`w-9 p-2 font-bold text-xl text-black text-center rounded-lg focus:outline-none ${isEmpty ? "bg-gray-500" : "bg-secondary-100"} `}
            onChange={handleInputChange}
            readOnly={isOpen}
            ref={inputRef}
          />
        )}
        {tooltipOpen && <Tooltip>영어, 숫자만 입력할 수 있습니다</Tooltip>}
      </div>
    </>
  );
}

export default MultiInput;
