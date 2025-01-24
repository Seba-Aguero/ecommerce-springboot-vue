import { useAuthStore } from "@/stores/authStore";
import api from "@/services/api";

export const authService = {
  async login(credentials) {
    const authStore = useAuthStore();
    try {
      await authStore.login(credentials);
      return true;
    } catch (error) {
      throw new Error("Invalid credentials");
    }
  },

  async register(userData) {
    const authStore = useAuthStore();
    try {
      await authStore.register(userData);
      return true;
    } catch (error) {
      throw error;
    }
  },

  async logout() {
    const authStore = useAuthStore();
    try {
      await authStore.logout();
    } catch (error) {
      throw new Error("Error during logout: " + error.message);
    }
  },

  async confirmEmail(data) {
    console.log(data);
    const authStore = useAuthStore();
    try {
      await api.post("/api/v1/auth/confirm-email", data);
      return true;
    } catch (error) {
      throw new Error(
        error.response?.data?.message || "Error confirming email"
      );
    }
  },

  async resendConfirmationCode(email) {
    try {
      await api.post("/api/v1/auth/resend-confirmation", { email });
      return true;
    } catch (error) {
      throw new Error(error.response?.data?.message || "Error resending code");
    }
  },
};
