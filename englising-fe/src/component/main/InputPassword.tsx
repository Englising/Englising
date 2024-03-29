import React, { useState } from 'react';
import axios from 'axios';
interface InputPasswordProps {
    onClose: () => void;
    correctPassword: number;
    roomId: number;
}

const InputPassword: React.FC<InputPasswordProps> = ({ onClose, correctPassword, roomId }) => {
    const [password, setPassword] = useState<number>(0);

    const handleSubmit = () => {
        if (password === correctPassword) {
            onClose(); // 비밀번호가 일치하면 모달을 닫음
            axios.post(`https://j10a106.p.ssafy.io/api/multiplay/${roomId}/game`, {withCredentials:true})
            .then(() => {
                window.location.href = `/waitroom/${roomId}`;
            })
            .catch((error) => {
                if (error.response && error.response.request.status === 404) {
                    alert("참여할 수 없는 방입니다.");
                } else {
                    console.error('참여 실패', error);
                }
            });
        } else {
            alert('비밀번호가 올바르지 않습니다.'); // 비밀번호가 일치하지 않으면 에러 메시지 설정
        }
    };

    return (
        <div>
            <div className="fixed top-0 left-0 w-full h-full bg-black opacity-50 z-50"></div>
            <div className="fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-primary-700 p-6 rounded-lg shadow-lg z-50">
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-lg font-bold text-white">비밀번호를 입력하세요</h2>
                    <button className="text-black" onClick={onClose}>X</button>
                </div>
                <input
                    type="number"
                    value={password}
                    onChange={(e) => setPassword(parseInt(e.target.value))}
                    placeholder="Password"
                    className="w-full h-10 px-3 rounded-lg border border-gray-300 focus:outline-none focus:border-primary-500 text-black"
                />
                <div className="flex justify-end mt-4">
                    <button
                        className="bg-secondary-500 text-black px-4 py-2 rounded-lg"
                        onClick={handleSubmit}
                    >
                        입력
                    </button>
                </div>
            </div>
        </div>
    );
};

export default InputPassword;
