import { useEffect, useRef, useState } from "react";
import { Client, IMessage } from "@stomp/stompjs";
import { useParams } from "react-router";
import UserProfile from "../component/multi/UserProfile";
import ChatArea from "../component/chat/ChatArea";
import MemoArea from "../component/multi/MemoArea";
import Timer from "../component/multi/Timer";
import Modal from "../component/multi/Modal";
import Timeout from "../component/multi/modalContent/Timeout";
import Success from "../component/multi/modalContent/Success";
import Fail from "../component/multi/modalContent/Fail";
import HintRoulette from "../component/multi/modalContent/Hint/HintRoulette.js";
import MultiInputArea from "../component/multi/MultiInputArea";
import { Quiz } from "../component/multi/MultiInputArea";
import useStomp from "../hooks/useStomp.js";
import { getMultiplayInfo } from "../util/multiAxios.js";
import Hint from "../component/multi/modalContent/Hint/Hint.js";

export interface User {
  userId: number;
  profileImage: string;
  nickname: string;
}

type Track = {
  afterLyric: string;
  beforeLyric: string;
  title: string;
  youtubeId: string;
};

type Room = {
  name: string;
  currentUser: User[];
  hint?: string;
};

function MultiplayPage() {
  const roundClient = useRef<Client>();
  const timeClient = useRef<Client>();
  const dialog = useRef<HTMLDialogElement>(null);
  const { multiId } = useParams();
  const [modalOpen, setModalOpen] = useState(false);
  const [status, setStatus] = useState<string>();
  const [round, setRound] = useState<number>(1);
  const [time, setTime] = useState<number>();
  const [leftTime, setLeftTime] = useState<number>();
  const [quiz, setQuiz] = useState<Quiz[]>();
  const [track, setTrack] = useState<Track>();
  const [room, setRoom] = useState<Room>();

  const handleModalOpen = () => {
    setModalOpen(!modalOpen);
  };

  useEffect(() => {
    roundConnect();
    timeConnect();

    // 참여 게임 정보 받기
    // getMultiplayInfo(multiId as string)
    //   .then((res) => {
    //     setRoom({
    //       currentUser: res.data.data.currentUser,
    //       name: res.data.data.roomName,
    //     });
    //   })
    //   .catch((e) => console.log(e));

    return () => {
      roundDisconnect();
      timeDiscconnect();
    };
  }, []);

  const roundCallback = (body: IMessage) => {
    const json = JSON.parse(body.body);
    console.log("round", json);
    setStatus(json.status);
    setRound(json.round);
    switch (json.status) {
      case "ROUNDSTART":
        setModalOpen(true);
        setTime(3);
        // 1라운드일 때 문제 정보 받기
        if (json.round == 1) {
          setQuiz(json.data.sentences);
          setTrack({
            afterLyric: json.data.afterLyric,
            beforeLyric: json.data.beforeLyric,
            title: json.data.trackTitle,
            youtubeId: json.data.youtubeId,
          });
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
        setTime(0);
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
    console.log(status, time);
  }, [status]);

  return (
    <>
      <div className="h-screen p-8 flex gap-10 bg-gray-800 text-white">
        <section className="shrink-0 grid grid-rows-[0.5fr_7fr_2fr] gap-4 justify-items-center">
          <p className="text-3xl font-bold text-secondary-400">
            Round {round}/<span className="text-white">3</span>
          </p>
          <div className="flex flex-col gap-4 justify-self-start">
            {room?.currentUser.map((user) => {
              return <UserProfile key={user.userId} user={user} classes={"w-10 h-10"} />;
            })}
          </div>
          {status == "INPUTSTART" ? (
            <Timer ref={dialog} roundTime={time} status={status} leftTime={leftTime} onModalOpen={handleModalOpen} />
          ) : (
            ""
          )}
        </section>
        <section className="grow grid grid-rows-[0.5fr_9fr] gap-4">
          <p className="text-xl font-bold text-secondary-400 text-center">{room && room.name}</p>
          <div className="flex flex-col gap-4">
            <div className="bg-gradient-to-r from-secondary-400 to-purple-500 rounded-full p-px text-center">
              <div className="bg-gray-800 py-1 rounded-full">{track && track.beforeLyric}</div>
            </div>
            <div className="h-full flex flex-col gap-4 justify-center">{quiz && <MultiInputArea quiz={quiz} />}</div>
            <div className="justify-self-end bg-gradient-to-r from-secondary-400 to-purple-500 rounded-full p-px text-center">
              <div className="bg-gray-800 py-1 rounded-full">{track && track.afterLyric}</div>
            </div>
          </div>
        </section>
        <section className="shrink-0 grid grid-rows-[0.5fr_7fr_2fr] gap-4">
          <div className="flex-shrink-0">
            <button>나가기</button>
          </div>
          <ChatArea />
          <MemoArea />
        </section>
      </div>
      {modalOpen && (
        <Modal ref={dialog}>
          {round == 3 && status == "ROUNDSTART" ? (
            <Hint />
          ) : (
            <Timeout time={time as number}>
              {status == "ROUNDSTART" && (
                <p>
                  {time}초 뒤 게임 시작과 함께
                  <br />
                  문제 구간의 음악이 재생됩니다!
                </p>
              )}
              {status == "INPUTEND" && (
                <>
                  <p className="text-secondary-400 font-bold text-3xl">답변이 제출되었습니다</p>
                  <p className="mt-6 mb-12 font-bold text-xl">{time}초 후, 이번 라운드의 결과가 공개됩니다</p>
                </>
              )}
              {status == "ROUNDEND" && (
                <>
                  <p>라운드 결과 들어갈곳</p>
                </>
              )}
            </Timeout>
          )}
          {/* <Success /> */}
          {/* <Fail /> */}
        </Modal>
      )}
    </>
  );
}

export default MultiplayPage;
