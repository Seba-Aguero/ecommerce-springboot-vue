import { describe, it, expect, vi, beforeEach } from "vitest";
import { useCartStore } from "../cartStore";
import { cartService } from "@/services/cartService";

describe("Cart Store", () => {
  beforeEach(() => {
    localStorage.clear();
    vi.clearAllMocks();
  });

  const mockProduct = {
    id: 1,
    name: "Test Product",
    price: 99.99,
    stock: 10,
  };

  describe("state", () => {
    it("initializes with empty cart", () => {
      const store = useCartStore();
      expect(store.items).toEqual([]);
      expect(store.totalAmount).toBe(0);
    });

    it("loads persisted cart from localStorage", () => {
      const mockCart = [{ ...mockProduct, quantity: 2 }];
      localStorage.setItem("cart", JSON.stringify(mockCart));

      const store = useCartStore();
      expect(store.items).toEqual(mockCart);
    });
  });

  describe("actions", () => {
    it("adds item to cart", async () => {
      const store = useCartStore();
      vi.spyOn(cartService, "addToCart").mockResolvedValue({});

      await store.addToCart(mockProduct);

      expect(store.items).toHaveLength(1);
      expect(store.items[0].id).toBe(mockProduct.id);
      expect(store.items[0].quantity).toBe(1);
      // Here null as userId since there is no user logged in, and quantity is 1 by default
      expect(cartService.addToCart).toHaveBeenCalledWith(null, mockProduct.id, 1);
    });

    it("removes item from cart", async () => {
      const store = useCartStore();
      await store.addToCart(mockProduct);
      vi.spyOn(cartService, "removeFromCart").mockResolvedValue({});

      await store.removeFromCart(mockProduct.id);

      expect(store.items).toHaveLength(0);
      expect(cartService.removeFromCart).toHaveBeenCalledWith(null, mockProduct.id);
    });

    it("increments quantity", async () => {
      const store = useCartStore();
      await store.addToCart(mockProduct);
      vi.spyOn(cartService, "incrementQuantity").mockResolvedValue({});

      await store.incrementQuantity(mockProduct.id);

      expect(store.items[0].quantity).toBe(2);
      expect(cartService.incrementQuantity).toHaveBeenCalledWith(null, mockProduct.id);
    });

    it("decrements quantity", async () => {
      const store = useCartStore();
      await store.addToCart(mockProduct);
      store.items[0].quantity = 2;
      vi.spyOn(cartService, "decrementQuantity").mockResolvedValue({});

      await store.decrementQuantity(mockProduct.id);

      expect(store.items[0].quantity).toBe(1);
      expect(cartService.decrementQuantity).toHaveBeenCalledWith(null, mockProduct.id);
    });

    it("clears cart", async () => {
      const store = useCartStore();
      await store.addToCart(mockProduct);
      vi.spyOn(cartService, "clearCart").mockResolvedValue({});

      await store.clearCart();

      expect(store.items).toHaveLength(0);
      expect(localStorage.getItem("cart")).toBe("[]");
      expect(cartService.clearCart).toHaveBeenCalledWith(null);
    });
  });

  describe("getters", () => {
    it("calculates total amount correctly", async () => {
      const store = useCartStore();
      vi.spyOn(cartService, "addToCart").mockResolvedValue({});
      await store.addToCart(mockProduct);
      await store.addToCart(mockProduct);

      expect(store.totalAmount).toBe(199.98); // 99.99 * 2
    });

    it("calculates total items correctly", async () => {
      const store = useCartStore();
      vi.spyOn(cartService, "addToCart").mockResolvedValue({});
      await store.addToCart(mockProduct);
      await store.addToCart({ ...mockProduct, id: 2 });

      expect(store.totalItems).toBe(2);
    });
  });
});
