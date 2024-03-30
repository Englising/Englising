import { Client, IMessage } from "@stomp/stompjs";
import { MutableRefObject } from "react";

const useStomp = (client: MutableRefObject<Client | undefined>, url: string, callback: (body: IMessage) => void) => {
  const connect = () => {
    client.current = new Client({
      brokerURL: import.meta.env.VITE_SOKCET_BROKER_URL,
      onConnect: () => {
        console.log("connect success");
        client.current?.subscribe(`${url}`, callback);
      },
    });

    client.current.activate();
  };

  const disconnect = () => {
    client.current?.deactivate();
  };

  return [connect, disconnect];
};

export default useStomp;
