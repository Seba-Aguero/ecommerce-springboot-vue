import { defineStore } from "pinia";
import api from "@/services/api";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    token: localStorage.getItem("token"),
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    userEmail: (state) => state.user ? state.user.email : "",
    userRole: (state) => state.user ? state.user.role : "",
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
      try {
        const response = await api.post("/api/v1/auth/register", userData);
        // The backend returns UserDto which contains id, email and role
        this.user = response.data;

        // Need to login after registration to get the token
        await this.login({
          email: userData.email,
          password: userData.password
        });

        return true;
      } catch (error) {
        console.error("Register error:", error);
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
