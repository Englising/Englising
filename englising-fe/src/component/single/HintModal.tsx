
interface Props {
    hintWord: string,
    hintNum: number,
    onUse(hintword: string): void,
    onCancel(): void
}

const HintModal = ({hintWord, hintNum, onUse, onCancel}: Props) => {

    return (
        <div className="fixed inset-0 flex items-center justify-center">
            <div className="w-1/5 h-1/4 text-white bg-black flex justify-center items-center absolut rounded-lg shadow-xl">
                {hintNum > 0 ?
                    (<div className="w-full h-full flex flex-col justify-center">
                        <div className="text-[1.2em]">힌트를 사용하시겠습니까?</div>
                        <div className="my-3 text-[1.2em]">{hintNum} / 3</div> 
                        <div className="flex justify-center">
                            <div className="h-[2.2em] w-[4em] m-1 rounded-lg bg-secondary-300 text-black font-bold cursor-pointer hover:opacity-50 flex flex-col" onClick={() => onUse(hintWord)}>
                                <div className="m-auto">사용</div>
                            </div>
                            <div className="h-[2.2em] w-[4em] m-1 rounded-lg bg-secondary-300 text-black font-bold cursor-pointer hover:opacity-50 flex flex-col" onClick={onCancel}>
                                <div className="m-auto">취소</div>
                            </div>
                        </div>
                    </div>):
                    (<div className="w-full h-full flex flex-col justify-center items-center">
                        <div className="mb-5 text-[1.2em]">힌트를 모두 소진하셨습니다.</div>
                        <div className="h-[2.5em] w-[5em] m-1 rounded-lg bg-secondary-300 text-black font-bold cursor-pointer hover:opacity-50 flex flex-col" onClick={onCancel}>
                            <div className="m-auto">확인</div>
                        </div>
                    </div>
                    )
                }
            </div>
        </div>
    );

}

export default HintModal;