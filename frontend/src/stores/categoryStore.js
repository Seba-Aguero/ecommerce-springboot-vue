import { defineStore } from "pinia";
import { categoryService } from "@/services/categoryService";

export const useCategoryStore = defineStore("categories", {
  state: () => ({
    categories: [],
    loading: false,
    error: null,
  }),

  actions: {
    async fetchCategories() {
      this.loading = true;
      try {
        const response = await categoryService.fetchCategories();
        this.categories = response.data;
      } catch (error) {
        this.error = error.message;
        throw error;
      } finally {
        this.loading = false;
      }
    },
  },
});