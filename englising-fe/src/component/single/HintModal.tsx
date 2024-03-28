
interface Props {
    hintWord: string,
    hintNum: number,
    onUse(hintword: string): void,
    onCancel(): void
}

const HintModal = ({hintWord, hintNum, onUse, onCancel}: Props) => {

    return (
        <div className="fixed inset-0 flex items-center justify-center">
            <div className="absolute bg-white rounded-lg shadow-xl">
                {hintNum > 0 ?
                    (<div>
                        <p>힌트를 사용하시겠습니까?</p>
                        <p>{hintNum} / 3</p> 
                        <div className="flex justify-center">
                            <div className="m-1" onClick={() => onUse(hintWord)}>사용</div>
                            <div className="m-1" onClick={onCancel}>취소</div>
                        </div>
                    </div>):
                    (<div>
                        <p>힌트를 모두 소진하셨습니다.</p>
                        <div className="m-1" onClick={onCancel}>확인</div>
                    </div>
                    )
                }
            </div>
        </div>
    );

}

export default HintModal;