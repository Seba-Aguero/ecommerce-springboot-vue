import { useProductStore } from "@/stores/productStore";
import { useCartStore } from "@/stores/cartStore";

export const productService = {
  async fetchProducts(filters = {}) {
    const productStore = useProductStore();
    try {
      const products = await productStore.fetchProducts();
      return products;
    } catch (error) {
      throw new Error("Error fetching products: " + error.message);
    }
  },

  async addToCart(product) {
    const cartStore = useCartStore();
    try {
      if (!product.stock) {
        throw new Error("Product out of stock");
      }
      await cartStore.addToCart(product);
    } catch (error) {
      throw new Error("Error adding to cart: " + error.message);
    }
  },
};
