import { User } from "../../pages/MultiplayPage";
import ProfileImage from "../multi/ProfileImage";
import { Chat } from "./ChatArea";

interface ChatMessageProps {
  chat: Chat;
  user: User;
  profileVisible: boolean;
  myMessage: boolean;
}

function WaitChatMessage({ user, chat, profileVisible, myMessage }: ChatMessageProps) {
  return (
    <div className={`flex gap-2 ${myMessage ? "justify-end" : ""}`}>
      {/* <ProfileImage
        classes={`w-8 h-8 flex-shrink-0 rounded-full overflow-hidden ${profileVisible ? "" : "hidden"}`}
        src={user.profileImg}
      /> */}
      <div>
        {/* <p className={`mb-1 ${profileVisible ? "" : "hidden"}`}>{user.nickname}</p> */}
        {myMessage ? (
          <p className={`max-w-52 ms-auto py-1 px-2 text-white bg-secondary-500 rounded-lg rounded-tr-sm break-keep`}>
            {chat.message}
          </p>
        ) : (
          <p
            className={`max-w-52 py-1 px-2 bg-gray-100 text-gray-900 rounded-lg rounded-tl-sm break-keep ${profileVisible ? "" : "ms-10"}`}
          >
            {chat.message}
          </p>
        )}
      </div>
    </div>
  );
}

export default WaitChatMessage;
