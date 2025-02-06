<template>
  <div class="flex items-center space-x-4">
    <div class="relative flex-1">
      <span class="absolute inset-y-0 left-3 flex items-center text-gray-500">$</span>
      <input
        :value="modelValue.min"
        @input="handleMinChange"
        type="number"
        placeholder="Min"
        class="custom-number-input w-full pl-8 pr-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
      />
    </div>
    <span class="text-gray-500">-</span>
    <div class="relative flex-1">
      <span class="absolute inset-y-0 left-3 flex items-center text-gray-500">$</span>
      <input
        :value="modelValue.max"
        @input="handleMaxChange"
        type="number"
        placeholder="Max"
        class="custom-number-input w-full pl-8 pr-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
      />
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => ({
      min: null,
      max: null
    })
  }
});

const emit = defineEmits(['update:modelValue']);

const handleMinChange = (event) => {
  emit('update:modelValue', {
    ...props.modelValue,
    min: event.target.value ? Number(event.target.value) : null
  });
};

const handleMaxChange = (event) => {
  emit('update:modelValue', {
    ...props.modelValue,
    max: event.target.value ? Number(event.target.value) : null
  });
};
</script>