import { describe, it, expect, vi, beforeEach } from "vitest";
import { useUserStore } from "../userStore";
import { userService } from "@/services/userService";

describe("User Store", () => {
  let store;

  const mockProfile = {
    id: 1,
    name: "Test User",
    email: "test@example.com",
  };

  beforeEach(() => {
    store = useUserStore();
    vi.clearAllMocks();
  });

  describe("state", () => {
    it("initializes with default state", () => {
      expect(store.profile).toBeNull();
      expect(store.loading).toBe(false);
    });
  });

  describe("actions", () => {
    it("fetchProfile success", async () => {
      vi.spyOn(userService, "fetchProfile").mockResolvedValue(mockProfile);

      const result = await store.fetchProfile();

      expect(result).toEqual(mockProfile);
      expect(store.profile).toEqual(mockProfile);
      expect(store.loading).toBe(false);
      expect(userService.fetchProfile).toHaveBeenCalled();
    });

    it("fetchProfile handles error", async () => {
      const errorMessage = "Failed to fetch profile";
      vi.spyOn(userService, "fetchProfile").mockRejectedValue({
        response: { data: { message: errorMessage } }
      });

      await expect(store.fetchProfile()).rejects.toThrow(errorMessage);
      expect(store.loading).toBe(false);
      expect(store.profile).toBeNull();
    });

    it("updateProfile success", async () => {
      const updatedProfile = { ...mockProfile, name: "Updated Name" };
      vi.spyOn(userService, "updateProfile").mockResolvedValue(updatedProfile);

      const result = await store.updateProfile(updatedProfile);

      expect(result).toEqual(updatedProfile);
      expect(store.profile).toEqual(updatedProfile);
      expect(store.loading).toBe(false);
      expect(userService.updateProfile).toHaveBeenCalledWith(updatedProfile);
    });

    it("updateProfile handles error", async () => {
      const errorMessage = "Failed to update profile";
      vi.spyOn(userService, "updateProfile").mockRejectedValue({
        response: { data: { message: errorMessage } }
      });

      await expect(store.updateProfile({})).rejects.toThrow(errorMessage);
      expect(store.loading).toBe(false);
    });

    it("handles loading state correctly", async () => {
      vi.spyOn(userService, "fetchProfile").mockResolvedValue(mockProfile);

      const promise = store.fetchProfile();
      expect(store.loading).toBe(true);

      await promise;
      expect(store.loading).toBe(false);
    });
  });
});