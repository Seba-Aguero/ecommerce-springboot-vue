<template>
  <div class="relative">
    <select
      :id="id"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      :required="required"
      class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white appearance-none"
    >
      <option value="">{{ placeholder }}</option>
      <option v-for="option in options" :key="option" :value="option">
        {{ option }}
      </option>
    </select>
    <component
      :is="icon"
      v-if="icon"
      class="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400"
      aria-hidden="true"
    />
    <ChevronDown
      class="absolute right-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400 pointer-events-none"
      aria-hidden="true"
    />
  </div>
</template>

<script setup>
import { ChevronDown } from 'lucide-vue-next';

defineProps({
  id: {
    type: String,
    required: true
  },
  modelValue: {
    type: String,
    default: ""
  },
  required: {
    type: Boolean,
    default: false
  },
  placeholder: {
    type: String,
    default: "Select an option"
  },
  options: {
    type: Array,
    required: true
  },
  icon: {
    type: [Object, Function],
    default: null
  }
});

defineEmits(["update:modelValue"]);
</script>