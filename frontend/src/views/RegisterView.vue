<template>
  <AuthCard>
    <div class="flex items-center justify-center mb-8">
      <ShoppingBag class="h-12 w-12 text-primary-500" />
      <h1
        class="text-3xl font-bold ml-2 bg-clip-text text-transparent bg-gradient-to-r from-primary-500 to-purple-600"
      >
        My Ecommerce
      </h1>
    </div>

    <form @submit.prevent="handleRegister" class="mt-8 space-y-6">
      <div class="grid grid-cols-2 gap-4">
        <div>
          <label
            for="firstName"
            class="block text-sm font-medium text-gray-700 dark:text-gray-100 mb-1"
          >
            First Name
          </label>
          <div class="relative">
            <input
              id="firstName"
              v-model="firstName"
              type="text"
              class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              required
            />
            <User
              class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5"
            />
          </div>
        </div>
        <div>
          <label
            for="lastName"
            class="block text-sm font-medium text-gray-700 dark:text-gray-100 mb-1"
          >
            Last Name
          </label>
          <div class="relative">
            <input
              id="lastName"
              v-model="lastName"
              type="text"
              class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              required
            />
            <User
              class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5"
            />
          </div>
        </div>
      </div>

      <div>
        <label
          for="email"
          class="block text-sm font-medium text-gray-700 dark:text-gray-100 mb-1"
        >
          Email
        </label>
        <div class="relative">
          <input
            id="email"
            v-model="email"
            type="email"
            class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="tu@email.com"
            required
          />
          <Mail
            class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5"
          />
        </div>
      </div>

      <div>
        <label
          for="password"
          class="block text-sm font-medium text-gray-700 dark:text-gray-100 mb-1"
        >
          Password
        </label>
        <div class="relative">
          <input
            id="password"
            v-model="password"
            :type="showPassword ? 'text' : 'password'"
            class="w-full pl-10 pr-10 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            required
          />
          <Lock
            class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5"
          />
          <button
            type="button"
            class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600"
            @click="showPassword = !showPassword"
          >
            <Eye v-if="!showPassword" class="h-5 w-5" />
            <EyeOff v-else class="h-5 w-5" />
          </button>
        </div>
      </div>

      <div>
        <label
          for="confirmPassword"
          class="block text-sm font-medium text-gray-700 dark:text-gray-100 mb-1"
        >
          Confirm Password
        </label>
        <div class="relative">
          <input
            id="confirmPassword"
            v-model="confirmPassword"
            :type="showConfirmPassword ? 'text' : 'password'"
            class="w-full pl-10 pr-10 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            required
          />
          <Lock
            class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5"
          />
          <button
            type="button"
            class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600"
            @click="showConfirmPassword = !showConfirmPassword"
          >
            <Eye v-if="!showConfirmPassword" class="h-5 w-5" />
            <EyeOff v-else class="h-5 w-5" />
          </button>
        </div>
        <p
          v-if="password && confirmPassword && password !== confirmPassword"
          class="mt-1 text-sm text-red-500"
        >
          Passwords do not match
        </p>
      </div>

      <div class="flex items-center">
        <input
          id="terms"
          type="checkbox"
          required
          class="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
        />
        <label
          for="terms"
          class="ml-2 block text-sm text-gray-700 dark:text-gray-100"
        >
          I agree to the
          <a
            href="#"
            class="font-medium text-primary-600 hover:text-primary-800 dark:text-primary-400 dark:hover:text-primary-300"
          >
            Terms and Conditions
          </a>
        </label>
      </div>

      <div>
        <button
          type="submit"
          :disabled="
            loading ||
            (password && confirmPassword && password !== confirmPassword)
          "
          class="w-full bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 text-white font-semibold py-3 px-6 rounded-md transition duration-300 ease-in-out transform hover:scale-105 flex items-center justify-center disabled:opacity-50"
        >
          <ButtonSpinner v-if="loading"> Creating Account... </ButtonSpinner>
          <span class="flex items-center" v-else>
            Create Account
            <ArrowRight class="ml-2 h-5 w-5" />
          </span>
        </button>
      </div>

      <div class="text-center">
        <p class="mt-2 text-sm text-gray-600 dark:text-gray-300">
          Already have an account?
          <router-link
            to="/login"
            class="font-medium text-primary-600 hover:text-primary-500 dark:text-primary-400 dark:hover:text-primary-300 ml-2"
          >
            Log In
          </router-link>
        </p>
      </div>
    </form>
  </AuthCard>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import {
  ShoppingBag,
  Mail,
  Lock,
  ArrowRight,
  User,
  Eye,
  EyeOff,
} from "lucide-vue-next";
import AuthCard from "@/components/auth/AuthCard.vue";
import { useToast } from "vue-toastification";
import ButtonSpinner from "@/components/common/ButtonSpinner.vue";

const router = useRouter();
const authStore = useAuthStore();
const toast = useToast();
const firstName = ref("");
const lastName = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const loading = ref(false);
const error = ref("");
const showPassword = ref(false);
const showConfirmPassword = ref(false);

const handleRegister = async () => {
  loading.value = true;
  error.value = ""; // Reset error message
  try {
    await authStore.register({
      email: email.value,
      password: password.value,
      confirmPassword: confirmPassword.value,
    });
    router.push("/confirm-email");
  } catch (e) {
    error.value = e.message || "Registration failed";
    toast.error(error.value);
  } finally {
    loading.value = false;
  }
};
</script>
