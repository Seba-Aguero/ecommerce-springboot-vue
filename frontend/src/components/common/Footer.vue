<template>
  <footer class="bg-white border-t mt-auto">
    <div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <!-- Enlaces útiles -->
        <div>
          <h3
            class="text-sm font-semibold text-gray-600 tracking-wider uppercase"
          >
            Enlaces útiles
          </h3>
          <div class="mt-4 space-y-2">
            <a href="#" class="text-gray-500 hover:text-gray-900 block">
              Sobre nosotros
            </a>
            <a href="#" class="text-gray-500 hover:text-gray-900 block">
              Términos y condiciones
            </a>
            <a href="#" class="text-gray-500 hover:text-gray-900 block">
              Política de privacidad
            </a>
          </div>
        </div>

        <!-- Contacto -->
        <div>
          <h3
            class="text-sm font-semibold text-gray-600 tracking-wider uppercase"
          >
            Contacto
          </h3>
          <div class="mt-4 space-y-2">
            <p class="text-gray-500">
              <i class="fas fa-envelope mr-2"></i>
              info@miecommerce.com
            </p>
            <p class="text-gray-500">
              <i class="fas fa-phone mr-2"></i>
              +1234567890
            </p>
          </div>
        </div>

        <!-- Redes sociales -->
        <div>
          <h3
            class="text-sm font-semibold text-gray-600 tracking-wider uppercase"
          >
            Síguenos
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
        <p class="text-center text-gray-500">
          © {{ new Date().getFullYear() }} Mi Ecommerce. Todos los derechos
          reservados.
        </p>
      </div>
    </div>
  </footer>
</template>

<script setup></script>
