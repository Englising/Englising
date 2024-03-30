import { useEffect, useRef, useState } from "react";
import { Client, IMessage } from "@stomp/stompjs";
import { useParams, useNavigate } from "react-router";
import UserProfile from "../component/multi/UserProfile";
import ChatArea from "../component/chat/ChatArea";
import MemoArea from "../component/multi/MemoArea";
import Timer from "../component/multi/Timer";
import Modal from "../component/multi/Modal";
import MultiInputArea, { Alphabet } from "../component/multi/MultiInputArea";
import { Quiz } from "../component/multi/MultiInputArea";
import MultiMusicPlayer from "../component/multi/MultiMusicPlayer";
import MultiplayModal from "../component/multi/modalContent/MultiplayModal";
import { getMultiplayInfo } from "../util/multiAxios";
import useStomp from "../hooks/useStomp";

export interface User {
  userId: string;
  profileImg: string;
  nickname: string;
  color?: string;
}

export interface PlayInfo {
  url: string;
  startTime: number;
  endTime: number;
  speed: number;
  onPlay: number;
}

type Track = {
  afterLyric: string;
  beforeLyric: string;
  title: string;
  youtubeId: string;
  startTime: number;
  endTime: number;
};

export interface Room {
  name: string;
  currentUser: User[];
  hint: number;
  result?: boolean;
  track?: Track;
  answer?: Quiz[];
}

