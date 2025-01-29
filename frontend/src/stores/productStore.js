import { defineStore } from "pinia";
import api from "@/services/api";

export const useProductStore = defineStore("products", {
  state: () => ({
    products: [],
    loading: false,
    error: null,
    currentProduct: null,
    filters: {
      categories: [],
      minPrice: null,
      maxPrice: null,
      search: "",
    },
    totalPages: 0,
    totalElements: 0,
  }),

  getters: {
    filteredProducts: (state) => {
      return state.products.filter((product) => {
        let matches = true;

        if (state.filters.categories.length > 0) {
          // It has to have at least one category
          matches =
            matches &&
            product.categories.some((category) =>
              state.filters.categories.includes(category.id)
            );
        }

        if (state.filters.minPrice) {
          matches = matches && product.price >= state.filters.minPrice;
        }

        if (state.filters.maxPrice) {
          matches = matches && product.price <= state.filters.maxPrice;
        }

        if (state.filters.search) {
          matches =
            matches &&
            (product.name
              .toLowerCase()
              .includes(state.filters.search.toLowerCase()) ||
              product.description
                .toLowerCase()
                .includes(state.filters.search.toLowerCase()));
        }

        // If the product matches all the filters, return true
        return matches;
      });
    },

    availableCategories: (state) => {
      const categoriesSet = new Set();
      state.products.forEach((product) => {
        product.categories.forEach((category) => {
          categoriesSet.add(JSON.stringify(category));
        });
      });
      return Array.from(categoriesSet).map((cat) => JSON.parse(cat));
    },
  },

  actions: {
    async fetchProducts() {
      this.loading = true;
      try {
        const response = await api.get("/api/v1/products");
        this.products = response.data.content;
        this.totalPages = response.data.totalPages;
        this.totalElements = response.data.totalElements;
        return response.data;
      } catch (error) {
        this.error = error.response?.data?.message || error.message;
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async fetchProductById(id) {
      this.loading = true;
      try {
        const response = await api.get(`/api/v1/products/${id}`);
        this.currentProduct = response.data;
      } catch (error) {
        this.error = error.response?.data?.message || error.message;
      } finally {
        this.loading = false;
      }
    },

    setFilters(filters) {
      this.filters = {
        ...this.filters,
        ...filters,
        // If the categories are not provided, set an empty array
        categories: filters.categories || [],
      };
    },

    clearFilters() {
      this.filters = {
        categories: [],
        minPrice: null,
        maxPrice: null,
        search: "",
      };
    },

    toggleCategory(categoryId) {
      const index = this.filters.categories.indexOf(categoryId);
      if (index === -1) {
        this.filters.categories.push(categoryId);
      } else {
        this.filters.categories.splice(index, 1);
      }
    },
  },
});
