import { defineStore } from "pinia";
import { orderService } from "@/services/orderService";

export const useOrderStore = defineStore("orders", {
  state: () => ({
    orders: [],
    currentOrder: null,
    loading: false,
    error: null,
  }),

  actions: {
    async createOrder(orderData) {
      this.loading = true;
      try {
        const response = await orderService.createOrder(orderData);
        this.currentOrder = response.data;
        return response.data;
      } catch (error) {
        this.error = error.message;
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async fetchUserOrders(userId) {
      this.loading = true;
      try {
        const response = await orderService.fetchUserOrders(userId);
        this.orders = response.data;
      } catch (error) {
        this.error = error.message;
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async getOrderById(orderId) {
      this.loading = true;
      try {
        const response = await orderService.getOrderById(orderId);
        this.currentOrder = response.data;
      } catch (error) {
        this.error = error.message;
        throw error;
      } finally {
        this.loading = false;
      }
    },
  },
});
