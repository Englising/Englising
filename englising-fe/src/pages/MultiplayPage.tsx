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
import Input from "../component/multi/Input";
import { sentence } from "../assets/data/sample.js";

export interface User {
  userId: number;
  profileImage: string;
  nickname: string;
}

interface Sentence {
  index: number;
  word: string;
}

const TIME = 3;

function MultiplayPage() {
  const dialog = useRef<HTMLDialogElement>(null);
  const [time, setTime] = useState<number>(TIME);
  const [quiz, setQuiz] = useState<Sentence[][][]>();
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

  useEffect(() => {
    setQuiz(sentence);
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      setTime((prev) => prev - 1);
    }, 1000);

    const timer = setTimeout(
      () => {
        clearInterval(interval);
        dialog.current?.close();
        console.log("모달 클로즈");
      },
      time * 1000 + 1000
    );

    return () => {
      clearInterval(interval);
      clearTimeout(timer);
    };
  }, [time]);

  return (
    <>
      <div className="h-screen p-8 flex gap-10 text-white">
        <section className="shrink-0 grid grid-rows-[1fr_7fr_2fr] gap-4 justify-items-center">
          <p className="text-3xl font-bold text-secondary-400">Round 1/3</p>
          <div className="flex flex-col gap-4 justify-self-start">
            {userList.map((user) => {
              return <UserProfile key={user.userId} user={user} classes={"w-10 h-10"} />;
            })}
          </div>
          <Timer ref={dialog} roundTime={1} />
        </section>
        <section className="grow grid grid-rows-[1fr_7fr_2fr] gap-4">
          <p className="text-xl font-bold text-secondary-400 text-center">아보카도 좋아하는 모임</p>
          <div className="flex flex-col gap-4">
            <div className="bg-gradient-to-r from-secondary-400 to-purple-500 rounded-full p-px text-center">
              <div className="bg-gray-800 py-1 rounded-full">So come on, let's go</div>
            </div>
            <div className="h-full flex flex-col gap-6 justify-center">
              {quiz &&
                quiz.map((sentence, index) => {
                  return (
                    <div key={index} className="flex flex-wrap gap-6 justify-center">
                      {sentence.map((line, index) => {
                        return (
                          <div key={index} className="flex gap-2">
                            {line.map((word, index) => {
                              return <Input key={index} answer={word.word} index={word.index} />;
                            })}
                          </div>
                        );
                      })}
                    </div>
                  );
                })}
            </div>
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
      {/* <Modal ref={dialog}>
        <Timeout time={TIME} /> */}
      {/* <Success /> */}
      {/* <Fail /> */}
      {/* </Modal> */}
    </>
  );
}

export default MultiplayPage;
