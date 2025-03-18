package ecommerce_springboot_vue.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "file.upload")
@Getter
@Setter
public class FileStorageConfig {
  private String directory;
  private String baseUrl;
}
