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
        <div className="flex items-center justify-center ">
            <input 
            className="h-16  w-96 mr-16 rounded-md focus:outline-none border-slate-300 placeholder-slate-400 focus:border-sky-500 focus:ring-sky-500"
            placeholder="답안"
            value={answer}
            onChange={(event) => {handleAnswerChange(event)}}
            onKeyDown={(event) => {handlePressEnter(event)}}
            >
            </input>
            <div 
            className="h-16 w-28 text-center rounded-md md bg-secondary-500 text-black text-xl font-bold cursor-pointer hover:opacity-50 flex flex-col"
            onClick={handleAnswerSubmit}> 
            <div className="m-auto text-2xl ">입력</div>
            </div> 
        </div>
    )
} 
export default FooterVar;