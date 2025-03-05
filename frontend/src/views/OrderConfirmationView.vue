<template>
  <div
    v-if="orderStore.isOrderCompleted"
    class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-16"
  >
    <div class="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-8 text-center">
      <!-- Success Icon -->
      <div
        class="mx-auto flex items-center justify-center h-16 w-16 rounded-full bg-green-100 dark:bg-green-900 mb-6"
      >
        <CheckCircle
          class="h-10 w-10 text-green-600 dark:text-green-400"
          aria-hidden="true"
        />
      </div>

      <!-- Success Message -->
      <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-4">
        Thank you for your order!
      </h1>
      <p class="text-lg text-gray-600 dark:text-gray-400 mb-8">
        Your order has been successfully placed. We'll send you an email confirmation
        shortly.
      </p>

      <!-- Order Details -->
      <div class="border-t border-b border-gray-200 dark:border-gray-700 py-6 mb-8">
        <div class="flex flex-col gap-y-2 text-gray-600 dark:text-gray-400">
          <div class="flex justify-center items-center gap-x-2">
            <Mail class="h-5 w-5" aria-hidden="true" />
            <span>Order confirmation sent to your email</span>
          </div>
          <div class="text-sm">
            <p>Order ID: #{{ orderStore.currentOrder?.id }}</p>
            <p>Total Amount: ${{ formatPrice(orderStore.currentOrder?.totalAmount) }}</p>
          </div>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="flex flex-col sm:flex-row gap-4 justify-center">
        <router-link
          to="/"
          class="inline-flex items-center justify-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700"
        >
          <Home class="h-5 w-5 mr-2" aria-hidden="true" />
          Return to Home
        </router-link>

        <router-link
          to="/products"
          title="Browse more products"
          class="inline-flex items-center justify-center px-6 py-3 border border-gray-300 dark:border-gray-600 text-base font-medium rounded-md text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700"
        >
          <ShoppingBag class="h-5 w-5 mr-2" aria-hidden="true" />
          Continue Shopping
        </router-link>
      </div>
    </div>

    <!-- Additional Information -->
    <div class="mt-8 text-center">
      <p class="text-sm text-gray-500 dark:text-gray-400">
        Need help?
        <a
          href="#"
          title="Get help with your order"
          class="text-primary-600 hover:text-primary-500 dark:text-primary-400 dark:hover:text-primary-300"
        >
          Contact our support team
        </a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useOrderStore } from '@/stores/orderStore';
import { CheckCircle, Home, ShoppingBag, Mail } from 'lucide-vue-next';
import { formatPrice } from '@/utils/formatters';

const router = useRouter();
const orderStore = useOrderStore();

onMounted(() => {
  if (!orderStore.isOrderCompleted) {
    router.push('/');
    return;
  }
});

onUnmounted(() => {
  orderStore.resetOrderStatus();
});
</script>
