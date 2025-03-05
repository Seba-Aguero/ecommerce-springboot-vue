<template>
  <div :key="route.params.id">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <LoadingSpinner v-if="productStore.loading" />

      <div v-else-if="productStore.error" class="text-red-500 dark:text-red-400">
        {{ productStore.error }}
      </div>

      <div v-else class="flex flex-col md:flex-row gap-8">
        <!-- Product Image -->
        <div class="md:w-1/2">
          <img
            :src="productStore.currentProduct?.imageUrl"
            :alt="productStore.currentProduct?.name"
            class="w-full h-full max-h-[500px] max-w-[500px] rounded-lg shadow-lg object-contain bg-white dark:bg-gray-800"
            v-image-fallback
          />
        </div>

        <!-- Product Details -->
        <div class="md:w-1/2">
          <h1 class="text-3xl font-bold text-gray-900 dark:text-white">
            {{ productStore.currentProduct?.name }}
          </h1>

          <div class="mt-8">
            <p class="text-2xl font-bold text-primary-600 dark:text-primary-400">
              ${{ formatPrice(productStore.currentProduct?.price) }}
            </p>

            <p class="mt-2 text-sm text-gray-700 dark:text-gray-400">
              Stock: {{ productStore.currentProduct?.totalStock }}
            </p>
          </div>

          <!-- Categories -->
          <div class="mt-6 flex items-center">
            <h3 class="text-sm font-medium text-gray-900 dark:text-white mr-3">
              Categories:
            </h3>
            <div class="flex flex-wrap gap-2">
              <span
                v-for="category in productStore.currentProduct?.categories"
                :key="category.id"
                class="px-3 py-1 rounded-full text-sm bg-gray-200 dark:bg-gray-700 text-gray-800 dark:text-gray-200"
              >
                {{ category.name }}
              </span>
            </div>
          </div>

          <!-- Quantity and Add to Cart -->
          <div class="mt-8">
            <div class="flex items-center gap-4">
              <div class="w-24">
                <label for="quantity" class="sr-only">Quantity</label>
                <input
                  type="number"
                  id="quantity"
                  v-model.number="quantity"
                  min="1"
                  :max="computeMaxQuantity"
                  @input="validateQuantity"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                />
              </div>
              <button
                @click="addToCart"
                :disabled="!isProductAvailable || quantity <= 0"
                :title="
                  isProductAvailable
                    ? `Add ${quantity === 1 ? 'item' : 'items'} to cart`
                    : 'Product is out of stock'
                "
                class="flex-1 bg-primary-600 text-white px-6 py-2 rounded-md hover:bg-primary-700 transition-colors"
              >
                {{ isProductAvailable ? "Add to Cart" : "Out of Stock" }}
              </button>
            </div>
          </div>

          <!-- Description -->
          <div class="mt-12 border-t border-gray-200 dark:border-gray-700 pt-8">
            <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-4">
              Description
            </h2>
            <p class="mt-4 text-gray-700 dark:text-gray-300">
              {{ productStore.currentProduct?.description }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, onBeforeRouteUpdate } from "vue-router";
import { useProductStore } from "@/stores/productStore";
import { useCartStore } from "@/stores/cartStore";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { useToast } from "vue-toastification";
import { formatPrice } from "@/utils/formatters";

const route = useRoute();
const toast = useToast();
const productStore = useProductStore();
const cartStore = useCartStore();
const quantity = ref(1);

onBeforeRouteUpdate(async (to, from) => {
  if (to.params.id !== from.params.id) {
    await productStore.fetchProductById(to.params.id);
  }
});

const isProductAvailable = computed(
  () => productStore.currentProduct && productStore.currentProduct.totalStock > 0
);

// Compute current quantity already in cart for the product
const currentCartItemQuantity = computed(() => {
  if (!productStore.currentProduct) return 0;

  return cartStore.items
    .filter((item) => item.id === productStore.currentProduct.id)
    .reduce((sum, item) => sum + item.quantity, 0);
});

// Compute maximum possible quantity based on stock and cart items
const computeMaxQuantity = computed(() => {
  if (!productStore.currentProduct) return 1;

  return productStore.currentProduct.totalStock - currentCartItemQuantity.value;
});

// Quantity input validation
const validateQuantity = () => {
  if (quantity.value < 1) {
    quantity.value = 1;
  }
  if (quantity.value > computeMaxQuantity.value) {
    quantity.value = computeMaxQuantity.value;
  }
};

const addToCart = async () => {
  if (!productStore.currentProduct) {
    toast.error("Product not available");
    return;
  }

  // Quantity validation
  if (
    quantity.value < 1 ||
    currentCartItemQuantity.value + quantity.value >
      productStore.currentProduct.totalStock
  ) {
    toast.error(
      `Cannot add more than ${
        productStore.currentProduct.totalStock - currentCartItemQuantity.value
      } items to cart`
    );
    return;
  }

  try {
    await cartStore.addToCart(productStore.currentProduct, quantity.value);
    toast.success("Product added to cart successfully!");
  } catch (error) {
    toast.error("Error adding product to cart");
  }
};

onMounted(async () => {
  await productStore.fetchProductById(route.params.id);
});
</script>
