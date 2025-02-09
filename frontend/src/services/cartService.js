import api from "@/services/api";

export const cartService = {
  async addToCart(userId, product, quantity = 1) {
    return await api.post(`/api/v1/cart/${userId}/items`, {
      productId: product.id,
      quantity,
    });
  },

  async removeFromCart(userId, productId) {
    return await api.delete(`/api/v1/cart/${userId}/items/${productId}`);
  },

  async incrementQuantity(userId, productId) {
    return await api.patch(
      `/api/v1/cart/${userId}/items/${productId}?operation=INCREMENT`
    );
  },

  async decrementQuantity(userId, productId) {
    return await api.patch(
      `/api/v1/cart/${userId}/items/${productId}?operation=DECREMENT`
    );
  },

  async clearCart(userId) {
    return await api.delete(`/api/v1/cart/${userId}`);
  },
};