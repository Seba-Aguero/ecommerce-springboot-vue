import { useCartStore } from "@/stores/cartStore";

export const cartService = {
  async addToCart(product, quantity = 1) {
    const cartStore = useCartStore();
    try {
      await cartStore.addToCart(product, quantity);
      return true;
    } catch (error) {
      throw new Error("Error adding to cart: " + error.message);
    }
  },

  async removeFromCart(productId) {
    const cartStore = useCartStore();
    try {
      await cartStore.removeFromCart(productId);
      return true;
    } catch (error) {
      throw new Error("Error removing from cart: " + error.message);
    }
  },

  async incrementQuantity(productId) {
    const cartStore = useCartStore();
    try {
      await cartStore.incrementQuantity(productId);
      return true;
    } catch (error) {
      throw new Error("Error incrementing quantity: " + error.message);
    }
  },

  async decrementQuantity(productId) {
    const cartStore = useCartStore();
    try {
      await cartStore.decrementQuantity(productId);
      return true;
    } catch (error) {
      throw new Error("Error decrementing quantity: " + error.message);
    }
  },

  async clearCart() {
    const cartStore = useCartStore();
    try {
      await cartStore.clearCart();
      return true;
    } catch (error) {
      throw new Error("Error clearing cart: " + error.message);
    }
  },
};
