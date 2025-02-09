<template>
  <div
    class="min-h-screen bg-slate-50 dark:bg-gray-900 transition-colors duration-200"
  >
    <Navbar />
    <main class="transition-colors duration-200">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useAuthStore } from "@/stores/authStore";
import { useThemeStore } from "@/stores/themeStore";
import { useCartStore } from "@/stores/cartStore";
import Navbar from "@/components/common/Navbar.vue";
import Footer from "@/components/common/Footer.vue";

const authStore = useAuthStore();
const cartStore = useCartStore();
const themeStore = useThemeStore();

onMounted(async () => {
  themeStore.initTheme();
  // If there is a token, try to load the user profile
  if (localStorage.getItem("token")) {
    try {
      await authStore.fetchUserProfile();
      cartStore.setUserId();
    } catch (error) {
      console.error("Error fetching user profile:", error);
    }
  }
});
</script>

<style>
/* Global transitions for theme */
*,
*::before,
*::after {
  transition-property: color, background-color, border-color,
    text-decoration-color, fill, stroke;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 300ms;
}

/* Exclude elements that should not have transitions */
.no-transition {
  transition: none !important;
}

/* Transition for routes */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 200ms ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
