<template>
  <div
    class="min-h-screen bg-gradient-to-br from-primary-100 to-purple-100 dark:from-gray-900 dark:to-gray-900"
  >
    <!-- Hero Section -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="text-center">
        <h1
          class="text-4xl tracking-tight font-extrabold text-gray-900 dark:text-white sm:text-5xl md:text-6xl"
        >
          <span class="block">Welcome to My Ecommerce</span>
          <span class="block text-primary-600 dark:text-primary-400"
            >Your Online Shopping Destination</span
          >
        </h1>
        <p
          class="mt-3 max-w-md mx-auto text-base text-gray-500 dark:text-gray-400 sm:text-lg md:mt-5 md:text-xl md:max-w-3xl"
        >
          Discover a new way to shop online. Quality products, competitive
          prices, and an exceptional user experience.
        </p>
        <div class="mt-5 max-w-md mx-auto sm:flex sm:justify-center md:mt-8">
          <div class="rounded-md shadow">
            <router-link
              to="/products"
              class="w-full flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-md text-white bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 md:py-4 md:text-lg md:px-10"
              aria-label="Start Shopping"
            >
              Start Shopping
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Features Section -->
    <div id="features" class="py-12 bg-white dark:bg-gray-800">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center">
          <h2
            class="text-3xl font-extrabold text-gray-900 dark:text-white sm:text-4xl"
          >
            Why Choose Us?
          </h2>
          <p
            class="mt-4 max-w-2xl text-xl text-gray-500 dark:text-gray-400 mx-auto"
          >
            Discover the advantages of shopping with us.
          </p>
        </div>

        <div class="mt-12">
          <div class="grid grid-cols-1 gap-10 sm:grid-cols-2 lg:grid-cols-3">
            <div class="flex flex-col items-center">
              <Truck class="h-12 w-12 text-primary-500 dark:text-primary-400" aria-hidden="true" />
              <h3
                class="mt-6 text-xl font-medium text-gray-900 dark:text-white"
              >
                Fast Shipping
              </h3>
              <p
                class="mt-2 text-base text-gray-500 dark:text-gray-400 text-center"
              >
                24-48 hour delivery for most products.
              </p>
            </div>
            <div class="flex flex-col items-center">
              <ShieldCheck
                class="h-12 w-12 text-primary-500 dark:text-primary-400"
                aria-hidden="true"
              />
              <h3
                class="mt-6 text-xl font-medium text-gray-900 dark:text-white"
              >
                Secure Shopping
              </h3>
              <p
                class="mt-2 text-base text-gray-500 dark:text-gray-400 text-center"
              >
                Encrypted transactions and guaranteed data protection.
              </p>
            </div>
            <div class="flex flex-col items-center">
              <Headphones
                class="h-12 w-12 text-primary-500 dark:text-primary-400"
                aria-hidden="true"
              />
              <h3
                class="mt-6 text-xl font-medium text-gray-900 dark:text-white"
              >
                24/7 Support
              </h3>
              <p
                class="mt-2 text-base text-gray-500 dark:text-gray-400 text-center"
              >
                Our customer service team is always available.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Featured Products Section -->
    <div id="products" class="py-12 bg-gray-50 dark:bg-gray-900">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2
          class="text-3xl font-extrabold text-gray-900 dark:text-white text-center mb-8"
        >
          Featured Products
        </h2>

        <LoadingSpinner v-if="loading" size="lg" />

        <div
          v-else
          class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4"
        >
          <ProductCard
            v-for="product in featuredProducts"
            :key="product.id"
            :product="product"
            @view-product="router.push({ name: 'ProductDetail', params: { id: product.id } })"
            aria-label="Product Card"
          />
        </div>
      </div>
    </div>

    <!-- Contact Section -->
    <div id="contact" class="py-12 bg-white dark:bg-gray-800">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center">
          <h2
            class="text-3xl font-extrabold text-gray-900 dark:text-white sm:text-4xl"
          >
            Contact Us
          </h2>
          <p class="mt-4 text-lg text-gray-500 dark:text-gray-400">
            Have any questions? We're here to help.
          </p>
        </div>
        <div class="mt-8 flex justify-center space-x-4">
          <a
            href="mailto:info@miecommerce.com"
            class="inline-flex items-center justify-center px-5 py-3 border border-transparent text-base font-medium rounded-md text-white bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600"
            aria-label="Send us an email"
          >
            Send us an email
            <Mail class="ml-2 -mr-1 h-5 w-5" aria-hidden="true" />
          </a>
          <a
            href="tel:+1234567890"
            class="inline-flex items-center justify-center px-5 py-3 border border-primary-500 dark:border-primary-400 text-base font-medium rounded-md text-primary-600 dark:text-primary-400 hover:bg-primary-50 dark:hover:bg-gray-700"
            aria-label="Call us"
          >
            Call us
            <Phone class="ml-2 -mr-1 h-5 w-5" aria-hidden="true" />
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { productService } from "@/services/productService";
import { Truck, ShieldCheck, Headphones, Mail, Phone } from "lucide-vue-next";
import ProductCard from "@/components/products/ProductCard.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";

const router = useRouter();
const featuredProducts = ref([]);
const loading = ref(false);

onMounted(async () => {
  loading.value = true;
  try {
    const products = await productService.fetchProducts();
    featuredProducts.value = products.slice(0, 4);
  } catch (error) {
    console.error(error.message);
  } finally {
    loading.value = false;
  }
});
</script>
