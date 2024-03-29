import axios from "axios";

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  withCredentials: true,
});

const getMultiplayResult = (multiplayId: string) => {
  return instance.get(`/multiplay/${multiplayId}/result`);
};

const getMultiplayInfo = (multiplayId: string) => {
  return instance.get(`/multiplay/${multiplayId}`);
};

export { getMultiplayResult, getMultiplayInfo };
