<template>
  <div>
    <div v-if="loading">
      <div v-for="(n, index) in skeletonCount" :key="index" class="mb-2">
        <Skeleton :width="getSkeletonWidth(index)" :height="getSkeletonHeight(index)" />
      </div>
    </div>
    <div v-else>
      <slot />
    </div>
  </div>
</template>

<script setup>
import { defineProps } from "vue";
import { Skeleton } from "@brayamvalero/vue3-skeleton";

// Width and Height can be received as a string, array or function
const props = defineProps({
  loading: Boolean,
  skeletonCount: { type: Number, default: 3 },
  skeletonWidth: { type: [String, Array, Function], default: "100%" },
  skeletonHeight: { type: [String, Array, Function], default: "20px" },
});

// Function to handle dynamic width
const getSkeletonWidth = (index) => {
  if (typeof props.skeletonWidth === "function") return props.skeletonWidth(index);
  if (Array.isArray(props.skeletonWidth)) return props.skeletonWidth[index] || "100%";
  return props.skeletonWidth;
};

// Function to handle dynamic height
const getSkeletonHeight = (index) => {
  if (typeof props.skeletonHeight === "function") return props.skeletonHeight(index);
  if (Array.isArray(props.skeletonHeight)) return props.skeletonHeight[index] || "20px";
  return props.skeletonHeight;
};
</script>
