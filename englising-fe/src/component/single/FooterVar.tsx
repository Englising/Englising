import { useState } from "react";

interface Props {
    onSetAnswer(answer: string): void;
}

const FooterVar = ({onSetAnswer}:Props) => {
    const [answer, setAnswer] = useState<string>("");

    const handleAnswerChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setAnswer(e.target.value);
    }

    const handleAnswerSubmit = () => {
        onSetAnswer(answer);
        setAnswer("");
    }

    return (
        <div>
            <input 
            className="border border-black"
            placeholder="답안"
            value={answer}
            onChange={(event) => {handleAnswerChange(event)}}>
            </input>
            <div 
            className="border border-black rounded-md my-4  w-20 text-center bg-primary-950 text-secondary-300 text-xl cursor-pointer"
            onClick={handleAnswerSubmit}> 
            입력
            </div> 
        </div>
    )
} 
export default FooterVar;