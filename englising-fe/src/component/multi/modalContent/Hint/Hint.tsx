import { useState } from "react";
import HintRoulette from "./HintRoulette";
import MusicHint from "./MusicHint";
import CountHint from "./CountHint";

function Hint({ hint }: { hint: number }) {
  const [rouletteEnd, setRouletteEnd] = useState(false);

  const handleRoulette = () => {
    setRouletteEnd(!rouletteEnd);
  };

  const hintResult = () => {
    if (hint == 1) {
      return <MusicHint speed={0.7} />;
    } else if (hint == 2) {
      return <MusicHint speed={2} />;
    } else if (hint == 3) {
      return <CountHint count={3} />;
    } else {
      return <p>알파벳 오픈</p>;
    }
  };

  return <>{rouletteEnd ? hintResult() : <HintRoulette hint={hint} onRoulette={handleRoulette} />}</>;
}

export default Hint;
