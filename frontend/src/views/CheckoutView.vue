<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <!-- Progress Steps -->
    <div class="mb-8">
      <ol class="flex items-center justify-center" role="list">
        <li
          v-for="(step, index) in steps"
          :key="step.id"
          class="flex items-center relative mb-4"
          :aria-current="currentStep === index ? 'step' : undefined"
        >
          <div class="flex flex-col items-center">
            <div
              :class="[
                'w-8 h-8 rounded-full flex items-center justify-center',
                currentStep >= index
                  ? 'bg-primary-600 text-white'
                  : 'bg-gray-200 text-gray-600',
              ]"
            >
              {{ index + 1 }}
            </div>
            <span
              class="absolute top-10 text-sm whitespace-nowrap"
              :class="currentStep >= index ? 'text-primary-600' : 'text-gray-500'"
            >
              {{ step.name }}
            </span>
          </div>
          <!-- If it's not the last step, add a line -->
          <div
            v-if="index < steps.length - 1"
            class="h-0.5 w-16 mx-2"
            :class="currentStep > index ? 'bg-primary-600' : 'bg-gray-200'"
          ></div>
        </li>
      </ol>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <!-- Main Content -->
      <div class="lg:col-span-2">
        <!-- Shipping Information -->
        <div
          v-show="currentStep === 0"
          class="bg-white dark:bg-gray-800 rounded-lg shadow p-6"
        >
          <h2 class="text-xl font-semibold mb-6 text-gray-900 dark:text-white">
            Shipping Information
          </h2>
          <form @submit.prevent="nextStep" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <Label for="firstName">First Name</Label>
                <Input
                  id="firstName"
                  v-model="shippingInfo.firstName"
                  type="text"
                  required
                  placeholder="Enter your first name"
                  :icon="User"
                />
              </div>
              <div>
                <Label for="lastName">Last Name</Label>
                <Input
                  id="lastName"
                  v-model="shippingInfo.lastName"
                  type="text"
                  required
                  placeholder="Enter your last name"
                  :icon="User"
                />
              </div>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div class="md:col-span-2">
                <Label for="address">Street Address</Label>
                <Input
                  id="address"
                  v-model="shippingInfo.address"
                  type="text"
                  required
                  placeholder="Enter your street address"
                  :icon="Home"
                />
              </div>
              <div>
                <Label for="phone">Phone</Label>
                <Input
                  id="phone"
                  v-model="shippingInfo.phone"
                  type="tel"
                  required
                  placeholder="555-5555"
                  :icon="Phone"
                />
              </div>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <Label for="city">City</Label>
                <Input
                  id="city"
                  v-model="shippingInfo.city"
                  type="text"
                  required
                  placeholder="Enter your city"
                  :icon="Building"
                />
              </div>
              <div>
                <Label for="state">State</Label>
                <Select
                  id="state"
                  v-model="shippingInfo.state"
                  :options="states"
                  required
                  placeholder="Select State"
                  :icon="MapPinned"
                />
              </div>
              <div>
                <Label for="zipCode">ZIP Code</Label>
                <Input
                  id="zipCode"
                  v-model="shippingInfo.zipCode"
                  type="text"
                  required
                  placeholder="Enter ZIP code"
                  :icon="MapPin"
                />
              </div>
            </div>
            <div>
              <div class="flex justify-end mt-9">
                <button
                  type="submit"
                  class="bg-primary-600 text-white px-6 py-2 rounded-md hover:bg-primary-700"
                >
                  Continue to Payment
                </button>
              </div>
            </div>
          </form>
        </div>

        <!-- Payment Information -->
        <div
          v-show="currentStep === 1"
          class="bg-white dark:bg-gray-800 rounded-lg shadow p-6"
        >
          <h2 class="text-xl font-semibold mb-6 text-gray-900 dark:text-white">
            Payment Information
          </h2>
          <form @submit.prevent="nextStep" class="space-y-4">
            <div>
              <Label for="cardNumber">Card Number</Label>
              <Input
                id="cardNumber"
                v-model="paymentInfo.cardNumber"
                type="text"
                required
                placeholder="Enter card number"
                :icon="CreditCard"
              />
            </div>
            <div class="grid grid-cols-3 gap-4">
              <div class="col-span-2">
                <Label for="expiryDate">Expiration Date</Label>
                <Input
                  id="expiryDate"
                  v-model="paymentInfo.expiryDate"
                  type="text"
                  required
                  placeholder="MM/YY"
                  :icon="Calendar"
                />
              </div>
              <div>
                <Label for="cvv">CVV</Label>
                <Input
                  id="cvv"
                  v-model="paymentInfo.cvv"
                  type="text"
                  required
                  placeholder="CVV"
                  :icon="Lock"
                />
              </div>
            </div>
            <div class="flex justify-between pt-5">
              <button
                type="button"
                @click="currentStep--"
                class="text-gray-600 hover:text-gray-800 dark:text-gray-400 dark:hover:text-gray-200 flex items-center gap-x-1"
              >
                <ChevronLeft class="h-4 w-4" />
                Back
              </button>
              <button
                type="submit"
                class="bg-primary-600 text-white px-6 py-2 rounded-md hover:bg-primary-700"
              >
                Review Order
              </button>
            </div>
          </form>
        </div>

        <!-- Order Review -->
        <div
          v-show="currentStep === 2"
          class="bg-white dark:bg-gray-800 rounded-lg shadow p-6"
        >
          <h2 class="text-xl font-semibold mb-6 text-gray-900 dark:text-white">
            Review Order
          </h2>
          <div class="space-y-6">
            <!-- Shipping Details -->
            <div>
              <h3 class="text-lg font-medium text-gray-900 dark:text-white">
                Shipping Details
              </h3>
              <div class="mt-2 text-sm text-gray-600 dark:text-gray-400">
                <p>{{ shippingInfo.firstName }} {{ shippingInfo.lastName }}</p>
                <p>{{ shippingInfo.address }}</p>
                <p>
                  {{ shippingInfo.city }}, {{ shippingInfo.state }}
                  {{ shippingInfo.zipCode }}
                </p>
              </div>
            </div>

            <!-- Payment Details -->
            <div>
              <h3 class="text-lg font-medium text-gray-900 dark:text-white">
                Payment Details
              </h3>
              <div class="mt-2 text-sm text-gray-600 dark:text-gray-400">
                <p>Card ending in {{ paymentInfo.cardNumber.slice(-4) }}</p>
                <p>Expires: {{ paymentInfo.expiryDate }}</p>
              </div>
            </div>

            <div class="flex justify-between">
              <button
                type="button"
                @click="currentStep--"
                class="text-gray-600 hover:text-gray-800 dark:text-gray-400 dark:hover:text-gray-200 flex items-center gap-x-1"
              >
                <ChevronLeft class="h-4 w-4" />
                Back
              </button>
              <button
                @click="placeOrder"
                :disabled="loading"
                class="bg-primary-600 text-white px-6 py-2 rounded-md hover:bg-primary-700 disabled:opacity-50 flex items-center justify-center"
              >
                <ButtonSpinner v-if="loading"> Processing Order... </ButtonSpinner>
                <span v-else> Place Order </span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Order Summary -->
      <div class="lg:col-span-1">
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow p-6 sticky top-4">
          <h2 class="text-xl font-semibold mb-4 text-gray-900 dark:text-white">
            Order Summary
          </h2>
          <div class="space-y-4">
            <div
              v-for="item in cartStore.items"
              :key="item.id"
              class="flex items-center gap-4"
            >
              <img
                :src="item.imageUrl"
                :alt="item.name"
                class="w-16 h-16 object-cover rounded"
              />
              <div class="flex-1">
                <h3 class="text-sm font-medium text-gray-900 dark:text-white">
                  {{ item.name }}
                </h3>
                <p class="text-sm text-gray-500 dark:text-gray-400">
                  Qty: {{ item.quantity }}
                </p>
                <p class="text-sm font-medium text-gray-900 dark:text-white">
                  ${{ formatPrice(item.price * item.quantity) }}
                </p>
              </div>
            </div>

            <div class="border-t pt-4 space-y-2">
              <div class="flex justify-between text-sm">
                <span class="text-gray-500 dark:text-gray-400">Subtotal</span>
                <span class="font-medium text-gray-900 dark:text-white">
                  ${{ formatPrice(cartStore.totalAmount) }}
                </span>
              </div>
              <div class="flex justify-between text-sm">
                <span class="text-gray-500 dark:text-gray-400">Shipping</span>
                <span class="font-medium text-gray-900 dark:text-white">
                  ${{ formatPrice(shippingCost) }}
                </span>
              </div>
              <div class="flex justify-between text-lg font-semibold pt-2 border-t">
                <span class="text-gray-900 dark:text-white">Total</span>
                <span class="text-gray-900 dark:text-white">
                  ${{ formatPrice(cartStore.totalAmount + shippingCost) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useCartStore } from "@/stores/cartStore";
import { useOrderStore } from "@/stores/orderStore";
import { useToast } from "vue-toastification";
import { formatPrice } from "@/utils/formatters";
import Input from "@/components/common/Input.vue";
import Label from "@/components/common/Label.vue";
import Select from "@/components/common/Select.vue";
import ButtonSpinner from "@/components/common/ButtonSpinner.vue";
import {
  User,
  CreditCard,
  Home,
  Building,
  MapPin,
  MapPinned,
  Calendar,
  Lock,
  ChevronLeft,
  Phone,
} from "lucide-vue-next";

const router = useRouter();
const cartStore = useCartStore();
const orderStore = useOrderStore();
const toast = useToast();

const loading = ref(false);

const steps = [
  { id: 1, name: "Shipping" },
  { id: 2, name: "Payment" },
  { id: 3, name: "Review" },
];

const currentStep = ref(0);
const shippingCost = ref(5.99);

const shippingInfo = ref({
  firstName: "",
  lastName: "",
  address: "",
  phone: "",
  city: "",
  state: "",
  zipCode: "",
});

const paymentInfo = ref({
  cardNumber: "",
  expiryDate: "",
  cvv: "",
});

const nextStep = () => {
  if (currentStep.value < steps.length - 1) {
    currentStep.value++;
  }
};

const placeOrder = async () => {
  loading.value = true;
  try {
    const orderData = {
      userId: cartStore.userId,
      address: `${shippingInfo.value.address}, ${shippingInfo.value.city}, ${shippingInfo.value.state} ${shippingInfo.value.zipCode}`,
      phone: shippingInfo.value.phone || "",
    };

    await orderStore.createOrder(orderData);
    toast.success("Order placed successfully!");
    router.push("/order-confirmation");
    await cartStore.clearCart();
  } catch (error) {
    toast.error("Failed to place order. Please try again.");
  } finally {
    loading.value = false;
  }
};

const states = [
  "AL",
  "AK",
  "AZ",
  "AR",
  "CA",
  "CO",
  "CT",
  "DE",
  "FL",
  "GA",
  "HI",
  "ID",
  "IL",
  "IN",
  "IA",
  "KS",
  "KY",
  "LA",
  "ME",
  "MD",
  "MA",
  "MI",
  "MN",
  "MS",
  "MO",
  "MT",
  "NE",
  "NV",
  "NH",
  "NJ",
  "NM",
  "NY",
  "NC",
  "ND",
  "OH",
  "OK",
  "OR",
  "PA",
  "RI",
  "SC",
  "SD",
  "TN",
  "TX",
  "UT",
  "VT",
  "VA",
  "WA",
  "WV",
  "WI",
  "WY",
];
</script>
