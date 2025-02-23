<template>
  <AuthCard>
    <div class="flex flex-col items-center justify-center space-y-6">
      <!-- Icon and Title -->
      <div class="text-center">
        <Mail class="h-16 w-16 text-primary-500 mx-auto mb-4" aria-hidden="true" />
        <h1 class="text-2xl font-bold text-gray-900 dark:text-white">
          Confirm your email
        </h1>
      </div>

      <!-- Message -->
      <div class="text-center space-y-4">
        <p class="text-gray-600 dark:text-gray-300">
          We've sent a confirmation code to
          <span class="font-medium text-gray-900 dark:text-white">{{ email }}</span>
        </p>
      </div>

      <!-- Confirmation Code Form -->
      <form
        @submit.prevent="handleConfirmation"
        class="w-full max-w-sm space-y-4"
        aria-label="Confirmation Code Form"
      >
        <div>
          <Label for="confirmationCode">Confirmation Code</Label>
          <Input
            id="confirmationCode"
            v-model="confirmationCode"
            type="text"
            maxlength="6"
            placeholder="000000"
            required
            :icon="KeyRound"
          />
        </div>

        <!-- Error Message -->
        <div v-if="error" class="text-red-500 text-sm text-center">
          {{ error }}
        </div>

        <!-- Submit Button -->
        <button
          type="submit"
          :disabled="authStore.loading || !confirmationCode"
          class="w-full bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 text-white font-semibold py-2 px-4 rounded-md ease-in-out transform hover:scale-105 flex items-center justify-center disabled:opacity-50"
        >
          <ButtonSpinner v-if="authStore.loading"> Confirming... </ButtonSpinner>
          <span v-else class="flex items-center">
            Confirm Email
            <ArrowRight class="ml-2 h-5 w-5" aria-hidden="true" />
          </span>
        </button>
      </form>
    </div>
  </AuthCard>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import { useCartStore } from "@/stores/cartStore";
import { Mail, KeyRound, ArrowRight } from "lucide-vue-next";
import AuthCard from "@/components/auth/AuthCard.vue";
import Input from "@/components/common/Input.vue";
import Label from "@/components/common/Label.vue";
import { useToast } from "vue-toastification";
import ButtonSpinner from "@/components/common/ButtonSpinner.vue";

const router = useRouter();
const toast = useToast();
const authStore = useAuthStore();
const cartStore = useCartStore();
const email = computed(() => authStore.userEmail);
const password = computed(() => authStore.tempUserData?.password);
const confirmationCode = ref("");
const error = ref("");

const handleConfirmation = async () => {
  error.value = "";

  try {
    const response = await authStore.confirmEmail({
      email: email.value,
      confirmationCode: confirmationCode.value,
      password: password.value,
    });

    if (!authStore.isAuthenticated) {
      throw new Error("Authentication failed after confirmation");
    }

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
  }
};

onMounted(() => {
  // Redirect if email or temporary password is missing from the authStore
  if (!email.value || !password.value) {
    router.push("/register");
  }
});
</script>
