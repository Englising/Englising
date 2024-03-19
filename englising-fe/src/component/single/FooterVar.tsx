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

    const handlePressEnter = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if(e.key == 'Enter'){
            onSetAnswer(answer);
            setAnswer("");  
        }
    }

    return (
        <div className="h-24 flex justify-center ">
            <input 
            className="h-16"
            placeholder="답안"
            value={answer}
            onChange={(event) => {handleAnswerChange(event)}}
            onKeyDown={(event) => {handlePressEnter(event)}}
            >
            </input>
            <div 
            className="rounded-md h-16 w-20 text-center bg-secondary-500 text-black text-xl cursor-pointer hover:opacity-50"
            onClick={handleAnswerSubmit}> 
            입력
            </div> 
        </div>
    )
} 
export default FooterVar;