function MultiplayPage() {
  const roundClient = useRef<Client>();
  const timeClient = useRef<Client>();
  const dialog = useRef<HTMLDialogElement>(null);
  const navigate = useNavigate();
  const { multiId } = useParams();
  const [modalOpen, setModalOpen] = useState(true);
  const [status, setStatus] = useState<string>();
  const [round, setRound] = useState<number>(1);
  const [time, setTime] = useState<number>();
  const [leftTime, setLeftTime] = useState<number>();
  const [quiz, setQuiz] = useState<Quiz[]>([]);
  const [track, setTrack] = useState<Track | null>(null);
  const [room, setRoom] = useState<Room | null>(null);
  const [hintResult, setHintResult] = useState<Alphabet[] | number>([]);

  const handleModalOpen = () => {
    setModalOpen(!modalOpen);
  };

  const [playInfo, setPlayInfo] = useState<PlayInfo>({
    url: "", //처음에 초기화 해줘야함.
    startTime: -1,
    endTime: -1,
    speed: 1,
    onPlay: 0,
  });

  const basicPlay = (speed: number) => {
    // 특정시간에 도달하면 playInfo 값 넣어주기 (url, startTime, endTime, speed)
    setPlayInfo({
      ...playInfo,
      speed: speed,
      onPlay: (playInfo.onPlay + 1) % 2,
    });
  };

  // const fastPlay = () => {
  //   // 특정시간에 도달하면 playInfo 값 넣어주기 (url, startTime, endTime, speed)
  //   setPlayInfo({
  //     ...playInfo,
  //     startTime: 60,
  //     endTime: 65,
  //     speed: 2,
  //     onPlay: (playInfo.onPlay + 1) % 2,
  //   });
  // };

  // const slowPlay = () => {
  //   // 특정시간에 도달하면 playInfo 값 넣어주기 (url, startTime, endTime, speed)
  //   setPlayInfo({
  //     ...playInfo,
  //     startTime: 60,
  //     endTime: 65,
  //     speed: 0.7,
  //     onPlay: (playInfo.onPlay + 1) % 2,
  //   });
  // };

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
            startTime: json.data.beforeLyricStartTime,
            endTime: json.data.afterLyricEndTime,
          });
          setRoom((prev) => {
            return { ...prev, hint: parseInt(json.data.selectedHint), answer: json.data.sentences };
          });
          setPlayInfo({
            url: `https://www.youtube.com/watch?v=${json.data.youtubeId}`, //처음에 초기화 해줘야함.
            startTime: json.data.beforeLyricStartTime,
            endTime: json.data.afterLyricEndTime,
            speed: 1,
            onPlay: 0,
          });
        }

        break;
      case "MUSICSTART":
        setModalOpen(false);
        if (track) {
          setTime(track?.endTime - track?.startTime);
        }
        break;
      case "INPUTSTART":
        setModalOpen(false);
        setTime(30);
        break;
      case "INPUTEND":
        setModalOpen(true);
        setTime(3);
        break;
      case "ROUNDEND":
        setModalOpen(true);
        setTime(0);
        setRoom((prev) => {
          return { ...prev, result: json.data };
        });
        if (json.round == 3) {
          console.log("마지막");
          setTimeout(() => {
            basicPlay(1);
          }, 1000);
        }
        break;
      case "HINTRESULT":
        setHintResult(json.data);
    }
  };

  // const timeCallback = (body: IMessage) => {
  //   const json = JSON.parse(body.body);
  //   setLeftTime(json.leftTime);
  // };

  const [roundConnect, roundDisconnect] = useStomp(roundClient, `/sub/round/${multiId}`, roundCallback);
  // const [timeConnect, timeDiscconnect] = useStomp(timeClient, `time/${multiId}`, timeCallback);

  const handleLeaveGame = () => {
    if (confirm("게임을 종료하시겠습니까?")) {
      navigate("/englising/selectMulti");
    }
  };

  useEffect(() => {
    roundConnect();
    // timeConnect();

    // 참여 게임 정보 받기
    getMultiplayInfo(multiId as string)
      .then((res) => {
        console.log(res.data.data.currentUser);
        setRoom({
          currentUser: res.data.data.currentUser,
          name: res.data.data.roomName,
          hint: 0,
        });
      })
      .catch((e) => console.log(e));

    return () => {
      roundDisconnect();
      // timeDiscconnect();
    };
  }, []);

  useEffect(() => {
    setRoom((prev) => {
      return { ...prev, track: track };
    });
  }, [track]);

  useEffect(() => {
    if (modalOpen) {
      dialog.current?.showModal();
    } else {
      dialog.current?.close();
      if (status == "MUSICSTART") {
        if (round == 3) {
          if (room?.hint == 1) {
            basicPlay(0.7);
          } else {
            basicPlay(2);
          }
        } else {
          basicPlay(1);
        }
      }
    }
  }, [modalOpen]);

  return (
    <>
      <div className="hidden">
        <MultiMusicPlayer playInfo={playInfo} />
      </div>
      <div className="h-screen p-8 flex gap-10 bg-gray-800 text-white">
        <section className="shrink-0 grid grid-rows-[0.5fr_7fr_2fr] gap-4 justify-items-center">
          <p className="text-3xl font-bold text-secondary-400">
            Round {round}/<span className="text-white">3</span>
          </p>
          <div className="flex flex-col gap-4 justify-self-start">
            {room?.currentUser &&
              room.currentUser.map((user) => {
                return (
                  <UserProfile
                    key={user.userId}
                    user={user}
                    classes={`w-10 h-10 flex justify-center items-center   p-1.5`}
                  />
                );
              })}
          </div>
          {status == "INPUTSTART" ? (
            <Timer ref={dialog} roundTime={time || 0} status={status} onModalOpen={handleModalOpen} />
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
            <div className="h-full flex flex-col gap-4 justify-around">
              {quiz && <MultiInputArea quiz={quiz} hintResult={hintResult} />}
            </div>
            <div className="justify-self-end bg-gradient-to-r from-secondary-400 to-purple-500 rounded-full p-px text-center">
              <div className="bg-gray-800 py-1 rounded-full">{track && track.afterLyric}</div>
            </div>
          </div>
        </section>
        <section className="shrink-0 grid grid-rows-[0.5fr_7fr_2fr] gap-4">
          <div className="flex-shrink-0">
            <button onClick={handleLeaveGame}>나가기</button>
          </div>
          <ChatArea />
          <MemoArea />
        </section>
      </div>
      <Modal ref={dialog}>
        {modalOpen && <MultiplayModal status={status} round={round} room={room} time={time} hintResult={hintResult} />}
      </Modal>
    </>
  );
}

export default MultiplayPage;
