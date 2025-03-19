import { ref, onMounted } from 'vue';
import { configService } from '@/services/configService';

export function useShippingCost() {
  const shippingCost = ref(0);
  const loading = ref(true);
  const error = ref(null);

  const fetchShippingCost = async () => {
    try {
      loading.value = true;
      shippingCost.value = await configService.getShippingCost();
    } catch (err) {
      error.value = err;
      console.error("❌ Failed to fetch shipping cost:", err);
    } finally {
      loading.value = false;
    }
  };

  onMounted(fetchShippingCost);

  return {
    shippingCost,
    loading,
    error,
    fetchShippingCost
  };
}