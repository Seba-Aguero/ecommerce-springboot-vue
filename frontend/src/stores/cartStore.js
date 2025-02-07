import { defineStore } from "pinia";
import api from "@/services/api";

export const useCartStore = defineStore("cart", {
  state: () => ({
    items: JSON.parse(localStorage.getItem("cart")) || [],
  }),

  getters: {
    totalItems: (state) => state.items.reduce((sum, item) => sum + item.quantity, 0),
    totalAmount: (state) =>
      state.items.reduce((sum, item) => sum + item.price * item.quantity, 0),
  },

  actions: {
    async addToCart(product, quantity = 1) {
      try {
        // First update backend
        await api.post(`/api/v1/cart/${this.userId}/items`, {
          productId: product.id,
          quantity,
        });
        // Then update local state
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
      try {
        await api.delete(`/api/v1/cart/${this.userId}/items/${productId}`);
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
      try {
        await api.patch(
          `/api/v1/cart/${this.userId}/items/${productId}?operation=INCREMENT`
        );
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
      try {
        await api.patch(`/api/v1/cart/${this.userId}/items/${productId}?operation=DECREMENT`);
        const item = this.items.find(item => item.id === productId);
        if (item && item.quantity > 1) {
          item.quantity--;
          this.saveCart();
        }
      } catch (error) {
        throw new Error("Error decrementing quantity: " + error.message);
      }
    },

    async clearCart() {
      try {
        await api.delete(`/api/v1/cart/${this.userId}`);
        this.items = [];
        this.saveCart();
      } catch (error) {
        throw new Error("Error clearing cart: " + error.message);
      }
    },

    // Auxiliary method to save the cart in the local storage
    saveCart() {
      localStorage.setItem("cart", JSON.stringify(this.items));
    },
  },
});
