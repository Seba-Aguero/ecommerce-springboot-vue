<template>
  <nav class="bg-slate-50 dark:bg-gray-800 shadow-md">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <!-- Logo -->
        <div class="flex items-center">
          <router-link to="/" class="flex items-center">
            <ShoppingBag class="h-8 w-8 text-primary-500" />
            <span
              class="ml-2 text-xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-primary-500 to-purple-600"
            >
              My Ecommerce
            </span>
          </router-link>
        </div>

        <!-- Desktop Menu -->
        <div class="hidden md:flex md:items-center">

          <!-- Theme Toggle Button -->
          <button
            @click="toggleTheme"
            class="p-2 text-gray-500 hover:text-primary-500 dark:text-gray-300 dark:hover:text-primary-400 rounded-full transition-colors duration-200"
            :title="isDark ? 'Switch to light mode' : 'Switch to dark mode'"
          >
            <Sun v-if="isDark" class="h-5 w-5 no-transition" />
            <Moon v-else class="h-5 w-5 no-transition" />
          </button>

          <!-- Cart -->
          <router-link
            to="/cart"
            class="text-gray-600 hover:text-primary-500 dark:text-gray-300 dark:hover:text-primary-400 px-3 py-2 rounded-md text-sm font-medium relative"
            title="Cart"
          >
            <ShoppingCart class="h-5 w-5 inline-block no-transition" />
            <span
              v-if="cartStore.totalItems"
              class="absolute -top-1 -right-1 bg-primary-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center"
            >
              {{ cartStore.totalItems }}
            </span>
          </router-link>

          <!-- Desktop Auth Menu -->
          <div v-if="authStore.isAuthenticated" class="ml-4 relative">
            <button
              @click="isMenuOpen = !isMenuOpen"
              class="text-gray-600 hover:text-primary-500 dark:text-gray-300 dark:hover:text-primary-400 px-3 py-2 rounded-md text-sm font-medium flex items-center"
            >
              <User class="h-5 w-5 mr-1 no-transition" />
              {{ authStore.userFullName }}
            </button>

            <div
              v-show="isMenuOpen"
              class="absolute right-0 mt-2 w-48 rounded-md shadow-lg py-1 bg-slate-50 dark:bg-gray-700 ring-1 ring-black ring-opacity-5"
            >
              <router-link
                to="/profile"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600"
                @click="isMenuOpen = false"
              >
                My Profile
              </router-link>
              <button
                @click="handleLogout"
                class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600"
              >
                Log Out
              </button>
            </div>
          </div>

          <!-- Desktop Login/Register -->
          <div v-else class="ml-4 flex items-center space-x-4">
            <router-link
              to="/login"
              class="px-4 py-2 rounded-md text-sm font-medium text-gray-600 hover:text-gray-800 dark:text-gray-200 dark:hover:text-white border border-gray-300 dark:border-gray-600 hover:bg-gray-200 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
            >
              Log In
            </router-link>
            <router-link
              to="/register"
              class="px-4 py-2 rounded-md text-sm font-medium text-white bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
            >
              Sign Up
            </router-link>
          </div>
        </div>

        <!-- Mobile menu button -->
        <div class="flex items-center md:hidden">
          <!-- Theme Toggle Button for Mobile -->
          <button
            @click="toggleTheme"
            class="p-2 text-gray-500 dark:text-gray-400 hover:text-primary-500 dark:hover:text-primary-400 rounded-full transition-colors duration-200 mr-2"
            :title="isDark ? 'Switch to light mode' : 'Switch to dark mode'"
          >
            <Sun v-if="isDark" class="h-5 w-5 no-transition" />
            <Moon v-else class="h-5 w-5 no-transition" />
          </button>

          <router-link
            to="/cart"
            class="text-gray-600 hover:text-primary-500 dark:text-gray-300 dark:hover:text-primary-400 px-3 py-2 rounded-md relative mr-2"
          >
            <ShoppingCart class="h-5 w-5 no-transition" />
            <span
              v-if="cartStore.totalItems"
              class="absolute -top-1 -right-1 bg-primary-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center"
            >
              {{ cartStore.totalItems }}
            </span>
          </router-link>

          <button
            @click="isMobileMenuOpen = !isMobileMenuOpen"
            class="inline-flex items-center justify-center p-2 rounded-md text-gray-600 hover:text-gray-800 dark:text-gray-200 dark:hover:text-white hover:bg-gray-100 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-primary-500"
          >
            <span class="sr-only">Open main menu</span>
            <Menu v-if="!isMobileMenuOpen" class="block h-6 w-6" />
            <X v-else class="block h-6 w-6" />
          </button>
        </div>
      </div>
    </div>

    <!-- Mobile menu -->
    <div v-show="isMobileMenuOpen" class="md:hidden">
      <div class="px-2 pt-2 pb-3 space-y-1">

        <!-- Mobile Auth Menu -->
        <template v-if="authStore.isAuthenticated">
          <router-link
            to="/profile"
            class="block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 dark:text-gray-200 dark:hover:text-white hover:bg-gray-50 dark:hover:bg-gray-700"
            @click="isMobileMenuOpen = false"
          >
            My Profile
          </router-link>
          <button
            @click="handleMobileLogout"
            class="block w-full text-left px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 dark:text-gray-200 dark:hover:text-white hover:bg-gray-50 dark:hover:bg-gray-700"
          >
            Log Out
          </button>
        </template>

        <!-- Mobile Login/Register -->
        <template v-else>
          <router-link
            to="/login"
            class="block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 dark:text-gray-200 dark:hover:text-white hover:bg-gray-50 dark:hover:bg-gray-700"
            @click="isMobileMenuOpen = false"
          >
            Log In
          </router-link>
          <router-link
            to="/register"
            class="block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 dark:text-gray-200 dark:hover:text-white hover:bg-gray-50 dark:hover:bg-gray-700"
            @click="isMobileMenuOpen = false"
          >
            Sign Up
          </router-link>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import { useCartStore } from "@/stores/cartStore";
import { useThemeStore } from "@/stores/themeStore";
import { authService } from "@/services/authService";
import { themeService } from "@/services/themeService";
import {
  ShoppingBag,
  ShoppingCart,
  User,
  Menu,
  X,
  Sun,
  Moon,
} from "lucide-vue-next";

const router = useRouter();
const authStore = useAuthStore();
const cartStore = useCartStore();
const isMenuOpen = ref(false);
const isMobileMenuOpen = ref(false);
const themeStore = useThemeStore();

const isDark = computed(() => themeService.getTheme());

const toggleTheme = () => {
  themeService.toggleTheme();
};

onMounted(() => {
  themeStore.initTheme();
});

const handleLogout = async () => {
  try {
    await authService.logout();
    isMenuOpen.value = false;
    router.push("/login");
  } catch (error) {
    console.error(error.message);
  }
};

const handleMobileLogout = async () => {
  await authStore.logout();
  isMobileMenuOpen.value = false;
  router.push("/login");
};
</script>
