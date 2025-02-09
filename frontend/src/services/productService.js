import api from "@/services/api";

export const productService = {
  async fetchProducts(params = {}) {
    return await api.get("/api/v1/products", { params });
  },

  async fetchProductById(productId) {
    return await api.get(`/api/v1/products/${productId}`);
  },

  async createProduct(productData) {
    return await api.post("/api/v1/products", productData);
  },

  async updateProduct(productId, productData) {
    return await api.put(`/api/v1/products/${productId}`, productData);
  },

  async deleteProduct(productId) {
    return await api.delete(`/api/v1/products/${productId}`);
  },
};