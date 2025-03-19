<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-12">
    <div class="flex justify-between items-center mb-8">
      <h1 class="text-lg sm:text-2xl md:text-3xl font-bold text-gray-900 dark:text-white">
        Products Management
      </h1>
      <button
        @click="openProductForm()"
        class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm sm:text-sm font-medium text-white bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 dark:focus:ring-offset-gray-800 transition-colors duration-200"
      >
        <Plus class="h-5 w-5 mr-2" aria-hidden="true" />
        Add Product
      </button>
    </div>

    <!-- Filters -->
    <div class="mb-8 grid grid-cols-1 sm:grid-cols-5 lg:grid-cols-4 gap-6">
      <div class="sm:col-span-3 lg:col-span-3">
        <SearchInput
          v-model="filters.search"
          placeholder="Search products..."
          @update:modelValue="debouncedFetchProducts"
        />
      </div>

      <div class="sm:col-span-2 lg:col-span-1">
        <select
          id="category"
          v-model="filters.category"
          @change="fetchProducts"
          class="block w-full py-2 px-3 border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 text-gray-700 dark:text-white"
        >
          <option value="" class="font-bold">All Categories</option>
          <option v-for="category in categories" :key="category.id" :value="category.id">
            {{ category.name }}
          </option>
        </select>
      </div>
    </div>

    <!-- Products Table -->
    <div
      class="bg-divte dark:bg-gray-800 shadow-md rounded-lg overflow-hidden overflow-x-scroll"
    >
      <div v-if="loading" class="p-6 flex justify-center">
        <LoadingSpinner />
      </div>
      <table v-else class="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
        <thead class="bg-gray-50 dark:bg-gray-700">
          <tr>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider"
            >
              Image
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider"
            >
              Name
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider"
            >
              Price
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider"
            >
              Stock
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider"
            >
              Categories
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider"
            >
              Actions
            </th>
          </tr>
        </thead>
        <tbody
          class="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700"
        >
          <tr
            v-for="product in products"
            :key="product.id"
            class="hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors duration-150"
          >
            <td class="px-6 py-4 whitespace-nowrap">
              <div
                class="h-16 w-16 rounded-md overflow-hidden bg-gray-100 dark:bg-gray-700 flex items-center justify-center"
              >
                <img
                  v-if="product.imageUrl"
                  :src="product.imageUrl"
                  :alt="product.name"
                  class="h-full w-full object-cover"
                  v-image-fallback
                />
                <ImageIcon v-else class="h-8 w-8 text-gray-400" />
              </div>
            </td>
            <td class="px-6 py-4">
              <div class="text-sm font-medium text-gray-900 dark:text-white">
                {{ product.name }}
              </div>
              <div class="text-sm text-gray-500 dark:text-gray-400 line-clamp-2">
                {{ product.description }}
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm font-medium text-primary-600 dark:text-primary-400">
                ${{ formatPrice(product.price) }}
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm text-gray-900 dark:text-white">
                {{ product.totalStock }}
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="flex flex-wrap gap-1">
                <span
                  v-for="category in product.categories"
                  :key="category.id"
                  class="px-2 py-1 text-xs rounded-full bg-primary-100 text-primary-800 dark:bg-primary-900 dark:text-primary-200"
                >
                  {{ category.name }}
                </span>
              </div>
            </td>
            <td
              class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium flex flex-col space-y-4 mt-1"
            >
              <button
                @click="openProductForm(product)"
                class="text-primary-600 hover:text-primary-900 dark:text-primary-400 dark:hover:text-primary-300 mr-3 inline-flex items-center"
              >
                <Edit class="h-4 w-4 mr-1" />
                Edit
              </button>
              <button
                @click="confirmDelete(product)"
                class="text-red-600 hover:text-red-900 dark:text-red-400 dark:hover:text-red-300 inline-flex items-center"
              >
                <Trash class="h-4 w-4 mr-1" />
                Delete
              </button>
            </td>
          </tr>
          <tr v-if="products.length === 0">
            <td
              colspan="6"
              class="px-6 py-10 text-center text-sm text-gray-500 dark:text-gray-400"
            >
              <div class="flex flex-col items-center">
                <PackageX class="h-12 w-12 text-gray-400 mb-2" />
                <p class="font-medium">No products found</p>
                <p class="text-xs mt-1">Try adjusting your search or filters</p>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div
      class="mt-6 bg-white dark:bg-gray-800 shadow-md rounded-lg p-4 flex justify-between items-center"
    >
      <div class="text-sm text-gray-700 dark:text-gray-300">
        Showing <span class="font-medium">{{ startItem }}</span> to
        <span class="font-medium">{{ endItem }}</span> of
        <span class="font-medium">{{ totalItems }}</span> products
      </div>
      <div class="flex space-x-2">
        <button
          @click="prevPage"
          :disabled="currentPage === 0"
          class="px-3 py-1 border border-gray-300 dark:border-gray-600 rounded-md text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-150"
        >
          <ChevronLeft class="h-5 w-5" />
        </button>
        <button
          @click="nextPage"
          :disabled="currentPage >= totalPages - 1"
          class="px-3 py-1 border border-gray-300 dark:border-gray-600 rounded-md text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-150"
        >
          <ChevronRight class="h-5 w-5" />
        </button>
      </div>
    </div>

    <!-- Product Form Modal -->
    <ProductFormModal
      v-if="showProductForm"
      :product="selectedProduct"
      :categories="categories"
      @close="showProductForm = false"
      @save="saveProduct"
    />

    <!-- Delete Confirmation Modal -->
    <ConfirmationModal
      v-if="showDeleteConfirmation"
      :is-open="showDeleteConfirmation"
      title="Delete Product"
      :message="`Are you sure you want to delete '${productToDelete?.name}'? This action cannot be undone.`"
      confirm-text="Delete"
      confirm-variant="danger"
      @confirm="deleteProduct"
      @cancel="showDeleteConfirmation = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useToast } from "vue-toastification";
