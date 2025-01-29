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

    <form @submit.prevent="handleLogin" class="space-y-6">
      <div class="space-y-5">
        <div>
          <label
            for="email"
            class="block text-sm font-medium text-gray-900 dark:text-gray-100 mb-1"
          >
            Email
          </label>
          <div class="relative">
            <input
              id="email"
              v-model="email"
              type="email"
              required
              class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="you@example.com"
            />
            <Mail
              class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5"
            />
          </div>
        </div>

        <div>
          <label
            for="password"
            class="block text-sm font-medium text-gray-900 dark:text-gray-100 mb-1"
          >
            Password
          </label>
          <div class="relative">
            <input
              id="password"
              v-model="password"
              type="password"
              required
              class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="••••••••"
            />
            <Lock
              class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5"
            />
          </div>
        </div>
      </div>

      <div class="flex items-center justify-between mt-6">
        <div class="flex items-center">
          <input
            id="remember-me"
            type="checkbox"
            class="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
          />
          <label
            for="remember-me"
            class="ml-2 block text-sm text-gray-900 dark:text-gray-100"
          >
            Remember me
          </label>
        </div>

        <a
          href="#"
          class="text-sm font-medium text-primary-600 hover:text-primary-500 dark:text-primary-400"
        >
          Forgot your password?
        </a>
      </div>

      <button
        type="submit"
        :disabled="loading"
        class="mt-6 w-full bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 text-white font-semibold py-3 px-6 rounded-lg shadow-sm transition duration-300 ease-in-out transform hover:scale-105 flex items-center justify-center disabled:opacity-50"
      >
        <ButtonSpinner v-if="loading"> Logging in... </ButtonSpinner>
        <span class="flex items-center" v-else>
          Log In
          <ArrowRight class="ml-2 h-5 w-5" />
        </span>
      </button>

      <div class="text-center mb-8">
        <p class="mt-2 text-sm text-gray-900 dark:text-gray-100">
          Don't have an account?
          <router-link
            to="/register"
            class="font-medium text-primary-600 hover:text-primary-500 dark:text-primary-400 ml-2"
          >
            Sign Up
          </router-link>
        </p>
      </div>
    </form>
  </AuthCard>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useToast } from "vue-toastification";
import { authService } from "@/services/authService";
import { ShoppingBag, Mail, Lock, ArrowRight } from "lucide-vue-next";
import AuthCard from "@/components/auth/AuthCard.vue";
import ButtonSpinner from "@/components/common/ButtonSpinner.vue";

const router = useRouter();
const email = ref("");
const password = ref("");
const loading = ref(false);
const toast = useToast();

const handleLogin = async () => {
  loading.value = true;
  try {
    await authService.login({
      email: email.value,
      password: password.value,
    });
    toast.success("Login successful! Redirecting...", {
      timeout: 2000,
      onClose: () => {
        router.push("/");
      },
    });
  } catch (error) {
    console.error(error.message);
    toast.error(error.message);
  } finally {
    loading.value = false;
  }
};
</script>
