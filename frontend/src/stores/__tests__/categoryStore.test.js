import { describe, it, expect, vi, beforeEach } from "vitest";
import { useCategoryStore } from "../categoryStore";
import { categoryService } from "@/services/categoryService";

describe("Category Store", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const mockCategories = [
    { id: 1, name: "Electronics" },
    { id: 2, name: "Books" },
  ];

  describe("state", () => {
    it("initializes with empty categories", () => {
      const store = useCategoryStore();
      expect(store.categories).toEqual([]);
      expect(store.loading).toBe(false);
      expect(store.error).toBeNull();
    });
  });

  describe("actions", () => {
    it("fetches categories successfully", async () => {
      const store = useCategoryStore();
      vi.spyOn(categoryService, "fetchCategories").mockResolvedValue({
        data: mockCategories,
      });

      await store.fetchCategories();

      expect(store.categories).toEqual(mockCategories);
      expect(store.loading).toBe(false);
      expect(store.error).toBeNull();
      expect(categoryService.fetchCategories).toHaveBeenCalled();
    });

    it("handles fetch error correctly", async () => {
      const store = useCategoryStore();
      const errorMessage = "Failed to fetch categories";
      vi.spyOn(categoryService, "fetchCategories").mockRejectedValue(
        new Error(errorMessage)
      );

      await expect(store.fetchCategories()).rejects.toThrow(errorMessage);

      expect(store.categories).toEqual([]);
      expect(store.loading).toBe(false);
      expect(store.error).toBe(errorMessage);
    });

    it("sets loading state while fetching", async () => {
      const store = useCategoryStore();
      vi.spyOn(categoryService, "fetchCategories").mockResolvedValue({
        data: mockCategories,
      });

      const fetchPromise = store.fetchCategories();
      expect(store.loading).toBe(true);

      await fetchPromise;
      expect(store.loading).toBe(false);
    });
  });
});
