import { useEffect, useRef, useState } from "react";
import ChatMessage from "./ChatMessage";

const loginUserId = 1;

function ChatArea() {
  const chatEndRef = useRef(null);
  const [chatList, setChatList] = useState([
    {
      chatId: 1,
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      chatMsg: "ㅎㅇ",
    },
    {
      chatId: 2,
      userId: 2,
      nickname: "망곰",
      profileImage:
        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/654/d10c916fa0beb0b2ea3590f8fef4b728_res.jpeg",
      chatMsg: "하이하이",
    },
    {
      chatId: 3,
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      chatMsg: "??",
    },
    {
      chatId: 4,
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      chatMsg: "머라는지모르겟는데....",
    },
    {
      chatId: 5,
      userId: 2,
      nickname: "망곰",
      profileImage:
        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/654/d10c916fa0beb0b2ea3590f8fef4b728_res.jpeg",
      chatMsg: "난바보야",
    },
    {
      chatId: 6,
      userId: 2,
      nickname: "망곰",
      profileImage:
        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/654/d10c916fa0beb0b2ea3590f8fef4b728_res.jpeg",
      chatMsg: "이번 라운드는 쉬겠씁니다",
    },
    {
      chatId: 7,
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      chatMsg: "안돼",
    },
    {
      chatId: 8,
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      chatMsg: "도라와~~",
    },
    {
      chatId: 9,
      userId: 2,
      nickname: "망곰",
      profileImage:
        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/654/d10c916fa0beb0b2ea3590f8fef4b728_res.jpeg",
      chatMsg: "중간에 레인보우 이건 확실함 진짜 rainbow 무조건이야",
    },
  ]);

  useEffect(() => {
    chatEndRef.current.scrollIntoView();
  }, [chatList]);

  return (
    <section className="max-h-[425px] flex flex-col p-2 bg-primary-400 rounded-lg">
      <p className="font-bold text-secondary-400 text-center">CHAT</p>
      <div className="flex flex-col grow gap-2 my-1 px-1 overflow-y-scroll text-sm scrollbar">
        {chatList &&
          chatList.map((chat, index) => {
            let profileVisible = false;

            if (index != 0) {
              const prevChatUser = chatList[index - 1].userId;

              if (prevChatUser != chat.userId) {
                profileVisible = true;
              }
            } else {
              profileVisible = true;
            }

            if (chat.userId == loginUserId) {
              profileVisible = false;
            }

            return (
              <ChatMessage
                key={chat.chatId}
                user={{ userId: chat.userId, nickname: chat.nickname, profileImage: chat.profileImage }}
                message={chat.chatMsg}
                profileVisible={profileVisible}
                myMessage={chat.userId === loginUserId}
              />
            );
          })}
        <div ref={chatEndRef}></div>
      </div>
      <div className="bg-white rounded-lg p-2 flex">
        <input className="grow text-gray-900 focus:outline-none" type="text" placeholder="메시지를 작성해주세요" />
        <button className="bg-secondary-100 rounded-md  px-2 py-1 text-gray-900 text-sm">전송</button>
      </div>
    </section>
  );
}

export default ChatArea;
