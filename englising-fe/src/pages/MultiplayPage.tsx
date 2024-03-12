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

export interface User {
  userId: number;
  profileImage: string;
  nickname: string;
}

const TIME = 5;

function MultiplayPage() {
  const dialog = useRef<HTMLDialogElement>(null);
  const [time, setTime] = useState<number>(5);
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
      <div className="h-screen px-4 py-8 flex gap-10 text-white">
        <section className="grid grid-rows-[1fr_7fr_2fr] gap-4 justify-items-center">
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
              <div className="bg-gray-800 py-1 rounded-full">But one of these things is not like the other</div>
            </div>
            <div></div>
            <div className="justify-self-end bg-gradient-to-r from-secondary-400 to-purple-500 rounded-full p-px text-center">
              <div className="bg-gray-800 py-1 rounded-full">I promise that you'll never find another like</div>
            </div>
          </div>
          <NoticeArea />
        </section>
        <section className="grid grid-rows-[1fr_7fr_2fr] gap-4">
          <div className="flex-shrink-0"></div>
          <ChatArea />
          <MemoArea />
        </section>
      </div>
      <Modal ref={dialog}>
        <Timeout time={TIME} />
        {/* <Success /> */}
        {/* <Fail /> */}
      </Modal>
    </>
  );
}

export default MultiplayPage;
