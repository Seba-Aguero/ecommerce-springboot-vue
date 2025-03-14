import { defineStore } from "pinia";
import { cartService } from "@/services/cartService";
import { useAuthStore } from "@/stores/authStore";

export const useCartStore = defineStore("cart", {
  state: () => ({
    userId: null,
    // These items are actually the products in the cart
    items: JSON.parse(localStorage.getItem("cart")) || [],
  }),

  getters: {
    totalItems: (state) => state.items.reduce((sum, item) => sum + item.quantity, 0),
    totalAmount: (state) =>
      state.items.reduce((sum, item) => sum + item.price * item.quantity, 0),
  },

  actions: {
    // Helper function to check if user is admin
    checkAdminRestriction(actionName) {
      const authStore = useAuthStore();
      if (authStore.isAdmin) {
        throw new Error(
          `Administrators cannot ${actionName}. Please use a customer account.`
        );
      }
    },

    setUserId() {
      const authStore = useAuthStore();
      this.userId = authStore.user?.id || null;
    },

    async addToCart(product, quantity = 1) {
      this.checkAdminRestriction("add items to cart");

      try {
        await cartService.addToCart(this.userId, product.id, quantity);
        const existingItem = this.items.find((item) => item.id === product.id);
        if (existingItem) {
          existingItem.quantity += quantity;
        } else {
          this.items.push({ ...product, quantity });
        }
        this.saveCart();
      } catch (error) {
        throw new Error("Error adding to cart: " + error.message);
      }
    },

    async removeFromCart(productId) {
      this.checkAdminRestriction("remove items from cart");

      try {
        await cartService.removeFromCart(this.userId, productId);
        const index = this.items.findIndex((item) => item.id === productId);
        if (index > -1) {
          this.items.splice(index, 1);
          this.saveCart();
        }
      } catch (error) {
        throw new Error("Error removing from cart: " + error.message);
      }
    },

    async incrementQuantity(productId) {
      this.checkAdminRestriction("modify cart");

      try {
        await cartService.incrementQuantity(this.userId, productId);
        const item = this.items.find((item) => item.id === productId);
        if (item) {
          item.quantity++;
          this.saveCart();
        }
      } catch (error) {
        throw new Error("Error incrementing quantity: " + error.message);
      }
    },

    async decrementQuantity(productId) {
      this.checkAdminRestriction("modify cart");

      try {
        await cartService.decrementQuantity(this.userId, productId);
        const item = this.items.find((item) => item.id === productId);
        if (item && item.quantity > 1) {
          item.quantity--;
          this.saveCart();
        }
      } catch (error) {
        throw new Error("Error decrementing quantity: " + error.message);
      }
    },

    async clearCart() {
      this.checkAdminRestriction("clear cart");

      try {
        await cartService.clearCart(this.userId);
        this.items = [];
        this.saveCart();
      } catch (error) {
        throw new Error("Error clearing cart: " + error.message);
      }
    },

    saveCart() {
      localStorage.setItem("cart", JSON.stringify(this.items));
    },
  },
});
