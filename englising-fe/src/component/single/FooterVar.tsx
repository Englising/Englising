import { useState } from "react";

interface Props {
    onSetAnswer(answer: string): void;
    onSkip(): void
}

const FooterVar = ({onSetAnswer, onSkip}:Props) => {
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

    const handleDictionaryClick = () => {

    }

    const handleVocabularyClick = () => {

    }

    const handleExitClick = () => {

    }
    return (
        <div className="w-full h-full flex items-center justify-evenly ">
            <div className="w-1/3 flex pl-10 box-border text-white">
                <div className="w-20" onClick={handleDictionaryClick}>
                    사전
                </div>
                <div className="mx-10" onClick={handleVocabularyClick}>
                    단어장
                </div>
                <div className="mx-10" onClick={handleExitClick}>
                    종료하기
                </div>
            </div>

            <div className="w-2/3 flex justify-center items-center">
                <div className="h-[3em] mr-[3em] flex items-center p-1 rounded-lg bg-gradient-to-r from-[white] via-[#00ffff] to-[#3F4685] ">
                    <input 
                        className="h-[2.5em] w-[20em] text-[1em] pl-[1.25em] rounded-lg bg-black text-white"
                        placeholder="빈칸에 들어갈 단어를 작성해주세요."
                        value={answer}
                        onChange={(event) => {handleAnswerChange(event)}}
                        onKeyDown={(event) => {handlePressEnter(event)}}
                    >
                    </input>
                </div>
                <div 
                className="h-[2.5em] w-[5em] mr-[1em] text-center rounded-lg bg-secondary-300 text-black font-bold cursor-pointer hover:opacity-50 flex flex-col"
                onClick={handleAnswerSubmit}> 
                    <div className="m-auto text-[1em]">입력</div>
                </div>
                <div 
                    className="h-[2.5em] w-[5em] text-center rounded-lg bg-secondary-300 text-black font-bold cursor-pointer hover:opacity-50 flex flex-col"
                    onClick={onSkip}> 
                    <div className="m-auto text-[1em]">SKIP</div>
                </div>
            </div> 
        </div>
    )
} 
export default FooterVar;