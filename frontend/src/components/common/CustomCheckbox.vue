<template>
  <label class="relative flex items-center p-2 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700 group cursor-pointer">
    <input
      type="checkbox"
      :checked="modelValue"
      @change="$emit('update:modelValue', !modelValue)"
      class="hidden"
    />
    <div
      class="w-5 h-5 border-2 rounded-md border-gray-300 dark:border-gray-600 group-hover:border-primary-500 flex items-center justify-center"
      :class="{
        'bg-primary-500 border-primary-500': modelValue
      }"
    >
      <Check v-show="modelValue" class="h-3 w-3 text-white" />
    </div>
    <span class="ml-3 text-sm text-gray-600 dark:text-gray-300">
      <slot></slot>
    </span>
  </label>
</template>

<script setup>
import { Check } from 'lucide-vue-next';

defineProps({
  modelValue: {
    type: Boolean,
    required: true
  }
});

defineEmits(['update:modelValue']);
</script>