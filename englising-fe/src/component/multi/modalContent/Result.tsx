import { useEffect } from "react";
import { Room } from "../../../pages/MultiplayPage";
import { useNavigate, Link } from "react-router-dom";
import Countdown from "./Countdown";

const Result = ({ room }: { room: Room }) => {
  const navigate = useNavigate();
  const time = 5;

  useEffect(() => {
    setTimeout(() => {
      navigate("/englising/selectMulti");
    }, time * 1000);
  }, []);

  return (
    <div className="relative min-w-[45rem] flex flex-col  gap-4 p-8 text-center">
      <p className="text-xl">{room.track?.title}</p>
      <p className="text-secondary-400 text-5xl font-bold">{room.result ? "성공" : "실패"}</p>
      <p>잠시 후 메인화면으로 이동합니다</p>
      <Link to={"/englising/selectMulti"} className="self-center py-2 px-4 bg-secondary-500 rounded-lg">
        나가기
      </Link>
      <div className="absolute top-3 right-4">
        <Countdown time={time} />
      </div>
    </div>
  );
};

export default Result;
