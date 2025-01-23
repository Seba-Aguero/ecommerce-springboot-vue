import "@/assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "@/App.vue";
import router from "@/router";
import "@/assets/main.css";

const app = createApp(App);

// Vue DevTools
app.config.devtools = false;

app.use(createPinia());
app.use(router);

app.mount("#app");
