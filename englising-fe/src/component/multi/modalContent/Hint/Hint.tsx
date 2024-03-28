import HintRoulette from "./HintRoulette";

function Hint({ hint }: { hint: number }) {
  return (
    <>
      <HintRoulette hint={hint} />
    </>
  );
}

export default Hint;
