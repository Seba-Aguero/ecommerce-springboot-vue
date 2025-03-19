<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 z-40 backdrop-blur-sm"
    @click="$emit('close')"
  ></div>
  <div class="fixed inset-0 z-50 flex items-center justify-center p-4">
    <div
      class="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-hidden"
      @click.stop
    >
      <div
        class="flex justify-between items-center border-b border-gray-200 dark:border-gray-700 px-6 py-4 bg-gradient-to-r from-primary-500 to-purple-500"
      >
        <h2 class="text-xl font-semibold text-white">
          {{ isEditing ? "Edit Product" : "Add New Product" }}
        </h2>
        <button
          @click="$emit('close')"
          class="text-white hover:text-gray-200 transition-colors"
        >
          <X class="h-6 w-6" />
        </button>
      </div>

      <div class="px-6 py-4 overflow-y-auto max-h-[calc(90vh-130px)]">
        <form @submit.prevent="handleSubmit">
          <!-- Name Field -->
          <div class="mb-4">
            <label
              for="name"
              class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1"
            >
              Product Name *
            </label>
            <input
              id="name"
              v-model="form.name"
              type="text"
              class="mt-1 block w-full px-3 py-2 rounded-md border border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              required
            />
          </div>

          <!-- Description Field -->
          <div class="mb-4">
            <label
              for="description"
              class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1"
            >
              Description
            </label>
            <textarea
              id="description"
              v-model="form.description"
              rows="3"
              class="mt-1 block w-full px-3 py-2 rounded-md border border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            ></textarea>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <!-- Price Field -->
            <div>
              <label
                for="price"
                class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1"
              >
                Price ($) *
              </label>
              <div class="relative rounded-md shadow-sm">
                <div
                  class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"
                >
                  <span class="text-gray-500 dark:text-gray-400">$</span>
                </div>
                <input
                  id="price"
                  v-model.number="form.price"
                  type="number"
                  min="0.01"
                  step="0.01"
                  class="block w-full pl-7 pr-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:border-primary-500 focus:ring-primary-500 dark:bg-gray-700 dark:text-white"
                  required
                />
              </div>
            </div>

            <!-- Stock Field -->
            <div>
              <label
                for="totalStock"
                class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1"
              >
                Total Stock *
              </label>
              <div class="relative rounded-md shadow-sm">
                <input
                  id="totalStock"
                  v-model.number="form.totalStock"
                  type="number"
                  min="0"
                  class="block w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:border-primary-500 focus:ring-primary-500 dark:bg-gray-700 dark:text-white"
                  required
                />
              </div>
            </div>
          </div>

          <!-- Categories Field -->
          <div class="mb-4">
            <label
              class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1"
            >
              Categories
            </label>
            <div class="mt-2 grid grid-cols-2 sm:grid-cols-3 gap-2">
              <div
                v-for="category in props.categories"
                :key="category.id"
                class="flex items-center p-2 rounded-md hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
              >
                <input
                  :id="`category-${category.id}`"
                  v-model="selectedCategoryIds"
                  :value="category.id"
                  type="checkbox"
                  class="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded dark:bg-gray-700 dark:border-gray-600"
                />
                <label
                  :for="`category-${category.id}`"
                  class="ml-2 block text-sm text-gray-700 dark:text-gray-300"
                >
                  {{ category.name }}
                </label>
              </div>
            </div>
          </div>

          <!-- Image Upload Field -->
          <div class="mb-4">
            <label
              class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1"
            >
              Product Image
            </label>
            <div class="mt-2 flex items-center">
              <div
                v-if="imagePreview || (isEditing && props.product.imageUrl)"
                class="mr-4 h-24 w-24 overflow-hidden rounded-md bg-gray-100 dark:bg-gray-700 border border-gray-200 dark:border-gray-600"
              >
                <img
                  :src="
                    imagePreview ||
                    (isEditing && props.product.imageUrl ? props.product.imageUrl : '')
                  "
                  alt="Product preview"
                  class="h-full w-full object-cover"
                  v-image-fallback
                />
              </div>
              <label
                for="image-upload"
                class="cursor-pointer rounded-md bg-white dark:bg-gray-700 px-3 py-2 text-sm font-medium leading-4 text-gray-700 dark:text-gray-300 shadow-sm border border-gray-300 dark:border-gray-600 hover:bg-gray-50 dark:hover:bg-gray-600 transition-colors"
              >
                <span>Upload image</span>
                <input
                  id="image-upload"
                  type="file"
                  accept="image/*"
                  class="sr-only"
                  @change="handleImageChange"
                />
              </label>
            </div>
            <p class="mt-1 text-xs text-gray-500 dark:text-gray-400">
              PNG, JPG, WEBP up to 2MB
            </p>
          </div>

          <div class="mt-6 flex justify-end space-x-3">
            <button
              type="button"
              @click="$emit('close')"
              class="px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm text-sm font-medium text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-gradient-to-r from-primary-500 to-purple-500 hover:from-primary-600 hover:to-purple-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
            >
              {{ isEditing ? "Update" : "Create" }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { X } from "lucide-vue-next";
import { useToast } from "vue-toastification";

const props = defineProps({
  product: {
    type: Object,
    default: null,
  },
  categories: {
    type: Array,
    default: () => [],
  },
});

const toast = useToast();
const emit = defineEmits(["close", "save"]);

const isEditing = computed(() => !!props.product);
const baseUrl = import.meta.env.VITE_API_URL || "";

const form = ref({
  name: "",
  description: "",
  price: 0,
  totalStock: 0,
});

const selectedCategoryIds = ref([]);
const imageFile = ref(null);
const imagePreview = ref(null);

onMounted(() => {
  if (isEditing.value) {
    form.value = {
      name: props.product.name,
      description: props.product.description || "",
      price: props.product.price,
      totalStock: props.product.totalStock,
    };

    selectedCategoryIds.value = props.product.categories.map((cat) => cat.id);
  }
});

const handleImageChange = (event) => {
  const file = event.target.files[0];

  const maxSize = 2 * 1024 * 1024; // 2MB
  const allowedTypes = ["image/jpeg", "image/png", "image/webp"];

  if (!file) {
    return;
  }

  if (!allowedTypes.includes(file.type)) {
    toast.error("Solo se permiten imágenes JPG, PNG o WEBP");
    event.target.value = ""; // Clear input
    return;
  }

  if (file.size > maxSize) {
    toast.error("La imagen no debe superar 2MB");
    event.target.value = ""; // Clear input
    return;
  }

  imageFile.value = file;

  const reader = new FileReader();
  reader.onload = (e) => {
    imagePreview.value = e.target.result;
  };
  reader.readAsDataURL(file);
};

const handleSubmit = async () => {
  try {
    const productData = {
      name: form.value.name,
      description: form.value.description,
      price: parseFloat(form.value.price),
      totalStock: parseInt(form.value.totalStock),
      categories: selectedCategoryIds.value.map((id) => ({ id: parseInt(id) })),
    };

    emit("save", {
      id: isEditing.value ? props.product.id : null,
      productData: productData,
      image: imageFile.value,
    });

    emit("close");
  } catch (error) {
    console.error("❌ Error preparing product data:", error);
    toast.error("Error preparing product data");
  }
};
</script>
