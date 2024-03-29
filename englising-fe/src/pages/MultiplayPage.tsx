import { useEffect, useRef, useState } from "react";
import { Client, IMessage } from "@stomp/stompjs";
import { useParams } from "react-router";
import UserProfile from "../component/multi/UserProfile";
import ChatArea from "../component/chat/ChatArea";
import MemoArea from "../component/multi/MemoArea";
import Timer from "../component/multi/Timer";
import Modal from "../component/multi/Modal";
import Timeout from "../component/multi/modalContent/Timeout";
import MultiInputArea, { Alphabet } from "../component/multi/MultiInputArea";
import { Quiz } from "../component/multi/MultiInputArea";
import Hint from "../component/multi/modalContent/Hint/Hint";
import MultiMusicPlayer from "../component/multi/MultiMusicPlayer";
import Result from "../component/multi/modalContent/Result";
import Fail from "../component/multi/modalContent/Fail";
import useStomp from "../hooks/useStomp";
import { getMultiplayInfo } from "../util/multiAxios";

export interface User {
  userId: number;
  profileImage: string;
  nickname: string;
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
}

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
  const [quiz, setQuiz] = useState<Quiz[]>([]);
  const [track, setTrack] = useState<Track | null>(null);
  const [room, setRoom] = useState<Room | null>(null);
  const [hintResult, setHintResult] = useState<Alphabet[] | null>(null);

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

  const basicPlay = () => {
    // 특정시간에 도달하면 playInfo 값 넣어주기 (url, startTime, endTime, speed)
    setPlayInfo({
      ...playInfo,
      speed: 1,
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

  useEffect(() => {
    roundConnect();
    timeConnect();

    // 참여 게임 정보 받기
    getMultiplayInfo(multiId as string)
      .then((res) => {
        setRoom({
          currentUser: res.data.data.currentUser,
          name: res.data.data.roomName,
          hint: 0,
        });
      })
      .catch((e) => console.log(e));

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

    if (json.data.sentences != undefined) {
      setPlayInfo({
        url: `https://www.youtube.com/watch?v=${json.data.youtubeId}`, //처음에 초기화 해줘야함.
        startTime: json.data.sentences[0].startTime,
        endTime: json.data.sentences[json.data.sentences.length - 1].endTime,
        speed: 1,
        onPlay: 0,
      });
    }

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
            startTime: 1,
            endTime: 2,
          });
          setRoom((prev) => {
            return { ...prev, hint: parseInt(json.data.selectedHint) };
          });
        }

        break;
      case "MUSICSTART":
        setModalOpen(false);
        if (track != undefined) setTime(track?.endTime - track?.startTime);
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
        break;
      case "HINTRESULT":
        setHintResult(json.data);
    }
  };

  const timeCallback = (body: IMessage) => {
    const json = JSON.parse(body.body);
    setLeftTime(json.leftTime);
  };

  const [roundConnect, roundDisconnect] = useStomp(roundClient, `round/${multiId}`, roundCallback);
  const [timeConnect, timeDiscconnect] = useStomp(timeClient, `time/${multiId}`, timeCallback);

  const showHintModal = (status?: string, round?: number, room?: Room) => {
    console.log(room);
    if (status == "ROUNDSTART") {
      if (round == 3) {
        return <Hint hint={room?.hint} />;
      } else {
        return (
          <Timeout time={time}>
            <p>
              {time}초 뒤 게임 시작과 함께 음악이 재생됩니다
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
          <p className="mt-6 mb-12 font-bold text-xl">{time}초 후, 이번 라운드의 결과가 공개됩니다</p>
          {round == 2 && <p>다음 라운드엔 특별한 힌트가 지급되니 </p>}
        </Timeout>
      );
    } else if (status == "ROUNDEND") {
      if (round == 3 || room.result) {
        return <Result room={room} />;
      } else {
        return (
          <Timeout>
            <Fail />
          </Timeout>
        );
      }
    } else if (status == "HINTRESULT") {
      return <Hint hint={room.hint} />;
    }
  };

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
    }

    if (!modalOpen && status == "MUSICSTART") {
      basicPlay();
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
            {/* {room?.currentUser.map((user) => {
              return <UserProfile key={user.userId} user={user} classes={"w-10 h-10"} />;
            })} */}
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
            <div className="h-full flex flex-col gap-4 justify-center">
              {quiz && <MultiInputArea quiz={quiz} hintResult={hintResult} />}
            </div>
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
      <Modal ref={dialog}>{modalOpen && showHintModal(status, round, room)}</Modal>
    </>
  );
}

export default MultiplayPage;
