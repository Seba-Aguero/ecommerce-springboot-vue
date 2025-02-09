import { defineStore } from "pinia";
import { productService } from "@/services/productService";

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
    currentPage: 0,
  }),

  getters: {
    filteredProducts: (state) => {
      return state.products.filter((product) => {
        let matches = true;

        if (state.filters.categories.length > 0) {
          // Must have at least one category
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
            (product.name.toLowerCase().includes(state.filters.search.toLowerCase()) ||
              product.description
                .toLowerCase()
                .includes(state.filters.search.toLowerCase()));
        }

        // If the product matches all the filters, this returns true
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
    async fetchProducts(params = {}) {
      this.loading = true;
      try {
        const queryParams = new URLSearchParams();

        // Add page and size to query params
        queryParams.append("page", params.page || 0);
        queryParams.append("size", 9);

        // Add all filters to query params
        if (this.filters.categories.length > 0) {
          queryParams.append("categories", this.filters.categories.join(","));
        }
        if (this.filters.minPrice && this.filters.minPrice > 0) {
          queryParams.append("minPrice", this.filters.minPrice);
        }
        if (this.filters.maxPrice && this.filters.maxPrice > 0) {
          queryParams.append("maxPrice", this.filters.maxPrice);
        }
        if (this.filters.search) {
          queryParams.append("search", this.filters.search);
        }

        const response = await productService.fetchProducts(queryParams);

        // Update the state with the fetched data
        this.products = response.data.content;
        this.totalPages = response.data.totalPages;
        this.totalElements = response.data.totalElements;
        this.currentPage = response.data.number;

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
        const response = await productService.fetchProductById(id);
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

    async updatePriceRange(minPrice, maxPrice) {
      this.filters.minPrice = minPrice || null;
      this.filters.maxPrice = maxPrice || null;
      await this.fetchProducts();
    },

    async updateSearch(search) {
      this.filters.search = search;
      await this.fetchProducts();
    },

    async toggleCategory(categoryId) {
      const index = this.filters.categories.indexOf(categoryId);
      if (index === -1) {
        this.filters.categories.push(categoryId);
      } else {
        this.filters.categories.splice(index, 1);
      }
      await this.fetchProducts();
    },

    async clearFilters() {
      this.filters = {
        categories: [],
        minPrice: null,
        maxPrice: null,
        search: "",
      };
      await this.fetchProducts();
    },
  },
});
