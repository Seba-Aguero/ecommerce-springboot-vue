<template>
  <nav class="bg-white shadow-md">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <div class="flex">
          <router-link to="/" class="flex items-center">
            <!-- <img class="h-8 w-auto" src="@/assets/images/logo.png" alt="Logo" /> -->
            <span class="ml-2 text-xl font-bold text-gray-900">Mi Ecommerce</span>
          </router-link>

          <div class="hidden sm:ml-6 sm:flex sm:space-x-8">
            <router-link
              to="/products"
              class="inline-flex items-center px-1 pt-1 text-sm font-medium text-gray-900"
            >
              Productos
            </router-link>
          </div>
        </div>

        <div class="flex items-center">
          <div class="flex-shrink-0">
            <router-link
              to="/cart"
              class="relative p-2 text-gray-600 hover:text-gray-900"
            >
              <i class="fas fa-shopping-cart text-xl"></i>
              <span
                v-if="cartStore.totalItems"
                class="absolute top-0 right-0 -mt-1 -mr-1 px-2 py-1 text-xs font-bold text-white bg-primary-600 rounded-full"
              >
                {{ cartStore.totalItems }}
              </span>
            </router-link>
          </div>

          <div class="hidden sm:ml-6 sm:flex sm:items-center">
            <template v-if="authStore.isAuthenticated">
              <button
                @click="toggleUserMenu"
                class="flex items-center text-sm font-medium text-gray-700 hover:text-gray-900"
              >
                <img
                  class="h-8 w-8 rounded-full"
                  :src="authStore.user?.avatar || 'default-avatar.png'"
                  alt="User avatar"
                />
                <span class="ml-2">{{ authStore.userFullName }}</span>
              </button>
              <!-- Dropdown menu -->
              <div
                v-show="isUserMenuOpen"
                class="absolute right-0 mt-2 w-48 rounded-md shadow-lg py-1 bg-white ring-1 ring-black ring-opacity-5"
              >
                <router-link
                  to="/profile"
                  class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  Mi Perfil
                </router-link>
                <!-- @click="logout" esto va abajo, dentor de button --> 
                <button
                  class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  Cerrar Sesión
                </button>
              </div>
            </template>
            <template v-else>
              <router-link
                to="/login"
                class="text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
              >
                Iniciar Sesión
              </router-link>
              <router-link
                to="/register"
                class="bg-primary-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-primary-700"
              >
                Registrarse
              </router-link>
            </template>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'
// import { useAuthStore } from '@/stores/auth'
// import { useCartStore } from '@/stores/cart'

// const authStore = useAuthStore()
// const cartStore = useCartStore()
const isUserMenuOpen = ref(false)

const toggleUserMenu = () => {
  isUserMenuOpen.value = !isUserMenuOpen.value
}

// const logout = () => {
//   authStore.logout()
//   isUserMenuOpen.value = false
// }
</script>