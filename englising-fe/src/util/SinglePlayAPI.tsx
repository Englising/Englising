import axios from "axios";


const BASE_URL = "https://j10a106.p.ssafy.io/api";

//SinglePlay 시작
export interface SinglePlayData {
  trackId: number,
  level: number
}

export interface SinglePlayResultData {
    singlePlayId: number
}

export interface SinglePlayWordCheck {
  singleplayId: number,
  singleplayWordId: number,
  word: string
}

export interface SinglePlayWordLike {
    wordId: number
}

export const getSinglePlayData = async (data: SinglePlayData): Promise<any> => {
    try {
        const response = await axios.post(`${BASE_URL}/singleplay`, data, {withCredentials:true});
        console.log(response, "Success getSinglePlayData");
        return response.data; 
    } catch (error) {
        console.error(error, "Fail getSinglePlayData");
        throw error; 
    }
};

export const getStartimeData =async (trackId:number): Promise<any> => {
    try {
        const response = await axios.get(`${BASE_URL}/singleplay/track/${trackId}`, {withCredentials:true});
        console.log(response, "Success getStartimeData");
        return response.data; 
    } catch (error) {
        console.error(error, "Fail getStartimeData")
        throw error;
    }
}

export const getSinglePlayResultData = async (data: SinglePlayResultData): Promise<any> => {
    try {
        const response = await axios.post(`${BASE_URL}/singleplay/result`, data, {withCredentials:true});
        console.log(response, "Success getSinglePlayResultData");
        return response.data; 
    } catch (error) {
        console.error(error, "Fail getSinglePlayResultData")
        throw error;
    } 
}

export const  singlePlayWordCheck =async (data:SinglePlayWordCheck) => {
    try {
        const response = await axios.post(`${BASE_URL}/singleplay/word-check`, data, {withCredentials:true});
        console.log(response, "Success getSinglePlayResultData");
    }catch (error) {
        console.error(error, "Fail singlePlayWordCheck")
    }
}

export const singlePlayWordLike =async (data: SinglePlayWordLike) => {
    try {
        const response = await axios.post(`${BASE_URL}/word/like`, data, {withCredentials:true});
        console.log(response, "Success singlePlayWordLike");
    }catch (error) {
        console.error(error, "Fail singlePlayWordLike")
    }
}
