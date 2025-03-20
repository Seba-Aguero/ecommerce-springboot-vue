<template>
  <div class="flex flex-col gap-y-3">
    <div class="flex justify-between text-sm">
      <span class="text-gray-600 dark:text-gray-300">Subtotal:</span>
      <span class="text-gray-900 dark:text-white">${{ formatPrice(totalAmount) }}</span>
    </div>
    <div class="flex justify-between text-sm mb-2">
      <span class="text-gray-600 dark:text-gray-300">Shipping:</span>
      <span class="text-gray-900 dark:text-white">${{ formatPrice(shippingCost) }}</span>
    </div>
    <div class="flex justify-between font-semibold">
      <span class="text-gray-600 dark:text-gray-300">Total:</span>
      <span class="text-gray-900 dark:text-white"
        >${{ formatPrice(totalAmount + shippingCost) }}</span
      >
    </div>

    <slot></slot>
  </div>
</template>

<script setup>
import { formatPrice } from "@/utils/formatters";

defineProps({
  totalAmount: {
    type: Number,
    required: true,
  },
  shippingCost: {
    type: Number,
    required: true,
    default: 0,
  },
});
</script>
