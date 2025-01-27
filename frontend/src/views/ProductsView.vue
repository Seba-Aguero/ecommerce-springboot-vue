<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
    <!-- Header -->
    <div class="text-center mb-12">
      <h1
        class="text-3xl font-extrabold text-gray-900 dark:text-white sm:text-4xl"
      >
        Products
      </h1>
    </div>

    <div class="flex flex-col lg:flex-row gap-8">
      <!-- Filters Sidebar -->
      <div class="w-full lg:w-1/4">
        <div class="bg-white dark:bg-gray-800 p-6 rounded-lg shadow">
          <div class="mb-6 text-gray-600 dark:text-gray-400">
            {{ productStore.filteredProducts.length }}
            {{
              productStore.filteredProducts.length === 1
                ? "product"
                : "products"
            }}
            {{ hasActiveFilters ? "with current filters" : "in total" }}
          </div>
          <h2 class="text-lg font-medium text-gray-900 dark:text-white mb-4">
            Filters
          </h2>

          <!-- Search -->
          <div class="mb-6">
            <div class="relative">
              <input
                v-model="productStore.filters.search"
                type="text"
                placeholder="Search products..."
                class="w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              />
              <Search class="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            </div>
          </div>

          <!-- Categories Collapsible -->
          <div class="mb-6 border-b border-gray-200 dark:border-gray-700 pb-4">
            <button
              @click="toggleCategories"
              class="flex items-center justify-between w-full"
            >
              <h3 class="text-sm font-medium text-gray-900 dark:text-white">
                Categories
              </h3>
              <ChevronDown
                :class="[
                  'h-5 w-5 text-gray-500 transition-transform',
                  { 'transform rotate-180': showCategories },
                ]"
              />
            </button>
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
                  :id="'category-' + category.id"
                  type="checkbox"
                  :checked="
                    productStore.filters.categories.includes(category.id)
                  "
                  @change="productStore.toggleCategory(category.id)"
                  class="hidden"
                />
                <div
                  class="w-5 h-5 border-2 rounded-md border-gray-300 dark:border-gray-600 group-hover:border-primary-500 flex items-center justify-center"
                  :class="{
                    'bg-primary-500 border-primary-500':
                      productStore.filters.categories.includes(category.id),
                  }"
                >
                  <Check
                    v-show="
                      productStore.filters.categories.includes(category.id)
                    "
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
                  v-model="productStore.filters.minPrice"
                  type="number"
                  placeholder="Min"
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
                  v-model="productStore.filters.maxPrice"
                  type="number"
                  placeholder="Max"
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
      </div>

      <!-- Products Grid -->
      <div class="w-full lg:w-3/4">
        <LoadingSpinner v-if="productStore.loading" />

        <div
          v-else-if="productStore.error"
          class="text-center text-red-500 dark:text-red-400"
        >
          {{ productStore.error }}
        </div>

        <div v-else>
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            <ProductCard
              v-for="product in productStore.filteredProducts"
              :key="product.id"
              :product="product"
              @add-to-cart="addToCart"
            />
          </div>

          <!-- Pagination -->
          <div class="mt-8 flex justify-center">
            <nav class="flex items-center space-x-2">
              <button
                v-for="page in productStore.totalPages"
                :key="page"
                @click="changePage(page - 1)"
                :class="[
                  'px-3 py-1 rounded-md',
                  currentPage === page - 1
                    ? 'bg-primary-500 text-white'
                    : 'bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-200 hover:bg-gray-200 dark:hover:bg-gray-600',
                ]"
              >
                {{ page }}
              </button>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useProductStore } from "@/stores/productStore";
import { useCategoryStore } from "@/stores/categoryStore";
import { Search, ChevronDown, Check, RefreshCw } from "lucide-vue-next";
import ProductCard from "@/components/products/ProductCard.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";

const productStore = useProductStore();
const categoryStore = useCategoryStore();
const currentPage = ref(0);
const categories = ref([]);
const showCategories = ref(false);

const toggleCategories = () => {
  showCategories.value = !showCategories.value;
};

const changePage = async (page) => {
  currentPage.value = page;
  await productStore.fetchProducts({ page });
};

const addToCart = async (product) => {
  try {
    await productStore.addToCart(product);
    // Here can be added a success message or notification
  } catch (error) {
    console.error("Error adding to cart:", error);
    // Here can be added a error message or notification
  }
};

const hasActiveFilters = computed(() => {
  return (
    productStore.filters.categories.length > 0 ||
    productStore.filters.minPrice ||
    productStore.filters.maxPrice ||
    productStore.filters.search
  );
});

onMounted(async () => {
  try {
    await Promise.all([
      categoryStore.fetchCategories(),
      productStore.fetchProducts({ page: 0 }),
    ]);
    categories.value = categoryStore.categories;
  } catch (error) {
    console.error("Error loading initial data:", error);
  }
});
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
