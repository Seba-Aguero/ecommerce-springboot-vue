import { describe, it, expect, vi, beforeEach } from "vitest";
import { useProductStore } from "../productStore";
import { productService } from "@/services/productService";

describe("Product Store", () => {
  let store;

  const mockProduct = {
    id: 1,
    name: "Test Product",
    description: "Test Description",
    price: 99.99,
    totalStock: 10,
    categories: [{ id: 1, name: "Electronics" }],
  };

  const mockPaginatedResponse = {
    content: [mockProduct],
    totalPages: 2,
    totalElements: 15,
    number: 0,
  };

  beforeEach(() => {
    store = useProductStore();
    vi.clearAllMocks();
  });

  describe("state", () => {
    it("initializes with default values", () => {
      expect(store.products).toEqual([]);
      expect(store.loading).toBe(false);
      expect(store.error).toBeNull();
      expect(store.currentProduct).toBeNull();
      expect(store.filters).toEqual({
        categories: [],
        minPrice: null,
        maxPrice: null,
        search: "",
      });
      expect(store.totalPages).toBe(0);
      expect(store.totalElements).toBe(0);
      expect(store.currentPage).toBe(0);
    });
  });

  describe("getters", () => {
    it("filteredProducts returns products filtered by categories", () => {
      store.products = [
        { ...mockProduct, categories: [{ id: 1, name: "Electronics" }] },
        { ...mockProduct, id: 2, categories: [{ id: 2, name: "Books" }] },
      ];
      store.filters.categories = [1];

      const filtered = store.filteredProducts;
      expect(filtered).toHaveLength(1);
      expect(filtered[0].id).toBe(1);
    });

    it("filteredProducts returns all products when no filters", () => {
      store.products = [mockProduct, { ...mockProduct, id: 2 }];

      const filtered = store.filteredProducts;
      expect(filtered).toHaveLength(2);
    });
  });

  describe("actions", () => {
    it("fetchProducts success", async () => {
      vi.spyOn(productService, "fetchProducts").mockResolvedValue({
        data: mockPaginatedResponse,
      });

      await store.fetchProducts();

      expect(store.products).toEqual([mockProduct]);
      expect(store.totalPages).toBe(2);
      expect(store.totalElements).toBe(15);
      expect(store.currentPage).toBe(0);
      expect(store.loading).toBe(false);
      expect(store.error).toBeNull();
    });

    it("fetchProducts with params", async () => {
      vi.spyOn(productService, "fetchProducts").mockResolvedValue({
        data: mockPaginatedResponse,
      });

      const params = { page: 1 };
      await store.fetchProducts(params);

      expect(productService.fetchProducts).toHaveBeenCalled();
      expect(store.products).toEqual([mockProduct]);
    });

    it("fetchProducts handles error", async () => {
      const errorMessage = "Failed to fetch products";
      vi.spyOn(productService, "fetchProducts").mockRejectedValue({
        message: errorMessage,
      });

      try {
        await store.fetchProducts();
      } catch (error) {
        expect(store.error).toBe(errorMessage);
        expect(store.loading).toBe(false);
        expect(store.products).toEqual([]);
      }
    });

    it("fetchProductById success", async () => {
      vi.spyOn(productService, "fetchProductById").mockResolvedValue({
        data: mockProduct,
      });

      await store.fetchProductById(1);

      expect(store.currentProduct).toEqual(mockProduct);
      expect(store.loading).toBe(false);
      expect(store.error).toBeNull();
      expect(productService.fetchProductById).toHaveBeenCalledWith(1);
    });

    it("fetchProductById handles error", async () => {
      const errorMessage = "Product not found";
      vi.spyOn(productService, "fetchProductById").mockRejectedValue({
        message: errorMessage,
      });

      await store.fetchProductById(999);

      expect(store.error).toBe(errorMessage);
      expect(store.loading).toBe(false);
      expect(store.currentProduct).toBeNull();
    });

    it("setFilters updates filters correctly", () => {
      const newFilters = {
        categories: [1],
        minPrice: 10,
        maxPrice: 100,
        search: "test",
      };

      store.setFilters(newFilters);

      expect(store.filters).toEqual(newFilters);
    });

    it("clearFilters resets filters to default and fetches products", async () => {
      vi.spyOn(productService, "fetchProducts").mockResolvedValue({
        data: mockPaginatedResponse
      });

      store.filters = {
        categories: [1],
        minPrice: 10,
        maxPrice: 100,
        search: "test",
      };

      await store.clearFilters();

      expect(store.filters).toEqual({
        categories: [],
        minPrice: null,
        maxPrice: null,
        search: "",
      });
    });
  });
});
