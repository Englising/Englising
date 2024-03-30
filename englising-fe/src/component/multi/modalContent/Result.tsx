import { useEffect } from "react";
import { Room } from "../../../pages/MultiplayPage";
import { useNavigate, Link } from "react-router-dom";
import Countdown from "./Countdown";

const Result = ({ room }: { room: Room }) => {
  const navigate = useNavigate();
  const time = 5;

  useEffect(() => {
    setTimeout(
      () => {
        navigate("/englising/selectMulti");
      },
      time * 1000 + room.track?.endTime - room.track?.startTime
    );
  }, []);

  return (
    <div className="relative min-w-[45rem] max-w-[65rem] flex flex-col  gap-4 px-16 py-10 text-center">
      <p className="text-secondary-400 text-5xl font-bold">{room.result ? "성공" : "실패"}</p>
      <p className="mb-6 text-xl">{room.track?.title}</p>
      {room.answer.map((words, index) => {
        return (
          <div key={index} className="flex flex-wrap gap-4 justify-center">
            {words.words.map((alphabets, index) => {
              return (
                <div key={index} className="flex gap-0.5">
                  {alphabets.alphabets.map((alphabet, index) => {
                    return (
                      <p key={index} className="text-xl">
                        {alphabet.alphabet}
                      </p>
                    );
                  })}
                </div>
              );
            })}
          </div>
        );
      })}
      <p className="mt-5">잠시 후 메인화면으로 이동합니다</p>
      <Link
        to={"/englising/selectMulti"}
        className="self-center py-2 px-4 bg-secondary-200 rounded-lg text-black font-bold"
      >
        나가기
      </Link>
      <div className="absolute top-6 right-6">
        <Countdown time={time} />
      </div>
    </div>
  );
};

export default Result;
