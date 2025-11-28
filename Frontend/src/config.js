import axios from "axios";

export const API_BASE_URL = "http://localhost:2060/api";

export const ENDPOINTS = {
  login: `${API_BASE_URL}/auth/login`,
  register: `${API_BASE_URL}/auth/register`,
  updateUser: `${API_BASE_URL}/auth/update`,
  bookings: `${API_BASE_URL}/bookings`,
};

// ✅ Login API
export const loginUser = async ({ email, password }) => {
  return await axios.post(ENDPOINTS.login, { email, password });
};

// ✅ Register API
export const registerUser = async (data) => {
  return await axios.post(ENDPOINTS.register, data);
};
