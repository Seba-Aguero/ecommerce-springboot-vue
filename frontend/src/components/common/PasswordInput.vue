<template>
  <div class="relative">
    <input
      :id="id"
      :type="showPassword ? 'text' : 'password'"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      :required="required"
      :placeholder="placeholder"
      class="w-full pl-10 pr-10 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
    />
    <Lock
      class="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400"
      aria-hidden="true"
    />
    <button
      type="button"
      @click="showPassword = !showPassword"
      class="absolute right-3 top-1/2 transform -translate-y-1/2"
    >
      <component :is="showPassword ? EyeOff : Eye" class="h-5 w-5 text-gray-400" />
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Lock, Eye, EyeOff } from 'lucide-vue-next';

const props = defineProps({
  id: {
    type: String,
    required: true
  },
  modelValue: {
    type: String,
    default: ''
  },
  required: {
    type: Boolean,
    default: false
  },
  placeholder: {
    type: String,
    default: ''
  }
});

defineEmits(['update:modelValue']);

const showPassword = ref(false);
</script>
