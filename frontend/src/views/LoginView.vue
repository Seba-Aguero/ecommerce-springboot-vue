<template>
  <AuthCard>
    <div class="flex items-center justify-center mb-8">
      <ShoppingBag class="h-12 w-12 text-primary-500" aria-hidden="true" />
      <h1
        class="text-3xl font-bold ml-2 bg-clip-text text-transparent bg-gradient-to-r from-primary-500 to-purple-600"
      >
        My Ecommerce
      </h1>
    </div>

    <form @submit.prevent="handleLogin" class="mt-8 space-y-6">
      <div>
        <Label for="email">Email</Label>
        <Input
          id="email"
          v-model="email"
          type="email"
          required
          placeholder="email@example.com"
          :icon="Mail"
        />
      </div>

      <div class="relative">
        <Label for="password">Password</Label>
        <PasswordInput id="password" v-model="password" required />
      </div>

      <div>
        <div class="mt-9">
          <button
            type="submit"
            :disabled="loading || !email || !password"
            class="w-full bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 text-white font-semibold py-3 px-6 rounded-md transition duration-300 ease-in-out transform hover:scale-105 flex items-center justify-center disabled:opacity-50"
          >
            <ButtonSpinner v-if="loading"> Logging in... </ButtonSpinner>
            <span class="flex items-center" v-else>
              Log In
              <ArrowRight class="ml-2 h-5 w-5" aria-hidden="true" />
            </span>
          </button>
        </div>

        <div class="text-center">
          <p class="mt-5 text-sm text-gray-600 dark:text-gray-300">
            Don't have an account?
            <router-link
              to="/register"
              class="font-medium text-primary-600 hover:text-primary-500 dark:text-primary-400 dark:hover:text-primary-300 ml-2"
            >
              Sign Up
            </router-link>
          </p>
        </div>
      </div>
    </form>
  </AuthCard>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import { useCartStore } from "@/stores/cartStore";
import { ShoppingBag, Mail, Lock, ArrowRight } from "lucide-vue-next";
import AuthCard from "@/components/auth/AuthCard.vue";
import Input from "@/components/common/Input.vue";
import PasswordInput from "@/components/common/PasswordInput.vue";
import Label from "@/components/common/Label.vue";
import { useToast } from "vue-toastification";
import ButtonSpinner from "@/components/common/ButtonSpinner.vue";

const router = useRouter();
const authStore = useAuthStore();
const cartStore = useCartStore();
const toast = useToast();
const email = ref("");
const password = ref("");
const loading = ref(false);

const handleLogin = async () => {
  loading.value = true;
  try {
    await authStore.login({
      email: email.value,
      password: password.value,
    });
    cartStore.setUserId();
    router.push("/");
    toast.success("Successfully logged in!");
  } catch (error) {
    toast.error("Invalid email or password");
  } finally {
    loading.value = false;
  }
};
</script>
