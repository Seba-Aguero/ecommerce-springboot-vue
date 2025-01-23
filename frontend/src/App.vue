<template>
  <div
    class="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-200"
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
import Navbar from "@/components/common/Navbar.vue";
import Footer from "@/components/common/Footer.vue";

const authStore = useAuthStore();

onMounted(async () => {
  // If there is a token, try to load the user profile
  if (localStorage.getItem("token")) {
    try {
      await authStore.fetchUserProfile();
    } catch (error) {
      console.error("Error al cargar el perfil:", error);
    }
  }
});
</script>

<style>
/* Transiciones globales para el tema */
*,
*::before,
*::after {
  transition-property: color, background-color, border-color,
    text-decoration-color, fill, stroke;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 300ms;
}

/* Excluir elementos que no deberían tener transición */
.no-transition {
  transition: none !important;
}

/* Transición para las rutas */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 200ms ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
