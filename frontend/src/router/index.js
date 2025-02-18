import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/authStore";

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("@/views/HomeView.vue"),
  },
  {
    path: "/products",
    name: "products",
    component: () => import("@/views/ProductsView.vue"),
  },
  {
    path: "/product/:id",
    name: "ProductDetail",
    component: () => import("@/views/ProductDetailView.vue"),
  },
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
  {
    path: "/confirm-email",
    name: "ConfirmEmail",
    component: () => import("@/views/ConfirmEmailView.vue"),
  },
  {
    path: "/checkout",
    name: "checkout",
    component: () => import("@/views/CheckoutView.vue"),
  },
  {
    path: "/order-confirmation",
    name: "order-confirmation",
    component: () => import("@/views/OrderConfirmationView.vue"),
  },
  // {
  //   path: "/about",
  //   name: "about",
  //   component: () => import("@/views/AboutView.vue"),
  // },
  // {
  //   path: "/contact",
  //   name: "contact",
  //   component: () => import("@/views/ContactView.vue"),
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
