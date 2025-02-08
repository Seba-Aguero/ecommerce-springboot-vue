<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
    <!-- Header -->

    <div class="flex flex-col lg:flex-row gap-8">
      <!-- Filters Sidebar -->
      <div class="w-full lg:w-1/4">
        <LoadingWrapper
          :loading="loadingFilters"
          :skeleton-count="6"
          :skeleton-height="[15, 20, 40, 15, 15, 40]"
          :skeleton-width="['50%', '100%', '100%', '100%', '50%', '100%']"
        >
          <FilterSection
            :filters="productStore.filters"
            :categories="categories"
            :filteredProductsCount="productStore.totalElements"
          />
        </LoadingWrapper>
      </div>

      <!-- Products Grid -->
      <div class="w-full lg:w-3/4">
        <div v-if="productStore.loading">
          <LoadingSpinner />
        </div>

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
                @view-product="
                  router.push({ name: 'ProductDetail', params: { id: product.id } })
                "
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
import { ref, onMounted, defineAsyncComponent } from "vue";
import { useRouter } from "vue-router";
import { useProductStore } from "@/stores/productStore";
import { useCategoryStore } from "@/stores/categoryStore";
import ProductCard from "@/components/products/ProductCard.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import PaginationControls from "@/components/common/pagination/PaginationControls.vue";
import EmptyState from "@/components/common/EmptyState.vue";
import LoadingWrapper from "@/components/common/LoadingWrapper.vue";
const FilterSection = defineAsyncComponent(() =>
  import("@/components/products/filters/FilterSection.vue")
);

const productStore = useProductStore();
const categoryStore = useCategoryStore();
const categories = ref([]);
const router = useRouter();
const loadingFilters = ref(true);

const changePage = async (page) => {
  await productStore.fetchProducts({ page });
};

onMounted(async () => {
  try {
    await categoryStore.fetchCategories();
    categories.value = categoryStore.categories;
    await productStore.fetchProducts({ page: 0 });
  } catch (error) {
    console.error("Error loading initial data:", error);
  } finally {
    loadingFilters.value = false;
  }
});
</script>
