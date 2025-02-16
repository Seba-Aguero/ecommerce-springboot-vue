<template>
  <div
    class="min-h-screen flex justify-center bg-gradient-to-br from-primary-600 to-purple-600
    dark:from-gray-900 dark:to-gray-900 py-6 px-4 sm:px-6 lg:px-8"
  >
    <div class="w-full max-w-md my-14">
      <div
        class="bg-white dark:bg-gray-800 p-8 rounded-lg shadow-2xl transform transition-all duration-500 ease-in-out hover:scale-105"
      >
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script setup></script>
