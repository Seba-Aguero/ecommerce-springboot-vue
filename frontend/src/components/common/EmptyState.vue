<template>
  <div class="flex flex-col items-center justify-center py-12 text-center">
    <SearchX class="w-16 h-16 text-gray-400 dark:text-gray-600 mb-4" aria-hidden="true" />
    <h3 class="text-lg font-medium text-gray-900 dark:text-white mb-2">
      {{ title }}
    </h3>
    <p class="text-gray-500 dark:text-gray-400">
      {{ description }}
    </p>
  </div>
</template>

<script setup>
import { SearchX } from "lucide-vue-next";

defineProps({
  title: {
    type: String,
    default: "No results found",
  },
  description: {
    type: String,
    default: "Try adjusting your search criteria",
  },
});
</script>
