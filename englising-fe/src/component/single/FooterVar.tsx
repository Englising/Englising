import { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

interface Props {
    onSetAnswer(answer: string): void;
    onLyricMove(index: number): void;
    idx: number;
    singlePlayId: number;
}

const FooterVar = ({ onSetAnswer, onLyricMove, idx, singlePlayId }: Props) => {
    const { state } = useLocation();
    
    const navigate = useNavigate();

    const [answer, setAnswer] = useState<string>("");
    const inputRef = useRef<HTMLInputElement | null>(null);

    const handleAnswerChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setAnswer(e.target.value);
    }

    const handleAnswerSubmit = () => {
        onSetAnswer(answer);
        setAnswer("");
    }
    
    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if(e.key == 'Enter'){
            onSetAnswer(answer);
            setAnswer("");  
        } else if (e.key == 'ArrowUp') {
            onLyricMove(idx - 1);
        } else if (e.key == 'ArrowDown') {
            onLyricMove(idx + 1);
        }
    }


    const handleExitClick = () => {
        navigate(`/SinglePlay/result/${singlePlayId}`, { state: { ...state } } );
    }

    useEffect(() => {
        inputRef.current?.focus();
    }, [idx])
    
    return (
        <div className="w-full h-full flex items-center justify-evenly ">
            <div className="w-1/3 flex pl-10 box-border text-white">
                <div className="h-[2.5em] w-[5em] mr-[1em] rounded-lg bg-secondary-300 text-black font-bold cursor-pointer hover:opacity-50 flex flex-col justify-center items-center" onClick={handleExitClick}>
                    <div> 종료하기 </div>
                </div>
            </div>

            <div className="w-2/3 flex justify-center items-center">
                <div className="h-[3em] mr-[3em] flex items-center p-1 rounded-lg bg-gradient-to-r from-[white] via-[#00ffff] to-[#3F4685] ">
                    <input 
                        ref={inputRef}
                        className="h-[2.5em] w-[20em] text-[1em] pl-[1.25em] rounded-lg bg-black text-white"
                        placeholder="빈칸에 들어갈 단어를 작성해주세요."
                        value={answer}
                        onChange={(event) => {handleAnswerChange(event)}}
                        onKeyDown={(event) => {handleKeyDown(event)}}
                    >
                    </input>
                </div>
                <div 
                className="h-[2.5em] w-[5em] mr-[1em] rounded-lg bg-secondary-300 text-black font-bold cursor-pointer hover:opacity-50 flex flex-col"
                onClick={handleAnswerSubmit}> 
                    <div className="m-auto text-[1em]">입력</div>
                </div>
                <div 
                    className="h-[2.5em] w-[5em] rounded-lg bg-secondary-300 text-black font-bold cursor-pointer hover:opacity-50 flex flex-col"> 
                    <div className="m-auto text-[1em]">공사중</div>
                </div>
            </div> 
        </div>
    )
} 
export default FooterVar;