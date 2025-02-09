import api from "@/services/api";

export const orderService = {
  async createOrder(orderData) {
    return await api.post("/orders/create", orderData);
  },

  async fetchUserOrders(userId) {
    return await api.get(`/orders/user/${userId}`);
  },

  async getOrderById(orderId) {
    return await api.get(`/orders/${orderId}`);
  },
};
