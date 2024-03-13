import { useRef } from "react";

function MemoArea() {
  const textareaRef = useRef(null);

  const handleResizTextareaHeight = () => {
    textareaRef.current.style.height = "auto"; //height 초기화
    textareaRef.current.style.height = textareaRef.current.scrollHeight + "px";
  };

  return (
    <div className="bg-primary-400 mt-4 py-2 px-4 rounded-lg">
      <p className="text-secondary-500 font-bold text-center">MEMO</p>
      <textarea
        className="w-full max-h-24 mt-2 bg-transparent resize-none overflow-hidden text-lg placeholder:text-gray-300 placeholder:text-center focus:outline-none"
        placeholder="간단하게 메모해보세요!"
        ref={textareaRef}
        onChange={handleResizTextareaHeight}
      ></textarea>
    </div>
  );
}

export default MemoArea;
