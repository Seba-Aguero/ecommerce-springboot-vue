<template>
  <AuthCard>
    <div class="flex flex-col items-center justify-center space-y-6">
      <!-- Icon and Title -->
      <div class="text-center">
        <Mail class="h-16 w-16 text-primary-500 mx-auto mb-4" />
        <h1 class="text-2xl font-bold text-gray-900 dark:text-white">
          Confirm your email
        </h1>
      </div>

      <!-- Message -->
      <div class="text-center space-y-4">
        <p class="text-gray-600 dark:text-gray-300">
          We've sent a confirmation code to
          <span class="font-medium text-gray-900 dark:text-white">{{
            email
          }}</span>
        </p>
      </div>

      <!-- Confirmation Code Form -->
      <form
        @submit.prevent="handleConfirmation"
        class="w-full max-w-sm space-y-4"
      >
        <div>
          <label
            for="confirmationCode"
            class="block text-sm font-medium text-gray-700 dark:text-gray-200 mb-1"
          >
            Confirmation Code
          </label>
          <div class="relative">
            <input
              id="confirmationCode"
              v-model="confirmationCode"
              type="text"
              maxlength="6"
              class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white text-center text-lg tracking-widest"
              placeholder="000000"
              required
            />
            <KeyRound
              class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5"
            />
          </div>
        </div>

        <!-- Error Message -->
        <div v-if="error" class="text-red-500 text-sm text-center">
          {{ error }}
        </div>

        <!-- Submit Button -->
        <button
          type="submit"
          :disabled="loading || !confirmationCode"
          class="w-full bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 text-white font-semibold py-2 px-4 rounded-md transition duration-300 ease-in-out transform hover:scale-105 flex items-center justify-center disabled:opacity-50"
        >
          <svg
            v-if="loading"
            class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              class="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              stroke-width="4"
            />
            <path
              class="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
            />
          </svg>
          {{ loading ? "Verifying..." : "Verify Email" }}
        </button>

        <!-- Resend Code -->
        <div class="text-center">
          <button
            type="button"
            @click="handleResendCode"
            :disabled="loading"
            class="text-sm text-primary-600 hover:text-primary-500 dark:text-primary-400 dark:hover:text-primary-300"
          >
            Didn't receive the code? Resend
          </button>
        </div>
      </form>
    </div>
  </AuthCard>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import { Mail, KeyRound } from "lucide-vue-next";
import AuthCard from "@/components/auth/AuthCard.vue";
import { authService } from "@/services/authService";
import { useToast } from "vue-toastification";

const router = useRouter();
const toast = useToast();
const authStore = useAuthStore();
const email = ref(authStore.userEmail);
const password = ref(sessionStorage.getItem("temp_password"));
const confirmationCode = ref("");
const loading = ref(false);
const error = ref("");

const handleConfirmation = async () => {
  loading.value = true;
  error.value = "";

  try {
    await authService.confirmEmail({
      email: email.value,
      confirmationCode: confirmationCode.value,
      password: password.value,
    });

    // If the confirmation was successful, then log in
    await authService.login({
      email: email.value,
      password: password.value,
    });

    toast.success("Email confirmed successfully! Redirecting...", {
      timeout: 2000,
      onClose: () => {
        router.push("/");
      },
    });
  } catch (err) {
    error.value = err.message || "Invalid confirmation code";
    toast.error(error.value);
  } finally {
    loading.value = false;
    // Clear the temporary password after using it
    sessionStorage.removeItem("temp_password");
  }
};

const handleResendCode = async () => {
  loading.value = true;
  error.value = "";

  try {
    await authService.resendConfirmationCode(email.value);
    toast.info("New code sent to your email");
  } catch (err) {
    error.value = err.message || "Error sending confirmation code";
    toast.error(error.value);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  if (!email.value || !password.value) {
    router.push("/register");
  }
});
</script>
