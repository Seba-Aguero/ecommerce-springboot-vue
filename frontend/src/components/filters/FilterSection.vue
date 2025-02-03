<template>
  <div class="bg-white dark:bg-gray-800 p-6 rounded-lg shadow">
    <div class="mb-6 text-gray-600 dark:text-gray-400">
      {{ filteredProductsCount }}
      {{
        filteredProductsCount === 1
          ? "product"
          : "products"
      }}
    </div>
    <h2 class="text-lg font-medium text-gray-900 dark:text-white mb-4">
      Filters
    </h2>

    <!-- Search -->
    <div class="mb-6">
      <div class="relative">
        <input
          v-model="filters.search"
          type="text"
          @input="handleSearchChange"
          placeholder="Search products..."
          class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
        />
        <Search class="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
      </div>
    </div>

    <!-- Categories Collapsible -->
    <div class="mb-6 border-b border-gray-200 dark:border-gray-700 pb-4">
      <div class="flex items-center justify-between">
        <h3 class="text-sm font-medium text-gray-900 dark:text-white">
          Categories
        </h3>
        <button
          @click="toggleCategories"
          class="flex items-center"
        >
          <ChevronDown
        :class="[
          'h-5 w-5 text-gray-500 transition-transform',
          { 'transform rotate-180': showCategories },
        ]"
          />
        </button>
      </div>
      <!-- Categories List -->
      <div
        v-show="showCategories"
        class="mt-4 space-y-3 max-h-48 overflow-y-auto custom-scrollbar"
      >
        <label
          v-for="category in categories"
          :key="category.id"
          class="relative flex items-center p-2 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700 group cursor-pointer"
        >
          <input
            type="checkbox"
            :checked="filters.categories.includes(category.id)"
            @change=productStore.toggleCategory(category.id)
            class="hidden"
          />
          <div
            class="w-5 h-5 border-2 rounded-md border-gray-300 dark:border-gray-600 group-hover:border-primary-500 flex items-center justify-center"
            :class="{
              'bg-primary-500 border-primary-500': filters.categories.includes(category.id),
            }"
          >
            <Check
              v-show="filters.categories.includes(category.id)"
              class="h-3 w-3 text-white"
            />
          </div>
          <span class="ml-3 text-sm text-gray-600 dark:text-gray-300">
            {{ category.name }}
          </span>
        </label>
      </div>
    </div>

    <!-- Price Range -->
    <div class="mb-6">
      <h3 class="text-sm font-medium text-gray-900 dark:text-white mb-3">
        Price Range
      </h3>
      <div class="flex items-center space-x-4">
        <div class="relative flex-1">
          <span
            class="absolute inset-y-0 left-3 flex items-center text-gray-500"
            >$</span
          >
          <input
            v-model.number="localMinPrice"
            type="number"
            placeholder="Min"
            @input="handlePriceChange"
            class="w-full pl-8 pr-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
          />
        </div>
        <span class="text-gray-500">-</span>
        <div class="relative flex-1">
          <span
            class="absolute inset-y-0 left-3 flex items-center text-gray-500"
            >$</span
          >
          <input
            v-model.number="localMaxPrice"
            type="number"
            placeholder="Max"
            @input="handlePriceChange"
            class="w-full pl-8 pr-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
          />
        </div>
      </div>
    </div>

    <!-- Clear Filters -->
    <button
      @click="productStore.clearFilters"
      class="w-full bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-white px-4 py-2 rounded-md hover:bg-gray-200 dark:hover:bg-gray-600 transition duration-300 flex items-center justify-center"
    >
      <RefreshCw class="h-4 w-4 mr-2" />
      Clear Filters
    </button>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { Search, ChevronDown, Check, RefreshCw } from "lucide-vue-next";
import { useProductStore } from "@/stores/productStore";

const productStore = useProductStore();

const props = defineProps({
  filters: {
    type: Object,
    required: true,
  },
  categories: {
    type: Array,
    required: true,
  },
  filteredProductsCount: {
    type: Number,
    required: true,
  },
});

const showCategories = ref(false);
const localSearch = ref("");
const localMinPrice = ref(null);
const localMaxPrice = ref(null);
const timeout = ref(null);
const TIMEOUT_DURATION = 500;

const toggleCategories = () => {
  showCategories.value = !showCategories.value;
};

// Use debounce to prevent multiple API calls
const debounce = (fn, delay) => {
  clearTimeout(timeout.value);
  timeout.value = setTimeout(fn, delay);
};

const handleSearchChange = () =>
  debounce(() => productStore.updateSearch(localSearch.value), TIMEOUT_DURATION);

const handlePriceChange = () =>
  debounce(() => productStore.updatePriceRange(
    localMinPrice.value || null,
    localMaxPrice.value || null
  ), TIMEOUT_DURATION);

// Sincronize local price values with store
watch(() => props.filters, (newFilters) => {
  localSearch.value = newFilters.search;
  localMinPrice.value = newFilters.minPrice;
  localMaxPrice.value = newFilters.maxPrice;
}, { deep: true, immediate: true });

</script>

<style scoped>
.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: #cbd5e1 transparent;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #cbd5e1;
  border-radius: 3px;
}

.dark .custom-scrollbar {
  scrollbar-color: #4b5563 transparent;
}

.dark .custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #4b5563;
}

/* Remove arrows/spinners from number inputs */
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type="number"] {
  -moz-appearance: textfield; /* Firefox */
}
</style>
