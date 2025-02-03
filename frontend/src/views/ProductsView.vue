<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
    <!-- Header -->

    <div class="flex flex-col lg:flex-row gap-8">
      <!-- Filters Sidebar -->
      <div class="w-full lg:w-1/4">
        <FilterSection
          :filters="productStore.filters"
          :categories="categories"
          :filteredProductsCount="productStore.totalElements"
        />
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
          <!-- No products found -->
          <EmptyState
            v-if="!productStore.filteredProducts.length"
            title="No products found"
            description="Try adjusting your filters or search criteria"
          />

          <!-- Grid de productos -->
          <div v-else>
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              <ProductCard
                v-for="product in productStore.filteredProducts"
                :key="product.id"
                :product="product"
                @view-product="viewProduct"
              />
            </div>

            <!-- Pagination -->
            <PaginationControls
              :current-page="productStore.currentPage"
              :total-pages="productStore.totalPages"
              @page-change="changePage"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useProductStore } from "@/stores/productStore";
import { useCategoryStore } from "@/stores/categoryStore";
import ProductCard from "@/components/products/ProductCard.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import FilterSection from "@/components/products/filters/FilterSection.vue";
import PaginationControls from "@/components/common/pagination/PaginationControls.vue";
import EmptyState from "@/components/common/EmptyState.vue";

const productStore = useProductStore();
const categoryStore = useCategoryStore();
const categories = ref([]);
const router = useRouter();

const changePage = async (page) => {
  await productStore.fetchProducts({ page });
};

const viewProduct = (productId) => {
  router.push({ name: "ProductDetail", params: { id: productId } });
};

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
