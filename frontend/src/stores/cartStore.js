import { defineStore } from "pinia";

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
    addToCart(product, quantity = 1) {
      const existingItem = this.items.find((item) => item.id === product.id);

      if (existingItem) {
        existingItem.quantity += quantity;
      } else {
        this.items.push({
          id: product.id,
          name: product.name,
          price: product.price,
          imageUrl: product.imageUrl,
          quantity,
        });
      }
      this.saveCart();
    },

    removeFromCart(productId) {
      const index = this.items.findIndex((item) => item.id === productId);
      if (index > -1) {
        this.items.splice(index, 1);
        this.saveCart();
      }
    },

    incrementQuantity(productId) {
      const item = this.items.find((item) => item.id === productId);
      if (item) {
        item.quantity++;
        this.saveCart();
      }
    },

    decrementQuantity(productId) {
      const item = this.items.find((item) => item.id === productId);
      if (item && item.quantity > 1) {
        item.quantity--;
        this.saveCart();
      }
    },

    // Auxiliary method to save the cart in the local storage
    saveCart() {
      localStorage.setItem("cart", JSON.stringify(this.items));
    }
  },
});
