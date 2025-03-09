import { describe, it, expect, vi, beforeEach } from "vitest";
import { useAuthStore } from "@/stores/authStore";
import { authService } from "@/services/authService";

describe("Auth Store", () => {
  let store;

  // Mock objects
  const mockUser = {
    id: 1,
    email: "test@test.com",
    role: "USER",
  };

  const mockLoginData = {
    email: "test@test.com",
    password: "password123",
  };

  beforeEach(() => {
    sessionStorage.clear();
    vi.clearAllMocks();
    store = useAuthStore();
  });

  describe("state", () => {
    it("initializes with default state", () => {
      expect(store.isAuthenticated).toBe(false);
      expect(store.user).toBeNull();
      expect(store.token).toBeNull();
    });
  });

  describe("actions", () => {
    it("login sets auth state correctly", async () => {
      const mockResponse = {
        token: "test-token",
        user: mockUser,
      };

      vi.spyOn(authService, "login").mockResolvedValue(mockResponse);

      const result = await store.login(mockLoginData);

      expect(result).toBe(true);
      expect(store.isAuthenticated).toBe(true);
      expect(store.token).toBe(mockResponse.token);
      expect(store.user).toEqual(mockResponse.user);
      expect(sessionStorage.getItem("token")).toBe(mockResponse.token);
    });

    it("logout clears auth state", () => {
      // Set initial authenticated state
      store.$patch({
        isAuthenticated: true,
        token: "test-token",
        user: mockUser
      });
      sessionStorage.setItem("token", "test-token");

      store.logout();

      expect(store.isAuthenticated).toBe(false);
      expect(store.token).toBeNull();
      expect(store.user).toBeNull();
      expect(sessionStorage.getItem("token")).toBeNull();
    });

    it("handles login error correctly", async () => {
      vi.spyOn(authService, "login").mockRejectedValue(new Error("Login failed"));

      await expect(store.login(mockLoginData)).rejects.toThrow("Login failed");
      expect(store.isAuthenticated).toBe(false);
      expect(store.token).toBeNull();
      expect(store.user).toBeNull();
    });

    it("fetches user profile successfully", async () => {
      vi.spyOn(authService, "fetchUserProfile").mockResolvedValue(mockUser);

      await store.fetchUserProfile();
      expect(store.user).toEqual(mockUser);
    });
  });

  describe("getters", () => {
    it("isAuthenticated returns true when token exists", () => {
      store.$patch({
        token: "some-token",
      });
      expect(store.isAuthenticated).toBe(true);
    });

    it("isAuthenticated returns false when token is null", () => {
      store.$patch({
        token: null,
      });
      expect(store.isAuthenticated).toBe(false);
    });

    it("userEmail returns user email when user exists", () => {
      store.$patch({
        user: {
          email: "test@test.com",
          role: "USER",
        },
      });
      expect(store.userEmail).toBe("test@test.com");
    });

    it("userEmail returns temp email when only tempUserData exists", () => {
      store.$patch({
        tempUserData: { email: "temp@test.com" },
      });
      expect(store.userEmail).toBe("temp@test.com");
    });

    it("userEmail returns empty string when no user or tempUserData exists", () => {
      expect(store.userEmail).toBe("");
    });

    it("userRole returns correct role when user exists", () => {
      store.$patch({
        user: { id: 1, email: "test@test.com", role: "ADMIN" },
      });
      expect(store.userRole).toBe("ADMIN");
    });

    it("userRole returns empty string when no user exists", () => {
      expect(store.userRole).toBe("");
    });
  });
});
