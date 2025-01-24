import { useUserStore } from "@/stores/userStore";

export const userService = {
  async fetchProfile() {
    const userStore = useUserStore();
    try {
      const profile = await userStore.fetchProfile();
      return profile;
    } catch (error) {
      throw new Error("Error fetching profile: " + error.message);
    }
  },

  async updateProfile(userData) {
    const userStore = useUserStore();
    try {
      const updatedProfile = await userStore.updateProfile(userData);
      return updatedProfile;
    } catch (error) {
      throw new Error("Error updating profile: " + error.message);
    }
  },

  async changePassword(passwordData) {
    const userStore = useUserStore();
    try {
      await userStore.changePassword(passwordData);
      return true;
    } catch (error) {
      throw new Error("Error changing password: " + error.message);
    }
  },
};
