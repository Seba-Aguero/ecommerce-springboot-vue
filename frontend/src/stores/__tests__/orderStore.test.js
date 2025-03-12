import { describe, it, expect, vi, beforeEach } from "vitest";
import { useOrderStore } from "../orderStore";
import { orderService } from "@/services/orderService";

describe("Order Store", () => {
  let store;

  const mockOrder = {
    id: 1,
    userId: 1,
    total: 99.99,
    status: "PENDING",
  };

  const mockOrderItems = [
    { id: 1, orderId: 1, productId: 1, quantity: 2, price: 49.99 },
    { id: 2, orderId: 1, productId: 2, quantity: 1, price: 29.99 },
  ];

  beforeEach(() => {
    store = useOrderStore();
    vi.clearAllMocks();
  });

  describe("state", () => {
    it("initializes with default values", () => {
      expect(store.orders).toEqual([]);
      expect(store.orderItems).toEqual({});
      expect(store.currentOrder).toBeNull();
      expect(store.loading).toBe(false);
      expect(store.loadingItems).toBe(false);
      expect(store.error).toBeNull();
      expect(store.lastOrderCompleted).toBe(false);
    });
  });

  describe("getters", () => {
    it("isOrderCompleted returns true when order is completed", () => {
      store.lastOrderCompleted = true;
      store.currentOrder = mockOrder;
      expect(store.isOrderCompleted).toBe(true);
    });

    it("isOrderCompleted returns false when no current order", () => {
      store.lastOrderCompleted = true;
      store.currentOrder = null;
      expect(store.isOrderCompleted).toBe(false);
    });

    it("getItemsForOrder returns items for specific order", () => {
      store.orderItems = { 1: mockOrderItems };
      expect(store.getItemsForOrder(1)).toEqual(mockOrderItems);
    });

    it("getItemsForOrder returns empty array for non-existent order", () => {
      expect(store.getItemsForOrder(999)).toEqual([]);
    });
  });

  describe("actions", () => {
    it("createOrder success", async () => {
      const orderData = { userId: 1, items: [] };
      vi.spyOn(orderService, "createOrder").mockResolvedValue({ data: mockOrder });

      const result = await store.createOrder(orderData);

      expect(result).toEqual(mockOrder);
      expect(store.currentOrder).toEqual(mockOrder);
      expect(store.lastOrderCompleted).toBe(true);
      expect(store.loading).toBe(false);
      expect(orderService.createOrder).toHaveBeenCalledWith(orderData);
    });

    it("createOrder handles error", async () => {
      const errorMessage = "Failed to create order";
      vi.spyOn(orderService, "createOrder").mockRejectedValue(new Error(errorMessage));

      await expect(store.createOrder({})).rejects.toThrow(errorMessage);
      expect(store.error).toBe(errorMessage);
      expect(store.loading).toBe(false);
    });

    it("fetchUserOrders success", async () => {
      vi.spyOn(orderService, "fetchUserOrders").mockResolvedValue({
        data: [mockOrder],
      });

      await store.fetchUserOrders(1);

      expect(store.orders).toEqual([mockOrder]);
      expect(store.loading).toBe(false);
      expect(store.error).toBeNull();
      expect(orderService.fetchUserOrders).toHaveBeenCalledWith(1);
    });

    it("fetchOrderItems skips if already loaded", async () => {
      store.orderItems = { 1: mockOrderItems };
      vi.spyOn(orderService, "getOrderItems");

      await store.fetchOrderItems(1);

      expect(orderService.getOrderItems).not.toHaveBeenCalled();
      expect(store.orderItems[1]).toEqual(mockOrderItems);
    });

    it("fetchOrderItems success", async () => {
      vi.spyOn(orderService, "getOrderItems").mockResolvedValue({
        data: mockOrderItems,
      });

      await store.fetchOrderItems(1);

      expect(store.orderItems[1]).toEqual(mockOrderItems);
      expect(store.loadingItems).toBe(false);
      expect(orderService.getOrderItems).toHaveBeenCalledWith(1);
    });

    it("fetchOrderItems handles error", async () => {
      vi.spyOn(orderService, "getOrderItems").mockRejectedValue(new Error());

      await store.fetchOrderItems(1);

      expect(store.orderItems[1]).toEqual([]);
      expect(store.loadingItems).toBe(false);
    });

    it("getOrderById success", async () => {
      vi.spyOn(orderService, "getOrderById").mockResolvedValue({
        data: mockOrder,
      });

      await store.getOrderById(1);

      expect(store.currentOrder).toEqual(mockOrder);
      expect(store.loading).toBe(false);
      expect(orderService.getOrderById).toHaveBeenCalledWith(1);
    });

    it("resetOrderStatus resets state correctly", () => {
      store.lastOrderCompleted = true;
      store.currentOrder = mockOrder;

      store.resetOrderStatus();

      expect(store.lastOrderCompleted).toBe(false);
      expect(store.currentOrder).toBeNull();
    });
  });
});
