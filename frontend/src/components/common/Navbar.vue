<template>
  <nav class="bg-slate-50 dark:bg-gray-800 shadow-md">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <!-- Logo -->
        <div class="flex items-center">
          <router-link to="/" class="flex items-center" aria-label="Home" title="Go to homepage">
            <ShoppingBag class="h-8 w-8 text-primary-500" aria-hidden="true" />
            <span
              class="ml-2 text-xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-primary-500 to-purple-600"
            >
              My Ecommerce
            </span>
          </router-link>
        </div>

        <!-- Desktop Menu -->
        <div class="hidden md:flex md:items-center gap-4">
          <!-- Theme Toggle Button -->
          <button
            @click="toggleTheme"
            class="p-3 text-gray-500 hover:text-primary-500 dark:text-gray-300 dark:hover:text-primary-400 rounded-full"
            :title="isDark ? 'Switch to light mode' : 'Switch to dark mode'"
            aria-label="Toggle Theme"
          >
            <Sun v-if="isDark" class="h-5 w-5 no-transition" aria-hidden="true" />
            <Moon v-else class="h-5 w-5 no-transition" aria-hidden="true" />
          </button>

          <!-- Cart -->
          <button
            v-if="authStore.isAuthenticated"
            @click="isCartOpen = true"
            class="text-gray-600 hover:text-primary-500 dark:text-gray-300 dark:hover:text-primary-400 px-3 py-2 rounded-md text-sm font-medium relative"
            title="View your shopping cart"
            aria-label="Cart"
          >
            <ShoppingCart class="h-5 w-5 inline-block no-transition" aria-hidden="true" />
            <span
              v-if="cartStore.totalItems"
              class="absolute -top-1 -right-1 bg-primary-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center"
            >
              {{ cartStore.totalItems }}
            </span>
          </button>

          <!-- Desktop Auth Menu -->
          <div v-if="authStore.isAuthenticated" class="relative">
            <button
              @click="isProfileMenuOpen = !isProfileMenuOpen"
              class="text-gray-600 hover:text-primary-500 dark:text-gray-300 dark:hover:text-primary-400 px-3 py-2 rounded-md text-sm font-medium flex items-center"
              :title="isProfileMenuOpen ? 'Close user menu' : 'Open user menu'"
              aria-label="User Menu"
            >
              <div class="relative w-5 h-5">
                <User
                  class="h-5 w-5 absolute transition-all duration-200 ease-in-out"
                  :class="
                    isProfileMenuOpen ? 'opacity-0 rotate-90' : 'opacity-100 rotate-0'
                  "
                  aria-hidden="true"
                />
                <X
                  class="h-5 w-5 absolute transition-all duration-200 ease-in-out"
                  :class="
                    isProfileMenuOpen ? 'opacity-100 rotate-0' : 'opacity-0 -rotate-90'
                  "
                  aria-hidden="true"
                />
              </div>
            </button>

            <div
              v-show="isProfileMenuOpen"
              class="absolute right-0 mt-2 w-48 rounded-md shadow-lg py-1 bg-slate-50 dark:bg-gray-700 ring-1 ring-black ring-opacity-5"
            >
              <router-link
                to="/profile"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600"
                @click="isProfileMenuOpen = false"
                aria-label="My Profile"
                title="View and edit your profile"
              >
                My Profile
              </router-link>
              <router-link
                to="/user-orders"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600"
                @click="isProfileMenuOpen = false"
                aria-label="My Orders"
                title="View your order history"
              >
                My Orders
              </router-link>
              <button
                @click="handleLogout"
                class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600"
                aria-label="Log Out"
                title="Sign out of your account"
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
              aria-label="Log In"
              title="Sign in to your account"
            >
              Log In
            </router-link>
            <router-link
              to="/register"
              class="px-4 py-2 rounded-md text-sm font-medium text-white bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
              aria-label="Sign Up"
              title="Create a new account"
            >
              Sign Up
            </router-link>
          </div>
        </div>

        <!-- Mobile Navbar Items -->
        <div class="flex items-center md:hidden gap-3">
          <!-- Theme Toggle Button for Mobile -->
          <button
            @click="toggleTheme"
            class="p-2 text-gray-500 dark:text-gray-400 hover:text-primary-500 dark:hover:text-primary-400 rounded-full"
            :title="isDark ? 'Switch to light mode' : 'Switch to dark mode'"
            aria-label="Toggle Theme"
          >
            <Sun v-if="isDark" class="h-5 w-5 no-transition" aria-hidden="true" />
            <Moon v-else class="h-5 w-5 no-transition" aria-hidden="true" />
          </button>

          <!-- Cart for Mobile -->
          <button
            v-if="authStore.isAuthenticated"
            @click="isCartOpen = true"
            class="text-gray-600 hover:text-primary-500 dark:text-gray-300 dark:hover:text-primary-400 px-3 py-2 rounded-md text-sm font-medium relative"
            title="View your shopping cart"
            aria-label="Cart"
          >
            <ShoppingCart class="h-5 w-5 inline-block no-transition" aria-hidden="true" />
            <span
              v-if="cartStore.totalItems"
              class="absolute -top-1 -right-1 bg-primary-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center"
            >
              {{ cartStore.totalItems }}
            </span>
          </button>

          <!-- Mobile Burger Menu -->
          <button
            @click="isMobileMenuOpen = !isMobileMenuOpen"
            class="inline-flex items-center justify-center p-2 rounded-md text-gray-600 hover:text-gray-800 dark:text-gray-200 dark:hover:text-white hover:bg-gray-100 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-primary-500"
            :title="isMobileMenuOpen ? 'Close menu' : 'Open menu'"
            aria-label="Open main menu"
          >
            <Menu v-if="!isMobileMenuOpen" class="block h-6 w-6" aria-hidden="true" />
            <X v-else class="block h-6 w-6" aria-hidden="true" />
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
            aria-label="My Profile"
            title="View and edit your profile"
          >
            My Profile
          </router-link>
          <router-link
            to="/user-orders"
            class="block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 dark:text-gray-200 dark:hover:text-white hover:bg-gray-50 dark:hover:bg-gray-700"
            @click="isMobileMenuOpen = false"
            aria-label="My Orders"
            title="View your order history"
          >
            My Orders
          </router-link>
          <button
            @click="handleLogout"
            class="block w-full text-left px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 dark:text-gray-200 dark:hover:text-white hover:bg-gray-50 dark:hover:bg-gray-700"
            aria-label="Log Out"
            title="Sign out of your account"
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
            aria-label="Log In"
          >
            Log In
          </router-link>
          <router-link
            to="/register"
            class="block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:text-gray-900 dark:text-gray-200 dark:hover:text-white hover:bg-gray-50 dark:hover:bg-gray-700"
            @click="isMobileMenuOpen = false"
            aria-label="Sign Up"
          >
            Sign Up
          </router-link>
        </template>
      </div>
    </div>
    <CartModal :is-open="isCartOpen" @close="isCartOpen = false" />
  </nav>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import { useCartStore } from "@/stores/cartStore";
import { useThemeStore } from "@/stores/themeStore";
import CartModal from "@/components/cart/CartModal.vue";
import { ShoppingBag, ShoppingCart, User, Menu, X, Sun, Moon } from "lucide-vue-next";

const router = useRouter();
const authStore = useAuthStore();
const cartStore = useCartStore();
const themeStore = useThemeStore();
const isProfileMenuOpen = ref(false);
const isMobileMenuOpen = ref(false);
const isCartOpen = ref(false);

const isDark = computed(() => themeStore.isDark);

const toggleTheme = () => {
  themeStore.toggleTheme();
};

const handleLogout = async () => {
  try {
    await authStore.logout();
    isProfileMenuOpen.value = false;
    isMobileMenuOpen.value = false;
    router.push("/login");
  } catch (error) {
    console.error(error.message);
  }
};
</script>
