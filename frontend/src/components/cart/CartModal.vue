<template>
  <!-- Dark background with fade transition -->
  <transition name="fade">
    <div
      v-if="isOpen"
      class="fixed inset-0 bg-black bg-opacity-50 z-40"
      @click="$emit('close')"
    ></div>
  </transition>

  <!-- Cart panel with slide transition -->
  <transition name="slide">
    <div
      v-if="isOpen"
      class="fixed inset-y-0 right-0 w-80 md:w-96 bg-white dark:bg-gray-800 shadow-xl z-50"
    >
      <div class="relative h-full bg-white dark:bg-gray-800 shadow-xl">
        <!-- Header -->
        <div class="p-4 border-b dark:border-gray-700 flex justify-between items-center">
          <h2 class="text-lg font-semibold text-gray-900 dark:text-white">
            Shopping Cart
          </h2>
          <button
            @click="$emit('close')"
            class="text-gray-500 hover:text-gray-700 dark:hover:text-gray-300 transition duration-0"
            title="Close cart"
          >
            <X class="h-6 w-6" aria-hidden="true" />
          </button>
        </div>

        <!-- Checkout situation -->
        <div v-if="isInCheckoutRoute" class="p-4 flex justify-center items-center h-full">
          <div class="bg-blue-50 dark:bg-gray-700 p-4 rounded-lg">
            <div class="text-gray-600 dark:text-gray-300 mb-4 space-y-1">
              <p class="font-semibold">You are currently in the checkout process.</p>
              <p class="text-sm">Do you want to continue or cancel?</p>
            </div>
            <div class="space-y-2">
              <button
                @click="$emit('close')"
                class="w-full bg-primary-600 text-white px-4 py-2 rounded hover:bg-primary-700"
              >
                Continue with Checkout
              </button>
              <button
                @click="handleCancelCheckout"
                class="w-full bg-gray-200 text-gray-700 px-4 py-2 rounded hover:bg-gray-300"
              >
                Cancel and continue shopping
              </button>
            </div>
          </div>
        </div>

        <!-- Cart content -->
        <div v-else>
          <!-- Cart Items -->
          <div class="px-4 overflow-y-auto h-[calc(100vh-180px)] custom-scrollbar">
            <div
              v-if="!cartStore.items.length"
              class="text-center text-xl text-gray-500 dark:text-gray-400 mt-4"
            >
              Your cart is empty!
            </div>

            <div v-else>
              <CartItem
                v-for="item in cartStore.items"
                :key="item.id"
                :item="item"
                @increment="handleIncrementQuantity"
                @decrement="handleDecrementQuantity"
                @remove="handleRemoveFromCart"
              />
            </div>
          </div>

          <!-- Total and footer buttons -->
          <div
            class="absolute bottom-0 w-full p-4 border-t dark:border-gray-700 bg-white dark:bg-gray-800"
          >
            <CartSummary
              :total-amount="cartStore.totalAmount"
              :shipping-cost="shippingCost"
            >
              <div class="flex gap-x-2">
                <button
                  @click="handleCheckout"
                  :disabled="!cartStore.items.length"
                  class="bg-primary-600 text-white w-[70%] md:w-3/4 px-2 py-1 md:px-4 md:py-2 rounded hover:bg-primary-700 flex items-center gap-x-2 justify-center disabled:opacity-40"
                  title="Proceed to checkout"
                >
                  <CreditCard class="h-4 w-4" aria-hidden="true" /> Checkout
                </button>
                <button
                  @click="showClearConfirmation = true"
                  :disabled="!cartStore.items.length"
                  class="bg-red-700 text-white w-[30%] md:w-1/4 px-1 py-1 md:px-2 rounded hover:bg-red-800 flex items-center gap-x-2 justify-center disabled:opacity-40"
                  title="Clear all items from cart"
                >
                  <Trash2 class="h-4 w-4" aria-hidden="true" /> Clear
                </button>
              </div>
            </CartSummary>
          </div>

          <!-- Clear Confirmation Modal -->
          <ConfirmationModal
            :is-open="showClearConfirmation"
            message="Are you sure you want to clear the cart?"
            confirm-text="Yes, Clear"
            @confirm="handleClearCart"
            @cancel="showClearConfirmation = false"
          />
        </div>

        <!-- Cancel Checkout Confirmation Modal -->
        <ConfirmationModal
          :is-open="showCancelCheckoutConfirmation"
          message="Are you sure you want to cancel the checkout?"
          confirm-text="Yes, Cancel"
          @confirm="confirmCancelCheckout"
          @cancel="showCancelCheckoutConfirmation = false"
        />
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref, computed } from "vue";
import { X, Trash2, CreditCard } from "lucide-vue-next";
import { useCartStore } from "@/stores/cartStore";
import { useRouter } from "vue-router";
import { useToast } from "vue-toastification";
import ConfirmationModal from "@/components/common/ConfirmationModal.vue";
import { useShippingCost } from "@/composables/useShippingCost";
import { useAuthStore } from "@/stores/authStore";
import CartItem from "@/components/cart/CartItem.vue";
import CartSummary from "@/components/cart/CartSummary.vue";

const router = useRouter();
const cartStore = useCartStore();
const toast = useToast();
const showClearConfirmation = ref(false);
const showCancelCheckoutConfirmation = ref(false);
const { shippingCost } = useShippingCost();
const authStore = useAuthStore();

defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(["close"]);

const handleIncrementQuantity = async (productId) => {
  try {
    await cartStore.incrementQuantity(productId);
  } catch (error) {
    toast.error("Failed to increase quantity. Please try again.");
  }
};

const handleDecrementQuantity = async (productId) => {
  try {
    await cartStore.decrementQuantity(productId);
  } catch (error) {
    toast.error("Failed to decrease quantity. Please try again.");
  }
};

const handleRemoveFromCart = async (productId) => {
  try {
    await cartStore.removeFromCart(productId);
  } catch (error) {
    toast.error("Failed to remove item from cart. Please try again.");
  }
};

const handleCheckout = () => {
  if (authStore.isAdmin) {
    toast.error("Administrators cannot place orders. Please use a customer account.");
    return;
  }

  if (cartStore.items.length) {
    emit("close");
    router.push("/checkout");
  } else {
    toast.error("Your cart is empty!");
  }
};

const handleClearCart = async () => {
  try {
    await cartStore.clearCart();
    showClearConfirmation.value = false;
  } catch (error) {
    toast.error("Failed to clear cart. Please try again.");
  }
};

const isInCheckoutRoute = computed(() =>
  router.currentRoute.value.path.includes("checkout")
);

const handleCancelCheckout = () => {
  showCancelCheckoutConfirmation.value = true;
};

const confirmCancelCheckout = () => {
  showCancelCheckoutConfirmation.value = false;
  emit("close");
  router.push("/products");
};
</script>

<style scoped>
/* Fade transition for the background overlay */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Slide transition for the cart panel */
.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}
.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
}
</style>
