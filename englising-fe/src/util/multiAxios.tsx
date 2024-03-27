import axios from "axios";

const instance = axios.create({
  baseURL: "https://j10a106.p.ssafy.io/",
});

const getMultiUsers = (multiplayId: number) => {
  return instance.get(`/multiplay/${multiplayId}`);
};

const getMultiResult = (multiplayId: number) => {
  return instance.get(`/multiplay/${multiplayId}/result`);
};

export { getMultiUsers, getMultiResult };
