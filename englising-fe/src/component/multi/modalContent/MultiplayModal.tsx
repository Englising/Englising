import { Room } from "../../../pages/MultiplayPage";
import Hint from "./Hint/Hint";
import Timeout from "./Timeout";
import Result from "./Result";
import Fail from "./Fail";
import { Alphabet } from "../MultiInputArea";

type ModalProps = {
  status: string;
  round: number;
  room: Room;
  time: number;
  hintResult: Alphabet[] | number;
};

const MultiplayModal = ({ status, round, room, time, hintResult }: ModalProps) => {
  const roundTime = time;
  if (status == "ROUNDSTART") {
    if (round == 3) {
      return <Hint hint={room?.hint} />;
    } else {
      return (
        <Timeout time={time}>
          <p className="text-xl">
            {roundTime}초 뒤 게임 시작과 함께 음악이 재생됩니다
            <br />
            음악을 듣고 빈 칸을 채워보세요!
          </p>
        </Timeout>
      );
    }
  } else if (status == "INPUTEND") {
    return (
      <Timeout time={time}>
        <p className="text-secondary-400 font-bold text-3xl">답변이 제출되었습니다</p>
        <p className="mt-6 mb-12 font-bold text-xl">잠시 후, 이번 라운드의 결과가 공개됩니다</p>
      </Timeout>
    );
  } else if (status == "ROUNDEND") {
    if (round == 3 || room?.result) {
      return <Result room={room} />;
    } else {
      return (
        <Timeout>
          <Fail round={round} />
        </Timeout>
      );
    }
  } else if (status == "HINTRESULT") {
    return <Hint hint={room.hint} hintResult={hintResult} />;
  } else {
    return (
      <div className="min-w-[45rem] py-8 text-center">
        <p>다른 플레이어를 기다리는 중입니다</p>
        <p>잠시만 기다려 주세요</p>
        <p>gif 넣기</p>
      </div>
    );
  }
};

export default MultiplayModal;
