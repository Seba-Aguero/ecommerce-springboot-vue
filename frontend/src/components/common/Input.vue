<template>
  <div class="relative">
    <input
      :id="id"
      :type="type"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      :required="required"
      :placeholder="placeholder"
      class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
    />
    <component
      :is="icon"
      v-if="icon"
      class="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400"
      aria-hidden="true"
    />
  </div>
</template>

<script setup>
defineProps({
  id: {
    type: String,
    required: true
  },
  modelValue: {
    type: [String, Number],
    default: ""
  },
  type: {
    type: String,
    default: "text"
  },
  required: {
    type: Boolean,
    default: false
  },
  placeholder: {
    type: String,
    default: ""
  },
  icon: {
    type: [Object, Function],
    default: null
  }
});

defineEmits(["update:modelValue"]);
</script>

