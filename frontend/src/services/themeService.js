import { useThemeStore } from "@/stores/themeStore";

export const themeService = {
  toggleTheme() {
    const themeStore = useThemeStore();
    try {
      themeStore.toggleTheme();
      return themeStore.isDark;
    } catch (error) {
      throw new Error("Error changing theme: " + error.message);
    }
  },

  setTheme(isDark) {
    const themeStore = useThemeStore();
    try {
      themeStore.setTheme(isDark);
      return isDark;
    } catch (error) {
      throw new Error("Error setting theme: " + error.message);
    }
  },

  getTheme() {
    const themeStore = useThemeStore();
    return themeStore.isDark;
  },
};
