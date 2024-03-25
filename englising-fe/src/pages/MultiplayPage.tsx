import { useEffect, useRef, useState } from "react";
import UserProfile from "../component/multi/UserProfile";
import ChatArea from "../component/chat/ChatArea";
import MemoArea from "../component/multi/MemoArea";
import NoticeArea from "../component/multi/NoticeArea";
import Timer from "../component/multi/Timer";
import Modal from "../component/multi/Modal";
import Timeout from "../component/multi/modalContent/Timeout";
import Success from "../component/multi/modalContent/Success";
import Fail from "../component/multi/modalContent/Fail";
import HintRoulette from "../component/multi/modalContent/HintRoulette.js";
import useStomp from "../hooks/useStomp.js";
import { Client, IMessage } from "@stomp/stompjs";
import { useParams } from "react-router";
import MultiInputArea from "../component/multi/MultiInputArea.js";
import { Quiz } from "../component/multi/MultiInputArea";

export interface User {
  userId: number;
  profileImage: string;
  nickname: string;
}

const TIME = 5;

function MultiplayPage() {
  const roundClient = useRef<Client>();
  const timeClient = useRef<Client>();
  const dialog = useRef<HTMLDialogElement>(null);
  const { multiId } = useParams();
  const [modalOpen, setModalOpen] = useState(false);
  const [status, setStatus] = useState<string>();
  const [round, setRound] = useState<number>();
  const [time, setTime] = useState<number>();
  const [leftTime, setLeftTime] = useState<number>();
  const [quiz, setQuiz] = useState<Quiz[]>();
  const [userList, setUserList] = useState([
    {
      userId: 1,
      nickname: "망곰",
      profileImage:
        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/654/d10c916fa0beb0b2ea3590f8fef4b728_res.jpeg",
    },
    {
      userId: 2,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
    },
  ]);

  const handleModalOpen = () => {
    setModalOpen(!modalOpen);
  };

  useEffect(() => {
    roundConnect();
    timeConnect();
    // leftTime: 1
    // message: "남은 시간 타이머"

    return () => {
      roundDisconnect();
      timeDiscconnect();
    };
  }, []);

  const roundCallback = (body: IMessage) => {
    const json = JSON.parse(body.body);
    // console.log("round sub", json);
    setStatus(json.status);
    setRound(json.round);
    switch (json.status) {
      case "ROUNDSTART":
        setModalOpen(true);
        // 1라운드일 때 문제 정보 받기
        if (json.round == 1) {
          setQuiz(json.data);
          setTime(3);
        }

        if (json.round == 3) {
          setModalOpen(false);
        }
        break;
      case "MUSICSTART":
        setModalOpen(false);
        break;
      case "INPUTSTART":
        setModalOpen(false);
        setTime(10);
        break;
      case "INPUTEND":
        setModalOpen(true);
        setTime(3);
        break;
      case "ROUNDEND":
        setModalOpen(true);
        break;
    }
  };

  const timeCallback = (body: IMessage) => {
    const json = JSON.parse(body.body);
    setLeftTime(json.leftTime);
  };

  const [roundConnect, roundDisconnect] = useStomp(roundClient, `round/${multiId}`, roundCallback);
  const [timeConnect, timeDiscconnect] = useStomp(timeClient, `time/${multiId}`, timeCallback);

  useEffect(() => {
    if (modalOpen) {
      dialog.current?.showModal();
    } else {
      dialog.current?.close();
    }
  }, [modalOpen]);

  useEffect(() => {
    // setModalOpen(false);
  }, [status]);

  return (
    <>
      <div className="h-screen p-8 flex gap-10 bg-gray-800 text-white">
        <section className="shrink-0 grid grid-rows-[1fr_7fr_2fr] gap-4 justify-items-center">
          <p className="text-3xl font-bold text-secondary-400">
            Round {round}/<span className="text-white">3</span>
          </p>
          <div className="flex flex-col gap-4 justify-self-start">
            {userList.map((user) => {
              return <UserProfile key={user.userId} user={user} classes={"w-10 h-10"} />;
            })}
          </div>
          {status == "INPUTSTART" ? (
            <Timer ref={dialog} roundTime={time} status={status} leftTime={leftTime} onModalOpen={handleModalOpen} />
          ) : (
            ""
          )}
        </section>
        <section className="grow grid grid-rows-[1fr_7fr_2fr] gap-4">
          <p className="text-xl font-bold text-secondary-400 text-center">아보카도 좋아하는 모임</p>
          <div className="flex flex-col gap-4">
            <div className="bg-gradient-to-r from-secondary-400 to-purple-500 rounded-full p-px text-center">
              <div className="bg-gray-800 py-1 rounded-full">So come on, let's go</div>
            </div>
            <div className="h-full flex flex-col gap-6 justify-center">{quiz && <MultiInputArea quiz={quiz} />}</div>
            <div className="justify-self-end bg-gradient-to-r from-secondary-400 to-purple-500 rounded-full p-px text-center">
              <div className="bg-gray-800 py-1 rounded-full">where we'll have some fun</div>
            </div>
          </div>
          <NoticeArea />
        </section>
        <section className="shrink-0 grid grid-rows-[1fr_7fr_2fr] gap-4">
          <div className="flex-shrink-0">
            <button>나가기</button>
          </div>
          <ChatArea />
          <MemoArea />
        </section>
      </div>
      {modalOpen && (
        <Modal ref={dialog}>
          <Timeout time={time}>
            {status == "ROUNDSTART" && (
              <p>
                {time}초 뒤 게임 시작과 함께
                <br />
                문제 구간의 음악이 재생됩니다!
              </p>
            )}
            {status == "INPUTEND" && (
              <>
                <p className="text-secondary-400 font-bold text-3xl">답변 제출 기간이 종료되었습니다</p>
                <p className="mt-6 mb-12 font-bold text-xl">{time}초 후, 이번 라운드의 결과가 공개됩니다</p>
              </>
            )}
            {status == "ROUNDEND" && (
              <>
                <p>라운드 결과 들어갈곳</p>
              </>
            )}
          </Timeout>
          {/* <Success /> */}
          {/* <Fail /> */}
          {/* <HintRoulette /> */}
        </Modal>
      )}
    </>
  );
}

export default MultiplayPage;
