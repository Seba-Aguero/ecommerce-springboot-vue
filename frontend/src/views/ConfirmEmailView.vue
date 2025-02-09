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
        aria-label="Confirmation Code Form"
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
          class="w-full bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 text-white font-semibold py-2 px-4 rounded-md ease-in-out transform hover:scale-105 flex items-center justify-center disabled:opacity-50"
        >
          <ButtonSpinner v-if="loading"> Verifying... </ButtonSpinner>
          <span v-else> Verify Email </span>
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
import { useCartStore } from "@/stores/cartStore";
import { Mail, KeyRound } from "lucide-vue-next";
import AuthCard from "@/components/auth/AuthCard.vue";
import { useToast } from "vue-toastification";
import ButtonSpinner from "@/components/common/ButtonSpinner.vue";

const router = useRouter();
const toast = useToast();
const authStore = useAuthStore();
const cartStore = useCartStore();
const email = ref(authStore.userEmail);
const password = ref(authStore.tempUserData?.password);
const confirmationCode = ref("");
const loading = ref(false);
const error = ref("");

const handleConfirmation = async () => {
  loading.value = true;
  error.value = "";

  try {
    await authStore.confirmEmail({
      email: email.value,
      confirmationCode: confirmationCode.value,
      password: password.value,
    });

    // If the confirmation was successful, then log in using temporary password
    await authStore.login({
      email: email.value,
      password: password.value,
    });

    cartStore.setUserId();

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
    // Clear the temporary password from the authStore after using it
    authStore.tempUserData = null;
  }
};

const handleResendCode = async () => {
  loading.value = true;
  error.value = "";

  try {
    await authStore.resendConfirmationCode(email.value);
    toast.info("New code sent to your email");
  } catch (err) {
    error.value = err.message || "Error sending confirmation code";
    toast.error(error.value);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  // Redirect if email or temporary password is missing from the authStore
  if (!email.value || !password.value) {
    router.push("/register");
  }
});
</script>
