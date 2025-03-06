import api from "@/services/api";

export const configService = {
  async getShippingCost() {
    const response = await api.get("/api/v1/config/shipping-cost");
    return response.data.shippingCost;
  }
};