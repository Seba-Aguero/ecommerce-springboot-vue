import { defineStore } from "pinia";
import api from "@/services/api";

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
        const response = await api.post("/orders/create", orderData);
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
        const response = await api.get(`/orders/user/${userId}`);
        this.orders = response.data;
      } catch (error) {
        this.error = error.message;
      } finally {
        this.loading = false;
      }
    },

    async getOrderById(orderId) {
      this.loading = true;
      try {
        const response = await api.get(`/orders/${orderId}`);
        this.currentOrder = response.data;
      } catch (error) {
        this.error = error.message;
      } finally {
        this.loading = false;
      }
    },
  },
});
