import api from "@/services/api";

export const categoryService = {
  async fetchCategories() {
    return await api.get("/api/v1/categories");
  },
};