import { productService } from "@/services/productService";
import { categoryService } from "@/services/categoryService";
import { useProductStore } from "@/stores/productStore";
import { formatPrice } from "@/utils/formatters";
import {
  Plus,
  Edit,
  Trash,
  ImageIcon,
  PackageX,
  ChevronLeft,
  ChevronRight,
} from "lucide-vue-next";
import ProductFormModal from "@/components/admin/ProductFormModal.vue";
import ConfirmationModal from "@/components/common/ConfirmationModal.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import SearchInput from "@/components/common/SearchInput.vue";

const productStore = useProductStore();
const toast = useToast();

const products = ref([]);
const categories = ref([]);
const totalItems = ref(0);
const totalPages = ref(0);
const currentPage = ref(0);
const pageSize = ref(10);
const loading = ref(false);
const filters = ref({
  search: "",
  category: "",
});

const showProductForm = ref(false);
const selectedProduct = ref(null);
const showDeleteConfirmation = ref(false);
const productToDelete = ref(null);

const startItem = computed(() => {
  return currentPage.value * pageSize.value + 1;
});

const endItem = computed(() => {
  return Math.min((currentPage.value + 1) * pageSize.value, totalItems.value);
});

const fetchProducts = async () => {
  loading.value = true;
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
    };

    // Add search parameter if it exists
    if (filters.value.search) {
      params.search = filters.value.search;
    }

    // Add category as a string instead of an array. The backend will handle it properly.
    if (filters.value.category) {
      params.categories = filters.value.category.toString();
    }

    const response = await productService.fetchProducts(params);
    products.value = response.data.content;
    totalItems.value = response.data.totalElements;
    totalPages.value = response.data.totalPages;
  } catch (error) {
    toast.error("Failed to load products");
    console.error("❌ Error fetching products:", error);
  } finally {
    loading.value = false;
  }
};

const debouncedFetchProducts = (() => {
  let timeout = null;
  return () => {
    clearTimeout(timeout);
    timeout = setTimeout(() => {
      // Reset to first page when filters change
      currentPage.value = 0;
      fetchProducts();
    }, 200);
  };
})();

const fetchCategories = async () => {
  try {
    const response = await categoryService.fetchCategories();
    categories.value = response.data;
  } catch (error) {
    toast.error("Failed to load categories");
    console.error("❌ Error fetching categories:", error);
  }
};

const openProductForm = (product = null) => {
  selectedProduct.value = product;
  showProductForm.value = true;
};

const saveProduct = async (data) => {
  try {
    if (data.id) {
      // Update existing product
      await productStore.updateProductWithImage(
        data.id,
        data.productData,
        data.image
      );
      toast.success("Product updated successfully");
    } else {
      // Create new product
      await productStore.createProductWithImage(
        data.productData,
        data.image
      );
      toast.success("Product created successfully");
    }
    showProductForm.value = false;
    fetchProducts();
  } catch (error) {
    console.error("❌ Error saving product:", error);
    toast.error(error.message || "Failed to save product");
  }
};

const confirmDelete = (product) => {
  productToDelete.value = product;
  showDeleteConfirmation.value = true;
};

const deleteProduct = async () => {
  try {
    await productService.deleteProduct(productToDelete.value.id);
    toast.success("Product deleted successfully");
    showDeleteConfirmation.value = false;
    fetchProducts();
  } catch (error) {
    toast.error("Failed to delete product");
    console.error("❌ Error deleting product:", error);
  }
};

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--;
    fetchProducts();
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++;
    fetchProducts();
  }
};

onMounted(() => {
  fetchProducts();
  fetchCategories();
});
</script>




