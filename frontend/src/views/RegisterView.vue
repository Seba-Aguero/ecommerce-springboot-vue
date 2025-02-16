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

    <form @submit.prevent="handleRegister" class="mt-8 space-y-6">
      <div class="grid grid-cols-2 gap-4">
        <div>
          <Label for="firstName">First Name</Label>
          <Input id="firstName" v-model="firstName" type="text" required :icon="User" />
        </div>
        <div>
          <Label for="lastName">Last Name</Label>
          <Input id="lastName" v-model="lastName" type="text" required :icon="User" />
        </div>
      </div>

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

      <div>
        <Label for="password">Password</Label>
        <PasswordInput
          id="password"
          v-model="password"
          required
          placeholder="Enter your password"
        />
      </div>

      <div>
        <Label for="confirmPassword">Confirm Password</Label>
        <PasswordInput
          id="confirmPassword"
          v-model="confirmPassword"
          required
          placeholder="Confirm your password"
        />
      </div>

      <div>
        <div class="mt-9">
          <button
            type="submit"
            :disabled="
              loading || (password && confirmPassword && password !== confirmPassword)
            "
            class="w-full bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 text-white font-semibold py-3 px-6 rounded-md transition duration-300 ease-in-out transform hover:scale-105 flex items-center justify-center disabled:opacity-50"
          >
            <ButtonSpinner v-if="loading"> Creating Account... </ButtonSpinner>
            <span class="flex items-center" v-else>
              Create Account
              <ArrowRight class="ml-2 h-5 w-5" aria-hidden="true" />
            </span>
          </button>
        </div>

        <div class="text-center">
          <p class="mt-5 text-sm text-gray-600 dark:text-gray-300">
            Already have an account?
            <router-link
              to="/login"
              class="font-medium text-primary-600 hover:text-primary-500 dark:text-primary-400 dark:hover:text-primary-300 ml-2"
            >
              Log In
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
import { ShoppingBag, Mail, ArrowRight, User } from "lucide-vue-next";
import AuthCard from "@/components/auth/AuthCard.vue";
import Input from "@/components/common/Input.vue";
import PasswordInput from "@/components/common/PasswordInput.vue";
import Label from "@/components/common/Label.vue";
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
