import React from 'react';

interface Props {
    total: number,
    target: number,
    msg: string
}
const DonutChart = ({ total, target, msg }: Props) => {

  // 비율 계산
  const targetRatio = (target / total) * 100;

  // 원의 반지름
  const radius = 25;
  // 원의 둘레
  const circumference = radius * 2 * Math.PI;
  // target 부분의 둘레 값을 계산합니다.
  const dash = circumference * 0.9;

  return (
    <svg className="w-96 h-96">
      <circle
        cx="50" // 원의 중심의 x좌표를 나타냅니다.
        cy="50" // 원의 중심의 y좌표를 나타냅니다.
              r={radius}  // 원의 반지름을 나타냅니다.
        fill="red" // 원의 내부를 채우는 색상을 나타냅니다.
        stroke="blue" // 원의 테두리 색상을 나타냅니다.
        strokeWidth="10" // 원의 테두리의 너비를 나타냅니다.
        strokeDasharray= {`${dash} ${circumference-dash}`} // 선 스타일을 나타냅니다. "선, 공백"의 형태로 순서대로 그립니다.
        strokeDashoffset={0} // 선의 시작 위치를 나타냅니다.
        transform="" // 요소의 변환을 지정합니다.
      />


    </svg>
  );
};

export default DonutChart;