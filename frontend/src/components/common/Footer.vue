<template>
  <footer class="bg-white dark:bg-gray-800 border-t mt-auto">
    <div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
      <!-- <div class="grid grid-cols-1 md:grid-cols-3 gap-8"> -->
      <div class="flex justify-between flex-wrap gap-8">
        <!-- Useful Links -->
        <div>
          <h3
            class="text-sm font-bold text-gray-600 dark:text-gray-100 tracking-wider uppercase"
          >
            Useful Links
          </h3>
          <div class="mt-4 space-y-2">
            <a href="#" class="text-gray-500 dark:text-gray-300 hover:text-gray-900 block">
              About Us
            </a>
            <a href="#" class="text-gray-500 dark:text-gray-300 hover:text-gray-900 block">
              Terms & Conditions
            </a>
            <a href="#" class="text-gray-500 dark:text-gray-300 hover:text-gray-900 block">
              Privacy Policy
            </a>
          </div>
        </div>

        <!-- Contact -->
        <div>
          <h3
            class="text-sm font-semibold text-gray-600 dark:text-gray-100 tracking-wider uppercase"
          >
            Contact
          </h3>
          <div class="mt-4 space-y-2">
            <p class="text-gray-500 dark:text-gray-300">
              <Mail class="h-4 w-4 inline-block mr-2" />
              info@miecommerce.com
            </p>
            <p class="text-gray-500 dark:text-gray-300">
              <Phone class="h-4 w-4 inline-block mr-2" />
              +1234567890
            </p>
          </div>
        </div>

        <!-- Social Media -->
        <div>
          <h3
            class="text-sm font-semibold text-gray-600 dark:text-gray-100 tracking-wider uppercase"
          >
            Follow Us
          </h3>
          <div class="mt-4 flex space-x-6">
            <a href="#" class="text-gray-500 hover:text-gray-900">
              <i class="fab fa-facebook text-xl"></i>
            </a>
            <a href="#" class="text-gray-500 hover:text-gray-900">
              <i class="fab fa-twitter text-xl"></i>
            </a>
            <a href="#" class="text-gray-500 hover:text-gray-900">
              <i class="fab fa-instagram text-xl"></i>
            </a>
          </div>
        </div>
      </div>

      <div class="mt-8 border-t pt-8">
        <p class="text-center text-gray-500 dark:text-gray-400">
          © {{ new Date().getFullYear() }} My Ecommerce. All rights reserved.
        </p>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { Mail, Phone } from 'lucide-vue-next';
</script>
