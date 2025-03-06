<template>
  <span
    class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
    :class="statusClasses"
  >
    <component :is="statusIcon" class="w-4 h-4 mr-1" />
    {{ status }}
  </span>
</template>

<script setup>
import { computed } from 'vue';
import {
  Clock,
  PackageCheck,
  Truck,
  CheckCircle,
  XCircle,
} from 'lucide-vue-next';

const props = defineProps({
  status: {
    type: String,
    required: true,
  },
});

const statusClasses = computed(() => {
  const classes = {
    PENDING: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
    PROCESSING: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
    SHIPPED: 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200',
    DELIVERED: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    CANCELLED: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
  };
  return classes[props.status] || 'bg-gray-100 text-gray-800';
});

const statusIcon = computed(() => {
  const icons = {
    PENDING: Clock,
    PROCESSING: PackageCheck,
    SHIPPED: Truck,
    DELIVERED: CheckCircle,
    CANCELLED: XCircle,
  };
  return icons[props.status] || Clock;
});
</script>