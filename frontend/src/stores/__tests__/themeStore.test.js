import { describe, it, expect, beforeEach, vi } from "vitest";
import { useThemeStore } from "../themeStore";

describe("Theme Store", () => {
  let store;

  beforeEach(() => {
    localStorage.clear();
    document.documentElement.classList.remove("dark");
    store = useThemeStore();
  });

  describe("state", () => {
    it("initializes with dark theme by default", () => {
      expect(store.isDark).toBe(true);
    });
  });

  describe("actions", () => {
    it("toggleTheme switches between dark and light themes", () => {
      store.toggleTheme();
      expect(store.isDark).toBe(false);
      expect(document.documentElement.classList.contains("dark")).toBe(false);
      expect(localStorage.theme).toBe("light");

      store.toggleTheme();
      expect(store.isDark).toBe(true);
      expect(document.documentElement.classList.contains("dark")).toBe(true);
      expect(localStorage.theme).toBe("dark");
    });

    it("initTheme sets theme based on localStorage", () => {
      localStorage.theme = "light";
      store.initTheme();
      expect(store.isDark).toBe(false);
      expect(document.documentElement.classList.contains("dark")).toBe(false);

      localStorage.theme = "dark";
      store.initTheme();
      expect(store.isDark).toBe(true);
      expect(document.documentElement.classList.contains("dark")).toBe(true);
    });

    it("initTheme respects system preference when no localStorage theme", () => {
      const mockMatchMedia = (matches) => {
        Object.defineProperty(window, "matchMedia", {
          writable: true,
          value: vi.fn().mockImplementation((query) => ({
            matches,
            media: query,
          })),
        });
      };

      mockMatchMedia(true);
      store.initTheme();
      expect(store.isDark).toBe(false);
      expect(document.documentElement.classList.contains("dark")).toBe(false);

      mockMatchMedia(false);
      store.initTheme();
      expect(store.isDark).toBe(true);
      expect(document.documentElement.classList.contains("dark")).toBe(true);
    });
  });
});