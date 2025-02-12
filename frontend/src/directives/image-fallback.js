export const imageFallback = {
  mounted(element, binding) {
    const placeholderSrc = binding.value || "/images/image-placeholder.jpg";

    element.addEventListener("error", () => {
      element.src = placeholderSrc;
    });
  },
};
