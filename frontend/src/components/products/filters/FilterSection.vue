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
      <SearchInput
        v-model="localSearch"
        placeholder="Search products..."
        @update:modelValue="handleSearchChange"
      />
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
        class="mt-4 space-y-3"
      >
        <CustomCheckbox
          v-for="category in categories"
          :key="category.id"
          :model-value="filters.categories.includes(category.id)"
          @update:model-value="productStore.toggleCategory(category.id)"
        >
          {{ category.name }}
        </CustomCheckbox>
      </div>
    </div>

    <!-- Price Range -->
    <div class="mb-6">
      <h3 class="text-sm font-medium text-gray-900 dark:text-white mb-3">
        Price Range
      </h3>
      <PriceRangeInput
        :model-value="{ min: localMinPrice, max: localMaxPrice }"
        @update:model-value="handlePriceChange"
      />
    </div>

    <!-- Clear Filters -->
    <button
      @click="handleClearFilters"
      class="w-full bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-white px-4 py-2 rounded-md hover:bg-gray-200 dark:hover:bg-gray-600 transition duration-300 flex items-center justify-center"
    >
      <RefreshCw class="h-4 w-4 mr-2" />
      Clear Filters
    </button>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { ChevronDown, RefreshCw } from "lucide-vue-next";
import { useProductStore } from "@/stores/productStore";
import SearchInput from "@/components/common/SearchInput.vue";
import CustomCheckbox from '@/components/common/CustomCheckbox.vue';
import PriceRangeInput from '@/components/products/filters/PriceRangeInput.vue';

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

const handlePriceChange = ({ min, max }) => {
  localMinPrice.value = min;
  localMaxPrice.value = max;
  debounce(() => productStore.updatePriceRange(
    localMinPrice.value || null,
    localMaxPrice.value || null
  ), TIMEOUT_DURATION);
}

const handleClearFilters = () => {
  productStore.clearFilters();
};

// Sincronize local price values with store
watch(() => props.filters, (newFilters) => {
  localSearch.value = newFilters.search;
  localMinPrice.value = newFilters.minPrice;
  localMaxPrice.value = newFilters.maxPrice;
}, { deep: true, immediate: true });
</script>