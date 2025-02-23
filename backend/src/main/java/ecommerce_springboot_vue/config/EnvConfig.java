package ecommerce_springboot_vue.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

  public static void load() {
    Dotenv dotenv = Dotenv.load();

    System.setProperty("APP_NAME", dotenv.get("APP_NAME"));

    System.setProperty("DB_URL", dotenv.get("DB_URL"));
    System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
    System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));

    System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
    System.setProperty("JWT_EXPIRATION_TIME", dotenv.get("JWT_EXPIRATION_TIME"));

    System.setProperty("EMAIL_HOST", dotenv.get("EMAIL_HOST"));
    System.setProperty("EMAIL_PORT", dotenv.get("EMAIL_PORT"));
    System.setProperty("EMAIL_USERNAME", dotenv.get("EMAIL_USERNAME"));
    System.setProperty("EMAIL_PASSWORD", dotenv.get("EMAIL_PASSWORD"));

    System.setProperty("SHIPPING_COST", dotenv.get("SHIPPING_COST"));
  }
}