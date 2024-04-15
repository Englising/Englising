interface Props {
    onGameStart(): void
}
// <div className="w-1/3 h-1/2 absolute inset-0 bg-cover bg-center flex flex-col justify-end items-center" style={{ backgroundImage: "url('/src/assets/singleInfo.png')"}}>
const StartModal = ({onGameStart}: Props) => {
    return (
        <div className="fixed inset-0 flex flex-col items-center justify-center">
            <div className="w-1/3 h-[60%] bg-black rounded-3xl flex flex-col justify-center">
                <img src="https://englising-bucket.s3.ap-northeast-2.amazonaws.com/singleInfo.png" className="rounded-3xl" />
                <div className="p-6 flex justify-center bg-black rounded-3xl">
                    <div className="w-[8rem] h-[3rem] bg-secondary-300 rounded-lg flex items-center cursor-pointer" onClick={onGameStart}>
                        <div className="w-full text-center font-bold">
                            시작!
                        </div>
                    </div>
                </div>
            </div>    
        </div>
    );

}

export default StartModal;