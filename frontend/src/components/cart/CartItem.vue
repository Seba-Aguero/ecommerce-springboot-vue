<template>
  <div class="flex items-center gap-4 py-3 border-b dark:border-gray-700">
    <img
      :src="item.imageUrl"
      :alt="item.name"
      :title="item.name"
      class="w-16 h-16 object-cover rounded cursor-pointer"
      v-image-fallback
      @click="navigateToProduct"
    />
    <div class="flex-1">
      <h3 class="text-sm font-medium text-gray-900 dark:text-white">
        {{ item.name }}
      </h3>
      <div class="flex items-center mt-2 h-7">
        <QuantityInput
          :model-value="item.quantity"
          :min="1"
          :max="item.totalStock"
          display-mode="text"
          @update:model-value="(value) => {
            if (value > item.quantity) $emit('increment', item.id);
            else if (value < item.quantity) $emit('decrement', item.id);
          }"
        />
      </div>
    </div>
    <div class="flex flex-col">
      <div class="flex justify-end">
        <button
          @click="$emit('remove', item.id)"
          class="text-red-500 hover:text-red-400 duration-0"
          title="Remove item from cart"
        >
          <Trash2 class="h-4 w-4" aria-hidden="true" />
        </button>
      </div>
      <div class="py-1">
        <p class="font-semibold text-gray-500 dark:text-gray-300 mt-2">
          ${{ formatPrice(item.price * item.quantity) }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Minus, Plus, Trash2 } from "lucide-vue-next";
import { useRouter } from "vue-router";
import { formatPrice } from "@/utils/formatters";
import QuantityInput from "@/components/common/QuantityInput.vue";

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
});

const router = useRouter();

defineEmits(['increment', 'decrement', 'remove']);

const navigateToProduct = () => {
  router.push(`/product/${props.item.id}`);
};
</script>

