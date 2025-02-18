import api from "@/services/api";

export const orderService = {
  async createOrder(orderData) {
    return await api.post("api/v1/orders", orderData);
  },

  async fetchUserOrders(userId) {
    return await api.get(`api/v1/orders/user/${userId}`);
  },

  async getOrderById(orderId) {
    return await api.get(`api/v1/orders/${orderId}`);
  },
};
