import { defineStore } from 'pinia';
import { userService } from '@/services/userService';

export const useUserStore = defineStore('user', {
  state: () => ({
    profile: null,
    loading: false,
  }),

  actions: {
    async fetchProfile() {
      this.loading = true;
      try {
        const response = await userService.fetchProfile();
        this.profile = response;
        return response;
      } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to fetch profile');
      } finally {
        this.loading = false;
      }
    },

    async updateProfile(profileData) {
      this.loading = true;
      try {
        const response = await userService.updateProfile(profileData);
        this.profile = response;
        return response;
      } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to update profile');
      } finally {
        this.loading = false;
      }
    },
  },
});