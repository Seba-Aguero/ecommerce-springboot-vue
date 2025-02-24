<template>
  <div
    v-if="isOpen"
    class="absolute inset-0 flex items-center justify-center bg-black bg-opacity-30"
  >
    <div class="bg-white dark:bg-gray-800 p-4 rounded shadow-lg w-3/4">
      <p class="font-semibold text-gray-600 dark:text-gray-300">
        {{ message }}
      </p>
      <div class="flex justify-end mt-4">
        <button
          @click="onConfirm"
          class="bg-red-700 text-white px-4 py-2 rounded hover:bg-red-800"
        >
          {{ confirmText }}
        </button>
        <button
          @click="onCancel"
          class="bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400 ml-2"
        >
          {{ cancelText }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  isOpen: {
    type: Boolean,
    required: true
  },
  message: {
    type: String,
    required: true
  },
  confirmText: {
    type: String,
    default: 'Yes'
  },
  cancelText: {
    type: String,
    default: 'No'
  }
});

const emit = defineEmits(['confirm', 'cancel']);

const onConfirm = () => emit('confirm');
const onCancel = () => emit('cancel');
</script>