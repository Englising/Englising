import { FormEvent, useEffect, useRef, useState } from "react";
import { useParams } from "react-router";
import { Client, IMessage } from "@stomp/stompjs";
import ChatMessage from "./ChatMessage";
import useStomp from "../../hooks/useStomp";
import styles from "../multi/Multi.module.css";
import { User } from "../../pages/MultiplayPage";
import { getMultiplayInfo } from "../../util/multiAxios";

export type Chat = {
  userId: string;
  message: string;
  mine: boolean;
};

function ChatArea() {
  const chatEndRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const client = useRef<Client>();
  const { multiId } = useParams();
  const [chatList, setChatList] = useState<Chat[]>([]);
  const [user, setUser] = useState<User | null>();
  const [userList, setUserList] = useState<User[]>([]);
  const loginUserId = localStorage.getItem("userId");

  const callback = (body: IMessage) => {
    const json = JSON.parse(body.body);
    setChatList((prev) => [...prev, json]);
  };

  const [connect, disconnect] = useStomp(client, `/user/sub/chat/${multiId}`, callback);

  const publish = () => {
    if (!client.current?.connected) return;

    client.current.publish({
      destination: `/pub/chat/${multiId}`,
      body: JSON.stringify({
        message: inputRef.current?.value,
      }),
    });
  };

  const handleChatSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (!inputRef.current || inputRef.current.value.length <= 0) return;

    publish();
    inputRef.current.value = "";
  };

  useEffect(() => {
    if (chatEndRef.current) {
      chatEndRef.current.scrollIntoView();
    }
  }, [chatList]);

  useEffect(() => {
    connect();

    getMultiplayInfo(multiId)
      .then((res) => {
        for (const user of res.data.data.currentUser) {
          if (user.userId == loginUserId) {
            setUser(user);
            break;
          }
        }

        setUserList(res.data.data.currentUser);
      })
      .catch((err) => console.log(err));

    return () => disconnect();
  }, []);

  return (
    <section className="relative h-full flex flex-col p-2 bg-primary-400 rounded-lg">
      <p className="font-bold text-secondary-400 text-center">CHAT</p>
      <div
        className={`absolute w-full h-[calc(100%_-_100px)] left-0 bottom-14 flex flex-col grow gap-2 my-1 px-2 overflow-y-scroll text-sm ${styles.scrollbar}`}
      >
        {chatList?.length > 0 &&
          chatList.map((chat, index) => {
            return <ChatMessage key={index} chat={chat} myMessage={chat.mine} />;
          })}
        <div ref={chatEndRef}></div>
      </div>
      <form className="flex mt-auto p-2 bg-white rounded-lg" onSubmit={handleChatSubmit}>
        <input
          className="grow text-gray-900 focus:outline-none"
          type="text"
          placeholder="메시지를 작성해주세요"
          ref={inputRef}
        />
        <button className="bg-secondary-100 rounded-md  px-2 py-1 text-gray-900 text-sm">전송</button>
      </form>
    </section>
  );
}

export default ChatArea;
