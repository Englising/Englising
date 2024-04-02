import { useState } from "react";
import HintRoulette from "./HintRoulette";
import MusicHint from "./MusicHint";
import CountHint from "./CountHint";
import OpenHint from "./OpenHint";
import { Alphabet } from "../../MultiInputArea";

function Hint({ hint, hintResult }: { hint: number; hintResult?: Alphabet[] | number }) {
  const [rouletteEnd, setRouletteEnd] = useState(false);
  const hintList = {
    1: <MusicHint speed={0.7} />,
    2: <MusicHint speed={2} />,
    3: <CountHint count={hintResult} />,
    4: <OpenHint />,
  };

  const handleRoulette = () => {
    setRouletteEnd(!rouletteEnd);
  };

  return (
    <>
      {rouletteEnd ? (
        <div className="min-w-[45rem] min-h-72 flex flex-col justify-center items-center py-8">{hintList[hint]}</div>
      ) : (
        <HintRoulette hint={hint} onRoulette={handleRoulette} />
      )}
    </>
  );
}

export default Hint;
