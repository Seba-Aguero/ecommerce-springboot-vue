export const formatPrice = (price) => {
  return price ? Number(price).toFixed(2) : "0.00";
};