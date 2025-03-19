import api from "@/services/api";

export const productService = {
  async fetchProducts(params = {}) {
    return await api.get("/api/v1/products", { params });
  },

  async fetchProductById(productId) {
    return await api.get(`/api/v1/products/${productId}`);
  },

  async createProduct(formData) {
    const response = await api.post("/api/v1/products", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  },

  async updateProduct(id, formData) {
    const response = await api.put(`/api/v1/products/${id}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  },

  async deleteProduct(productId) {
    return await api.delete(`/api/v1/products/${productId}`);
  },
};
