import { defineStore } from "pinia";
import { ref } from "vue";

export const useThemeStore = defineStore("theme", () => {
  const isDark = ref(true);

  function toggleTheme() {
    isDark.value = !isDark.value;
    if (isDark.value) {
      document.documentElement.classList.add("dark");
      localStorage.theme = "dark";
    } else {
      document.documentElement.classList.remove("dark");
      localStorage.theme = "light";
    }
  }

  function initTheme() {
    if (
      localStorage.theme === "light" ||
      (!("theme" in localStorage) &&
        window.matchMedia("(prefers-color-scheme: light)").matches)
    ) {
      isDark.value = false;
      document.documentElement.classList.remove("dark");
    } else {
      isDark.value = true;
      document.documentElement.classList.add("dark");
    }
  }

  return {
    isDark,
    toggleTheme,
    initTheme,
  };
});
