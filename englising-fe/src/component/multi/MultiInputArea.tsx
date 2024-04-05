import { useEffect, useRef, useState } from "react";
import { useParams } from "react-router";
import { Client, IMessage } from "@stomp/stompjs";
import MultiInput from "../multi/MultiInput";
import useStomp from "../../hooks/useStomp";

export type Quiz = {
  sentenceIndex: number;
  words: Word[];
};

export type Word = {
  wordIndex: number;
  alphabets: Alphabet[];
};

export type Alphabet = {
  alphabetIndex: number;
  alphabet: string;
};

const MultiInputArea = ({ quiz, hintResult }: { quiz: Quiz[]; hintResult?: Alphabet[] | number }) => {
  const client = useRef<Client>();
  const { multiId } = useParams();
  const [changedAnswer, setChangedAnswer] = useState<Alphabet | undefined>();
  const [focusTarget, setFocustTarget] = useState<number>();

  const handdleFocusChange = (index: number) => {
    setFocustTarget(index + 1);
  };

  const handleInputChange = (val: Alphabet) => {
    if (val.alphabet == val.alphabet.toUpperCase()) {
      val.alphabet = val.alphabet.toLowerCase();
    }

    setChangedAnswer(val);
    publish(val);
  };

  const callback = (body: IMessage) => {
    const json = JSON.parse(body.body);
    setChangedAnswer({ alphabetIndex: json.alphabetIndex, alphabet: json.alphabet });
  };

  const [connect, disconnect] = useStomp(client, `/sub/answer/${multiId}`, callback);

  const publish = (ans: Alphabet) => {
    if (!client.current?.connected) return;

    client.current.publish({
      destination: `/pub/answer/${multiId}`,
      body: JSON.stringify({
        alphabetIndex: ans.alphabetIndex,
        alphabet: ans.alphabet,
      }),
    });

    setChangedAnswer(undefined);
  };

  useEffect(() => {
    connect();

    return () => disconnect();
  }, []);

  return (
    <>
      {quiz.map((words, index) => {
        return (
          <div key={index} className="flex flex-wrap gap-6 justify-center">
            {words.words.map((alphabets, index) => {
              return (
                <div key={index} className="flex gap-2">
                  {alphabets.alphabets.map((alphabet, index) => {
                    return (
                      <MultiInput
                        key={index}
                        index={alphabet.alphabetIndex}
                        answer={alphabet}
                        onInputChange={handleInputChange}
                        onFocusChange={handdleFocusChange}
                        changedAnswer={changedAnswer}
                        hintResult={hintResult}
                        focusTarget={focusTarget as number}
                      />
                    );
                  })}
                </div>
              );
            })}
          </div>
        );
      })}
    </>
  );
};

export default MultiInputArea;
