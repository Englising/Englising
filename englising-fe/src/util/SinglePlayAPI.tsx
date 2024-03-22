import axios, {AxiosResponse, AxiosError } from "axios";

// export const BASE_URL = "http://localhost:8080";

const BASE_URL = "https://j10a106.p.ssafy.io/";

//SinglePlay 시작
export interface SinglePlayData {
  trackId: number,
  level: number
}

export const getSinglePlayData = (data: SinglePlayData) => {
    axios.post(`${BASE_URL}/singleplay`, data)
    .then((response: AxiosResponse) => {
        console.log(response,"Success")
    }).catch((error: AxiosError) => {
        console.log(error,"Fail")
    })
}

export const getAllBuilding = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/api/user/building/selectall`);
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error(error);
    }
  };

// a미정
export const getRestRoom = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/api/user/restroom/select`,{
        params: {
            buildingId: buildingId
        }
      });
      console.log(response);
      return response.data;
    } catch (error) {
      console.error("요청 실패", error);
    }
  };

// Post
export const getStall = async (restroomId) => {
    try {
      const response = await axios.get(`${BASE_URL}/api/api/tmptest1`,{
        params: {
            restroomId: restroomId
        }
      });
      console.log(response);
      return response.data;
    } catch (error) {
      console.error(error);
    }
  };

// Update
export const getCongestion = async (buildingId) => {
  try {
    //console.log(buildingId,"axios에 빌딩 id 잘 넘어오는중")
    const response = await axios.get(`${BASE_URL}/api/api/tmptest2`,{
      params: {
          buildingId: buildingId
      }
    });
    console.log(response);
    return response.data;
  } catch (error) {
    console.error(error);
  }
}

// Delete
export const createReportData = (data) => {
    axios.post(`${BASE_URL}/api/user/reports/regist`, data, {headers: { 'Authorization': `BEARER ${localStorage.getItem("UserToken")}`}})
    .then((res) => {
        console.log("신고 제출 성공")
    }).catch((e) => {
        console.log(e,"신고 제출 실패")
    })
}



// 회원가입
export const createUser = async (data) => {
  try {
    const response = await axios.post(`${BASE_URL}/register`,data);
    console.log(response);
  } catch (error) {
    console.log(error);
  }
}

// 로그인
export const userLogIn = async (data) => {
  try {
    //console.log(buildingId,"axios에 빌딩 id 잘 넘어오는중")
    const response = await axios.post(`${BASE_URL}/login`,data);
    console.log(response.data, "Token");
    return response.data;
  } catch (error) {
    console.log(error);
  }
}