<template>
  <!-- Dark background -->
  <div
    v-if="isOpen"
    class="fixed inset-0 bg-black bg-opacity-50 z-40"
    @click="$emit('close')"
  ></div>

  <div
    v-if="isOpen"
    class="fixed inset-y-0 right-0 w-80 md:w-96 bg-white dark:bg-gray-800 shadow-xl z-50"
  >
    <!-- Header -->
    <div class="p-4 border-b dark:border-gray-700 flex justify-between items-center">
      <h2 class="text-lg font-semibold text-gray-900 dark:text-white">Shopping Cart</h2>
      <button
        @click="$emit('close')"
        class="text-gray-500 hover:text-gray-700 dark:hover:text-gray-300 transition duration-0"
      >
        <X class="h-6 w-6" />
      </button>
    </div>

    <!-- Cart Items -->
    <div class="px-4 overflow-y-auto h-[calc(100vh-180px)] custom-scrollbar">
      <div
        v-if="!cartStore.items.length"
        class="text-center text-gray-500 dark:text-gray-400 mt-4"
      >
        Your cart is empty!
      </div>

      <div v-else>
        <div
          v-for="item in cartStore.items"
          :key="item.id"
          class="flex items-center gap-4 py-3 border-b dark:border-gray-700"
        >
          <img
            :src="item.imageUrl"
            :alt="item.name"
            class="w-16 h-16 object-cover rounded"
          />
          <div class="flex-1">
            <h3 class="text-sm font-medium text-gray-900 dark:text-white">
              {{ item.name }}
            </h3>
            <div class="flex items-center mt-2">
              <div
                class="flex items-center gap-3 border rounded-md px-2 py-1 border-gray-600"
              >
                <button
                  @click="handleDecrementQuantity(item.id)"
                  :disabled="item.quantity <= 1"
                  class="text-gray-500 dark:text-gray-300 hover:text-gray-800 dark:hover:text-gray-50 duration-0 disabled:opacity-40"
                >
                  <Minus class="h-4 w-4" />
                </button>
                <span class="text-sm text-gray-700 dark:text-gray-300">{{
                  item.quantity
                }}</span>
                <button
                  @click="handleIncrementQuantity(item.id)"
                  :disabled="item.quantity >= item.totalStock"
                  class="text-gray-500 dark:text-gray-300 hover:text-gray-800 dark:hover:text-gray-50 duration-0 disabled:opacity-40"
                >
                  <Plus class="h-4 w-4" />
                </button>
              </div>
            </div>
          </div>
          <div class="flex flex-col">
            <div class="flex justify-end">
              <button
                @click="handleRemoveFromCart(item.id)"
                class="text-red-500 hover:text-red-400 duration-0"
              >
                <Trash2 class="h-4 w-4" />
              </button>
            </div>
            <div class="py-1">
              <p class="font-semibold text-gray-500 dark:text-gray-300 mt-2">
                ${{ (item.price * item.quantity).toFixed(2) }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Total and footer buttons -->
    <div
      class="absolute bottom-0 w-full p-4 border-t dark:border-gray-700 bg-white dark:bg-gray-800 flex flex-col gap-y-3"
    >
      <div class="flex justify-between">
        <span class="font-semibold text-gray-600 dark:text-gray-300">Total:</span>
        <span class="font-bold text-gray-900 dark:text-white">
          ${{ cartStore.totalAmount.toFixed(2) }}
        </span>
      </div>
      <div class="flex gap-x-2">
        <button
          @click="proceedToCheckout"
          class="bg-primary-600 text-white w-[70%] md:w-3/4 px-2 py-1 md:px-4 md:py-2 rounded hover:bg-primary-700 flex items-center gap-x-2 justify-center"
        >
          <CreditCard class="h-4 w-4"/> Checkout
        </button>
        <button
          @click="clearCart"
          class="bg-red-700 text-white w-[30%] md:w-1/4 px-1 py-1 md:px-2 rounded hover:bg-red-800 flex items-center gap-x-2 justify-center"
        >
          <Trash2 class="h-4 w-4"/> Clear
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { X, Trash2, Plus, Minus, CreditCard } from "lucide-vue-next";
import { useCartStore } from "@/stores/cartStore";
import { useToast } from "vue-toastification";

const cartStore = useCartStore();
const toast = useToast();

defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
});

defineEmits(["close"]);

const handleIncrementQuantity = async (productId) => {
  try {
    await cartStore.incrementQuantity(productId);
  } catch (error) {
    toast.error("Failed to increase quantity. Please try again.");
  }
};

const handleDecrementQuantity = async (productId) => {
  try {
    await cartStore.decrementQuantity(productId);
  } catch (error) {
    toast.error("Failed to decrease quantity. Please try again.");
  }
};

const handleRemoveFromCart = async (productId) => {
  try {
    await cartStore.removeFromCart(productId);
  } catch (error) {
    toast.error("Failed to remove item from cart. Please try again.");
  }
};
</script>
