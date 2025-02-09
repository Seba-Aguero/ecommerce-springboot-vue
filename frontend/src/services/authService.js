import api from "@/services/api";

export const authService = {
  async login(credentials) {
    const response = await api.post("/api/v1/auth/login", credentials);
    return response.data;
  },

  async register(userData) {
    const response = await api.post("/api/v1/auth/register", userData);
    return response.data;
  },

  async confirmEmail(data) {
    await api.post("/api/v1/auth/confirm-email", data);
    return true;
  },

  async resendConfirmationCode(email) {
    await api.post("/api/v1/auth/resend-confirmation", { email });
    return true;
  },

  async fetchUserProfile() {
    const response = await api.get("/api/v1/users/profile");
    return response.data;
  },
};