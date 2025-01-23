import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/authStore";

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("@/views/HomeView.vue"),
  },
  // {
  //   path: "/products",
  //   name: "products",
  //   component: () => import("@/views/ProductsView.vue"),
  // },
  // {
  //   path: "/products/:id",
  //   name: "product-detail",
  //   component: () => import("@/views/ProductDetailView.vue"),
  // },
  // {
  //   path: "/cart",
  //   name: "cart",
  //   component: () => import("@/views/CartView.vue"),
  // },
  // {
  //   path: "/checkout",
  //   name: "checkout",
  //   component: () => import("@/views/CheckoutView.vue"),
  //   meta: { requiresAuth: true },
  // },
  {
    path: "/login",
    name: "login",
    component: () => import("@/views/LoginView.vue"),
  },
  {
    path: "/register",
    name: "register",
    component: () => import("@/views/RegisterView.vue"),
  },
  // {
  //   path: "/profile",
  //   name: "profile",
  //   component: () => import("@/views/ProfileView.vue"),
  //   meta: { requiresAuth: true },
  // },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
      next("/login");
  } else {
      next();
  }
});

export default router;
