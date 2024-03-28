import axios, {AxiosResponse, AxiosError } from "axios";
import { SingleData } from "../pages/SinglePage";
// export const BASE_URL = "http://localhost:8080";

const BASE_URL = "https://j10a106.p.ssafy.io/api";

//SinglePlay 시작
export interface SinglePlayData {
  trackId: number,
  level: number
}

export const getSinglePlayData = async (data: SinglePlayData): Promise<any> => {
    try {
        const response = await axios.post(`${BASE_URL}/singleplay`, data);
        console.log(response, "Success getSinglePlayData");
        return response.data; 
    } catch (error) {
        console.error(error, "Fail");
        throw error; 
    }
};

export const getStartimeData =async (trackId:number): Promise<any> => {
    try {
        const response = await axios.get(`${BASE_URL}/singleplay/track/${trackId}`);
        console.log(response, "Success getStartimeData");
        return response.data; 
    } catch (error) {
        console.error(error, "Fail")
        throw error;
    }
}

