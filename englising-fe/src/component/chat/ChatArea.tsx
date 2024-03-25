import { useEffect, useRef, useState } from "react";
import ChatMessage from "./ChatMessage";
import { useParams } from "react-router";
import { Client, IMessage } from "@stomp/stompjs";
import useStomp from "../../hooks/useStomp";

const loginUserId = 1;

type Chat = {
  userId: number;
  message: string;
};

function ChatArea() {
  const chatEndRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const client = useRef<Client>();
  const { multiId } = useParams();
  const [chatList, setChatList] = useState([
    {
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      message: "ㅎㅇ",
    },
    {
      userId: 2,
      nickname: "망곰",
      profileImage:
        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/654/d10c916fa0beb0b2ea3590f8fef4b728_res.jpeg",
      message: "하이하이",
    },
    {
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      message: "??",
    },
    {
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      message: "머라는지모르겟는데....",
    },
    {
      userId: 2,
      nickname: "망곰",
      profileImage:
        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/654/d10c916fa0beb0b2ea3590f8fef4b728_res.jpeg",
      message: "난바보야",
    },
    {
      chatId: 6,
      userId: 2,
      nickname: "망곰",
      profileImage:
        "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/654/d10c916fa0beb0b2ea3590f8fef4b728_res.jpeg",
      message: "이번 라운드는 쉬겠씁니다",
    },
    {
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      message: "안돼",
    },
    {
      userId: 1,
      nickname: "마루",
      profileImage: "https://i.pinimg.com/564x/86/42/7f/86427ff9f865c6e943e2b86497cbf098.jpg",
      message: "도라와~~",
    },
  ]);

  const callback = (body: IMessage) => {
    const json = JSON.parse(body.body);
    setChatList((prev) => [...prev, json]);
  };

  const [connect, disconnect] = useStomp(client, `chat/${multiId}`, callback);

  const publish = (chat: Chat) => {
    if (!client.current?.connected) return;

    client.current.publish({
      destination: `/pub/chat/${multiId}`,
      body: JSON.stringify({
        userId: loginUserId,
        message: inputRef.current?.value,
      }),
    });
  };

  const handleChatSubmit = () => {
    if (!inputRef.current) return;
    const chat = { userId: loginUserId, message: inputRef.current.value };
    publish(chat);
    inputRef.current.value = "";
  };

  useEffect(() => {
    if (chatEndRef.current) {
      chatEndRef.current.scrollIntoView();
    }
  }, [chatList]);

  useEffect(() => {
    connect();

    return () => disconnect();
  }, []);

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
                key={index}
                user={{ userId: chat.userId, nickname: chat.nickname, profileImage: chat.profileImage }}
                message={chat.message}
                profileVisible={profileVisible}
                myMessage={chat.userId === loginUserId}
              />
            );
          })}
        <div ref={chatEndRef}></div>
      </div>
      <div className="bg-white rounded-lg p-2 flex">
        <input
          className="grow text-gray-900 focus:outline-none"
          type="text"
          placeholder="메시지를 작성해주세요"
          ref={inputRef}
        />
        <button className="bg-secondary-100 rounded-md  px-2 py-1 text-gray-900 text-sm" onClick={handleChatSubmit}>
          전송
        </button>
      </div>
    </section>
  );
}

export default ChatArea;
