<template>
  <div
    class="bg-white dark:bg-gray-800 overflow-hidden shadow-lg rounded-lg transition-transform duration-300 hover:scale-105"
  >
    <div class="flex flex-col h-full p-5">
      <div class="flex-shrink-0">
        <div
          class="aspect-w-1 aspect-h-1 w-full overflow-hidden rounded-lg bg-gray-100 dark:bg-gray-700"
        >
          <img
            :src="product.imageUrl"
            :alt="product.name"
            class="h-48 w-full object-cover object-center"
            v-image-fallback
            aria-hidden="true"
          />
        </div>
      </div>
      <div class="flex flex-col flex-grow mt-4">
        <h3 class="text-lg font-medium text-gray-900 dark:text-white">
          {{ product.name }}
        </h3>
        <p
          class="mt-2 text-sm text-gray-500 dark:text-gray-400 line-clamp-2 flex-grow"
        >
          {{ product.description }}
        </p>
        <div class="mt-4">
          <p
            class="text-lg font-semibold text-primary-600 dark:text-primary-400"
          >
            ${{ formatPrice(product.price) }}
          </p>
          <button
            @click="$emit('view-product', product.id)"
            :title="`View details of ${product.name}`"
            class="mt-3 w-full bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 text-white font-medium py-2 px-4 rounded-md"
            aria-label="View Product"
          >
            View Product
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/authStore';
import { formatPrice } from '@/utils/formatters';

const authStore = useAuthStore();

defineProps({
  product: {
    type: Object,
    required: true,
  },
});
</script>
