<template>
  <label
    :for="$props.for"
    class="block text-sm font-medium text-gray-700 dark:text-gray-100 mb-1"
  >
    <slot></slot>
  </label>
</template>

<script setup>
defineProps({
  'for': { // 'for' is a reserved keyword in JavaScript, so we use $props.for to access it
    type: String,
    required: true
  }
});
</script>
