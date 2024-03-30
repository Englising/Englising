const CountHint = ({ count }: { count: number }) => {
  return (
    <>
      <p className="text-2xl font-bold">오답 수</p>
      <p className="text-2xl font-bold text-secondary-500">{count}</p>
      <p className="text-lg">틀린 부분을 찾아보세요!</p>
    </>
  );
};

export default CountHint;
