<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
    <h1 class="text-2xl font-bold mb-8 text-gray-900 dark:text-white">My Orders</h1>

    <!-- Loading State -->
    <LoadingSpinner v-if="orderStore.loading" />

    <!-- Error State -->
    <div
      v-else-if="orderStore.error"
      class="text-center text-red-500 dark:text-red-400 py-8"
    >
      {{ orderStore.error }}
    </div>

    <!-- Empty State -->
    <EmptyState
      v-else-if="!orderStore.orders.length"
      title="No orders yet"
      description="Start shopping to see your orders here"
    />

    <!-- Orders List -->
    <div v-else class="space-y-6">
      <div
        v-for="order in orderStore.orders"
        :key="order.id"
        class="bg-white dark:bg-gray-800 shadow rounded-lg overflow-hidden"
      >
        <!-- Order Header -->
        <div class="px-6 py-3 border-b border-gray-200 dark:border-gray-700">
          <div class="flex items-center justify-between space-x-4">
            <div>
              <h3 class="text-lg font-medium text-gray-900 dark:text-white">
                Order #{{ order.id }}
              </h3>
              <p class="text-sm text-gray-500 dark:text-gray-400">
                Placed on {{ formatDate(order.createdAt) }}
              </p>
            </div>
            <OrderStatusBadge :status="order.status" />
          </div>
        </div>

        <!-- Order Items -->
        <div class="px-6 py-3">
          <LoadingSpinner v-if="orderStore.loadingItems" />
          <div v-else class="space-y-4">
            <div
              v-for="item in orderStore.getItemsForOrder(order.id)"
              :key="item.id"
              class="flex items-center space-x-4"
            >
              <img
                :src="item.product.imageUrl"
                :alt="item.product.name"
                class="w-16 h-16 object-cover rounded"
                v-image-fallback
              />
              <div class="flex-1">
                <h4 class="text-sm font-medium text-gray-900 dark:text-white">
                  {{ item.product.name }}
                </h4>
                <p class="mt-1 text-sm text-gray-500 dark:text-gray-400">
                  Quantity: {{ item.quantity }}
                </p>
              </div>
              <div class="text-sm text-gray-900 dark:text-white">
                ${{ formatPrice(item.price) }}
              </div>
            </div>
          </div>
        </div>

        <!-- Order Footer -->
        <div
          class="px-6 py-3 bg-gray-50 dark:bg-gray-700 flex items-center justify-between"
        >
          <div class="items-center">
            <div class="text-sm text-gray-600 dark:text-gray-300">
              <span class="font-medium">Shipping Address:</span>
              {{ order.address }}
            </div>
            <div class="text-sm text-gray-600 dark:text-gray-300">
              <span class="font-medium">Shipping Cost:</span>
              ${{ formatPrice(shippingCost) }}
            </div>
          </div>
          <div class="text-right pl-4 text-nowrap">
            <div class="text-gray-600 dark:text-gray-300">Total Amount</div>
            <div class="text-xl font-semibold text-gray-900 dark:text-white">
              ${{ formatPrice(order.totalAmount) }}
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useOrderStore } from "@/stores/orderStore";
import { useAuthStore } from "@/stores/authStore";
import { useCartStore } from "@/stores/cartStore";
import { useToast } from "vue-toastification";
import { formatPrice } from "@/utils/formatters";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import EmptyState from "@/components/common/EmptyState.vue";
import OrderStatusBadge from "@/components/orders/OrderStatusBadge.vue";
import { useShippingCost } from "@/composables/useShippingCost";

const orderStore = useOrderStore();
const authStore = useAuthStore();
const toast = useToast();
const { shippingCost } = useShippingCost();

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
};

onMounted(async () => {
  if (authStore.isAuthenticated) {
    try {
      await orderStore.fetchUserOrders(authStore.user.id);
      // Fetch order items for each order
      for (const order of orderStore.orders) {
        await orderStore.fetchOrderItems(order.id);
      }
    } catch (error) {
      toast.error("Failed to load orders");
    }
  }
});
</script>
