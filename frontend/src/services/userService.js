import api from '@/services/api';

export const userService = {
  async fetchProfile() {
    const response = await api.get('/api/v1/users/profile');
    return response.data;
  },

  async updateProfile(profileData) {
    const response = await api.put('/api/v1/users/profile', profileData);
    return response.data;
  },
};
