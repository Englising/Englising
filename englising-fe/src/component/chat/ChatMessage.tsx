import { User } from "../../pages/MultiplayPage";
import ProfileImage from "../multi/ProfileImage";
import { Chat } from "./ChatArea";

interface ChatMessageProps {
  chat: Chat;
  myMessage: boolean;
}

function ChatMessage({ chat, myMessage }: ChatMessageProps) {
  return (
    <div className={`flex gap-2 ${myMessage ? "justify-end" : ""}`}>
      {myMessage ? (
        <p className={`max-w-52 ms-auto py-1 px-2 text-white bg-gray-800 rounded-lg rounded-tr-sm break-normal`}>
          {chat.message}
        </p>
      ) : (
        <p className={`max-w-52 py-1 px-2 bg-gray-100 text-gray-900 rounded-lg rounded-tl-sm break-normal`}>
          {chat.message}
        </p>
      )}
    </div>
  );
}

export default ChatMessage;
