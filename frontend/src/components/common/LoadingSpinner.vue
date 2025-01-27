<template>
  <div class="flex justify-center items-center" :class="containerClass">
    <div
      class="animate-spin rounded-full border-b-2 border-primary-500"
      :class="[sizeClasses[size]]"
    ></div>
  </div>
</template>

<script setup>
const props = defineProps({
  size: {
    type: String,
    default: "md",
    validator: (value) => ["sm", "md", "lg"].includes(value),
  },
  containerClass: {
    type: String,
    default: "h-64",
  },
});

const sizeClasses = {
  sm: "h-8 w-8",
  md: "h-12 w-12",
  lg: "h-16 w-16",
};
</script>
