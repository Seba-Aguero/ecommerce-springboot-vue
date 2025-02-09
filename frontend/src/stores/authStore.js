import { defineStore } from "pinia";
import { authService } from "@/services/authService";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    token: localStorage.getItem("token") || null,
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    userEmail: (state) => (state.user ? state.user.email : ""),
    userRole: (state) => (state.user ? state.user.role : ""),
  },

  actions: {
    async login(credentials) {
      try {
        const data = await authService.login(credentials);
        this.token = data.token;
        this.user = data.user;
        localStorage.setItem("token", this.token);
        return true;
      } catch (error) {
        throw new Error(error.response?.data?.message || "Login failed");
      }
    },

    async register(userData) {
      try {
        const data = await authService.register(userData);
        this.user = data;
        return true;
      } catch (error) {
        throw new Error(error.response?.data?.message || "Registration failed");
      }
    },

    async logout() {
        this.user = null;
        this.token = null;
        localStorage.removeItem("token");
    },

    async confirmEmail(data) {
      try {
        await authService.confirmEmail(data);
        return true;
      } catch (error) {
        throw new Error(error.response?.data?.message || "Error confirming email");
      }
    },

    async resendConfirmationCode(email) {
      try {
        await authService.resendConfirmationCode(email);
        return true;
      } catch (error) {
        throw new Error(error.response?.data?.message || "Error resending code");
      }
    },

    async fetchUserProfile() {
      try {
        const data = await authService.fetchUserProfile();
        this.user = data;
      } catch (error) {
        console.error("Fetch profile error:", error);
        throw error;
      }
    },
  },
});