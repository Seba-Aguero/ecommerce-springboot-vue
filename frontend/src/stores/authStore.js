import { defineStore } from "pinia";
import { authService } from "@/services/authService";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    token: sessionStorage.getItem("token") || null,
    loading: false,
    tempUserData: null, // For storing temporary user data during registration
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    userEmail: (state) => (state.tempUserData?.email || (state.user ? state.user.email : "")),
    userRole: (state) => (state.user ? state.user.role : ""),
    isAdmin: (state) => state.user?.role === 'ADMIN',
    isUser: (state) => state.user?.role === 'USER',
  },

  actions: {
    setTempUserData(data) {
      this.tempUserData = data;
    },

    async login(credentials) {
      this.loading = true;
      try {
        const data = await authService.login(credentials);
        this.token = data.token;
        this.user = data.user;
        sessionStorage.setItem("token", this.token);
        return true;
      } catch (error) {
        throw new Error(error.response?.data?.message || "Login failed");
      } finally {
        this.loading = false;
      }
    },

    async register(userData) {
      this.loading = true;
      try {
        const data = await authService.register(userData);
        return true;
      } catch (error) {
        throw new Error(error.response?.data?.message || "Registration failed");
      } finally {
        this.loading = false;
      }
    },

    async logout() {
        this.user = null;
        this.token = null;
        sessionStorage.removeItem("token");
    },

    async confirmEmail(data) {
      this.loading = true;
      try {
        const response = await authService.confirmEmail(data);
        this.token = response.token;
        this.user = response.user;
        sessionStorage.setItem("token", this.token);

        // Clear temporary user data after successful confirmation
        this.tempUserData = null;

        if (!this.isAuthenticated) {
          throw new Error("Authentication failed after confirmation");
        }

        return response;
      } catch (error) {
        this.token = null;
        this.user = null;
        sessionStorage.removeItem("token");
        throw new Error(error.response?.data?.message || "Error confirming email");
      } finally {
        this.loading = false;
      }
    },

    async fetchUserProfile() {
      try {
        const data = await authService.fetchUserProfile();
        this.user = data;
      } catch (error) {
        throw error;
      }
    },
  },
});
