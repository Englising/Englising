const useSpeek = (word: string): void => {
    const voicesChangedHandler = () => {
        const voices: SpeechSynthesisVoice[] = speechSynthesis.getVoices();
        
        // 목소리를 설정하고 발화
        const utter: any = new SpeechSynthesisUtterance(word);
        utter.voice = voices[2]; 
        speechSynthesis.speak(utter);

        // 이벤트 핸들러 제거
        speechSynthesis.onvoiceschanged = null;
    };

    // 이벤트 핸들러 등록
    speechSynthesis.onvoiceschanged = voicesChangedHandler;

    // 현재 목소리를 가져와서 발화
    const voices: SpeechSynthesisVoice[] = speechSynthesis.getVoices();
    if (voices.length > 0) {
        const utter: any = new SpeechSynthesisUtterance(word);
        utter.voice = voices[2]; 
        speechSynthesis.speak(utter);
    }
};

export default useSpeek;