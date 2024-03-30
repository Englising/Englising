import axios from "../../node_modules/axios/index";

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  withCredentials: true,
});

const getUserInfo = () => {
  return instance.get(`user/profile`);
};

export { getUserInfo };
