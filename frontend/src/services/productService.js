import { useProductStore } from "@/stores/productStore";
import { useCartStore } from "@/stores/cartStore";

export const productService = {
  async fetchProducts(filters = {}) {
    const productStore = useProductStore();
    try {
      const response = await productStore.fetchProducts();
      // Extract the products from the response
      return response.content;
    } catch (error) {
      throw new Error("Error fetching products: " + error.message);
    }
  },

  async getProductById(id) {
    try {
      const productStore = useProductStore();
      const product = await productStore.getProductById(id);
      return product;
    } catch (error) {
      throw new Error("Error fetching product: " + error.message);
    }
  },

  async addToCart(product, quantity = 1) {
    const cartStore = useCartStore();
    try {
      if (!product.quantity || product.quantity <= quantity) {
        throw new Error("Insufficient stock");
      }
      await cartStore.addToCart(product, quantity);
    } catch (error) {
      throw new Error("Error adding to cart: " + error.message);
    }
  }

};
