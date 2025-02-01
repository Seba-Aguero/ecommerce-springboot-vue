<template>
  <div class="mt-8 flex justify-center">
    <nav class="flex items-center space-x-2">
      <button
        v-for="page in totalPages"
        :key="page"
        @click="$emit('page-change', page - 1)"
        :class="[
          'px-3 py-1 rounded-md',
          currentPage === page - 1
            ? 'bg-primary-500 text-white'
            : 'bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-200 hover:bg-gray-200 dark:hover:bg-gray-600',
        ]"
      >
        {{ page }}
      </button>
    </nav>
  </div>
</template>

<script setup>
defineProps({
  currentPage: {
    type: Number,
    required: true
  },
  totalPages: {
    type: Number,
    required: true
  }
});

defineEmits(['page-change']);
</script>