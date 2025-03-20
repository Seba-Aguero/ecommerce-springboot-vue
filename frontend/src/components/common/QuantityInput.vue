<template>
  <div class="flex items-center gap-3 border rounded-md px-2 h-full border-gray-600">
    <button
      @click="decrement"
      :disabled="modelValue <= min || disabled"
      :title="modelValue <= min ? 'Minimum quantity reached' : 'Decrease quantity'"
      class="text-gray-500 dark:text-gray-300 hover:text-gray-800 dark:hover:text-gray-50 duration-0 disabled:opacity-40"
    >
      <Minus class="h-4 w-4" aria-hidden="true" />
    </button>
    <span
      v-if="displayMode === 'text'"
      class="text-sm text-gray-700 dark:text-gray-300"
      >{{ modelValue }}</span
    >
    <input
      v-else
      type="number"
      :value="modelValue"
      @input="handleInput"
      :min="min"
      :max="max"
      :disabled="disabled"
      class="w-12 text-center border-none focus:ring-0 p-0 text-lg text-gray-700 dark:text-gray-300 bg-transparent"
    />
    <button
      @click="increment"
      :disabled="modelValue >= max || disabled"
      :title="modelValue >= max ? 'Maximum quantity reached' : 'Increase quantity'"
      class="text-gray-500 dark:text-gray-300 hover:text-gray-800 dark:hover:text-gray-50 duration-0 disabled:opacity-40"
    >
      <Plus class="h-4 w-4" aria-hidden="true" />
    </button>
  </div>
</template>

<script setup>
import { Minus, Plus } from "lucide-vue-next";

const props = defineProps({
  modelValue: {
    type: Number,
    required: true,
  },
  min: {
    type: Number,
    default: 1,
  },
  max: {
    type: Number,
    default: Infinity,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  displayMode: {
    type: String,
    default: "input", // 'input' or 'text'
    validator: (value) => ["input", "text"].includes(value),
  },
});

const emit = defineEmits(["update:modelValue"]);

const increment = () => {
  if (props.modelValue < props.max) {
    emit("update:modelValue", props.modelValue + 1);
  }
};

const decrement = () => {
  if (props.modelValue > props.min) {
    emit("update:modelValue", props.modelValue - 1);
  }
};

const handleInput = (event) => {
  let value = parseInt(event.target.value);

  // Validate input
  if (isNaN(value)) {
    value = props.min;
  } else {
    if (value < props.min) value = props.min;
    if (value > props.max) value = props.max;
  }

  emit("update:modelValue", value);
};
</script>

<style scoped>
/* Remove spinner buttons from number input */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
input[type="number"] {
  -moz-appearance: textfield;
}
</style>

