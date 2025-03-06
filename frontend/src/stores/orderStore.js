import { defineStore } from "pinia";
import { orderService } from "@/services/orderService";

export const useOrderStore = defineStore("orders", {
  state: () => ({
    orders: [],
    orderItems: {},
    currentOrder: null,
    loading: false,
    loadingItems: false,
    error: null,
    lastOrderCompleted: false,
  }),

  getters: {
    isOrderCompleted: (state) => state.lastOrderCompleted && state.currentOrder !== null,
    getItemsForOrder: (state) => (orderId) => {
      return state.orderItems[orderId] || [];
    },
  },

  actions: {
    async createOrder(orderData) {
      this.loading = true;
      try {
        const response = await orderService.createOrder(orderData);
        this.currentOrder = response.data;
        this.lastOrderCompleted = true;
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
      this.error = null;
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

    async fetchOrderItems(orderId) {
      // If the order items are already fetched, don't fetch them again
      if (this.orderItems[orderId]) return;

      this.loadingItems = true;
      try {
        const response = await orderService.getOrderItems(orderId);
        this.orderItems[orderId] = response.data;
      } catch (error) {
        this.orderItems[orderId] = [];
      } finally {
        this.loadingItems = false;
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

    resetOrderStatus() {
      this.lastOrderCompleted = false;
      this.currentOrder = null;
    },
  },
});
