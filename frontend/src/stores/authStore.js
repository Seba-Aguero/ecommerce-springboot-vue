import { defineStore } from "pinia";
import api from "@/services/api";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    token: localStorage.getItem("token"),
  }),

  getters: {
    isAuthenticated: (state) => (state.token ? true : false),
    userEmail: (state) => (state.user ? state.user.email : ""),
    userRole: (state) => (state.user ? state.user.role : ""),
  },

  actions: {
    async login(credentials) {
      try {
        const response = await api.post("/api/v1/auth/login", credentials);
        this.token = response.data.token;
        this.user = response.data.user;
        localStorage.setItem("token", this.token);
        return true;
      } catch (error) {
        console.error("Login error:", error);
        throw error;
      }
    },

    async register(userData) {
      console.log("Sending registration data:", userData);
      try {
        const response = await api.post("/api/v1/auth/register", userData);
        this.user = response.data;
        sessionStorage.setItem("temp_password", userData.password);
        return true;
      } catch (error) {
        if (error.response?.data?.message) {
          throw new Error(error.response.data.message);
        }
        throw error;
      }
    },

    logout() {
      this.user = null;
      this.token = null;
      localStorage.removeItem("token");
    },

    async fetchUserProfile() {
      try {
        const response = await api.get("/users/profile");
        this.user = response.data;
      } catch (error) {
        console.error("Fetch profile error:", error);
        throw error;
      }
    },
  },
});
