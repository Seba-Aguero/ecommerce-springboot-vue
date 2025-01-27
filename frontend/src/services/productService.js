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

  async addToCart(product) {
    const cartStore = useCartStore();
    try {
      if (!product.quantity || product.quantity <= 0) {
        throw new Error("Product out of stock");
      }
      await cartStore.addToCart(product);
    } catch (error) {
      throw new Error("Error adding to cart: " + error.message);
    }
  },
};
