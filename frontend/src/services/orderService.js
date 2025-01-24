import { useOrderStore } from "@/stores/orderStore";

export const orderService = {
  async createOrder(orderData) {
    const orderStore = useOrderStore();
    try {
      const order = await orderStore.createOrder(orderData);
      return order;
    } catch (error) {
      throw new Error("Error creating order: " + error.message);
    }
  },

  async fetchOrders() {
    const orderStore = useOrderStore();
    try {
      const orders = await orderStore.fetchOrders();
      return orders;
    } catch (error) {
      throw new Error("Error fetching orders: " + error.message);
    }
  },

  async getOrderDetails(orderId) {
    const orderStore = useOrderStore();
    try {
      const orderDetails = await orderStore.getOrderDetails(orderId);
      return orderDetails;
    } catch (error) {
      throw new Error("Error fetching order details: " + error.message);
    }
  },
};
