import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "@/App.vue";
import router from "@/router";
import Toast from "vue-toastification";
import "@/assets/main.css";
import "vue-toastification/dist/index.css";
import "@brayamvalero/vue3-skeleton/dist/style.css";

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(Toast, {
  position: "top-center",
  timeout: 2500,
  closeOnClick: true,
  pauseOnFocusLoss: true,
  pauseOnHover: true,
  draggable: true,
  draggablePercent: 0.6,
  showCloseButtonOnHover: false,
  hideProgressBar: false,
  closeButton: "button",
  icon: true,
  rtl: false,
});

app.mount("#app");
