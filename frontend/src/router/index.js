import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/authStore";
import HomeView from "@/views/HomeView.vue";
import ProductDetailView from "@/views/ProductDetailView.vue";
import ProductsView from "@/views/ProductsView.vue";
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import ConfirmEmailView from "@/views/ConfirmEmailView.vue";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/products",
    name: "products",
    component: ProductsView,
  },
  {
    path: "/product/:id",
    name: "ProductDetail",
    component: ProductDetailView,
  },
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
    component: LoginView,
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView,
  },
  // {
  //   path: "/profile",
  //   name: "profile",
  //   component: () => import("@/views/ProfileView.vue"),
  //   meta: { requiresAuth: true },
  // },
  {
    path: "/confirm-email",
    name: "ConfirmEmail",
    component: ConfirmEmailView,
  },
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
