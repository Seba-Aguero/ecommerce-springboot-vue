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
    meta: { requiresAuth: true },
  },
  {
    path: "/order-confirmation",
    name: "order-confirmation",
    component: () => import("@/views/OrderConfirmationView.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/user-orders",
    name: "user-orders",
    component: () => import("@/views/UserOrdersView.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/profile",
    name: "profile",
    component: () => import("@/views/ProfileView.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/admin/products",
    name: "ProductsManagement",
    component: () => import("@/views/admin/ProductsManagement.vue"),
    meta: {
      requiresAuth: true,
      requiresAdmin: true,
    },
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

// Navigation guard to prevent admins from accessing customer-only routes
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  const customerOnlyRoutes = ["/checkout", "/order-confirmation"];

  if (
    authStore.isAdmin &&
    customerOnlyRoutes.some((route) => to.path.startsWith(route))
  ) {
    next("/products");
  } else {
    next();
  }
});

export default router;
