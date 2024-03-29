interface Props {
    onGameStart(): void
}

const StartModal = ({onGameStart}: Props) => {

    return (
        <div className="fixed inset-0 flex items-center justify-center">
            <div className="absolute bg-black rounded-lg shadow-xl">
                <div className="bg-[url('src/assets/singleInfo.png')] w-full h-full flex flex-col justify-end items-center">
                    <div className="flex justify-center items-center" onClick={onGameStart}>
                        <div>Game Start</div>
                    </div>
                </div>
                
            </div>
        </div>
    );

}

export default StartModal;