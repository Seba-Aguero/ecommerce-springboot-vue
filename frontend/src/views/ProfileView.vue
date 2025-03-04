<template>
  <div class="max-w-4xl mx-auto px-4 py-8">
    <h1 class="text-2xl font-bold mb-8 text-gray-900 dark:text-white">My Profile</h1>

    <form @submit.prevent="handleSubmit" class="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <Label for="firstName">First Name</Label>
          <Input
            id="firstName"
            v-model="profileData.firstName"
            type="text"
            required
            :icon="User"
          />
        </div>

        <div>
          <Label for="lastName">Last Name</Label>
          <Input
            id="lastName"
            v-model="profileData.lastName"
            type="text"
            required
            :icon="User"
          />
        </div>

        <div>
          <Label for="email">Email</Label>
          <Input
            id="email"
            v-model="profileData.email"
            type="email"
            required
            :icon="Mail"
          />
        </div>

        <div>
          <Label for="phone">Phone</Label>
          <Input
            id="phone"
            v-model="profileData.phone"
            type="tel"
            :icon="Phone"
          />
        </div>

        <div class="md:col-span-2">
          <Label for="address">Address</Label>
          <Input
            id="address"
            v-model="profileData.address"
            type="text"
            :icon="MapPin"
          />
        </div>
      </div>

      <div class="mt-6 flex justify-end">
        <button
          type="submit"
          :disabled="loading"
          class="bg-primary-600 text-white px-6 py-2 rounded-md hover:bg-primary-700 flex items-center"
        >
          <ButtonSpinner v-if="loading">Saving...</ButtonSpinner>
          <span v-else>Save Changes</span>
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { User, Mail, Phone, MapPin } from 'lucide-vue-next';
import Input from '@/components/common/Input.vue';
import Label from '@/components/common/Label.vue';
import ButtonSpinner from '@/components/common/ButtonSpinner.vue';
import { useToast } from 'vue-toastification';

const userStore = useUserStore();
const toast = useToast();
const loading = ref(false);

const profileData = ref({
  firstName: '',
  lastName: '',
  email: '',
  phone: '',
  address: ''
});

onMounted(async () => {
  try {
    const userData = await userStore.fetchProfile();
    profileData.value = { ...userData };
  } catch (error) {
    toast.error('Failed to load profile data');
  }
});

const handleSubmit = async () => {
  loading.value = true;
  try {
    await userStore.updateProfile(profileData.value);
    toast.success('Profile updated successfully');
  } catch (error) {
    toast.error(error.message || 'Failed to update profile');
  } finally {
    loading.value = false;
  }
};
</script>
