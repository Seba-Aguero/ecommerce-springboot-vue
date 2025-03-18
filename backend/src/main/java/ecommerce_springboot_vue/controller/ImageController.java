package ecommerce_springboot_vue.controller;

import ecommerce_springboot_vue.config.FileStorageConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ImageController {

  private final FileStorageConfig fileStorageConfig;

  @GetMapping("/images/{filename:.+}")
  public ResponseEntity<Resource> getImage(@PathVariable String filename) {
    try {
      Path filePath = Paths.get(fileStorageConfig.getDirectory()).resolve(filename);
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists()) {
        String contentType = determineContentType(filename);
        return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .body(resource);
      }
      return ResponseEntity.notFound().build();
    } catch (IOException e) {
      return ResponseEntity.internalServerError().build();
    }
}

  private String determineContentType(String filename) {
    String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    switch (extension) {
      case "png":
        return "image/png";
      case "jpg":
      case "jpeg":
        return "image/jpeg";
      case "webp":
        return "image/webp";
      default:
        return "application/octet-stream";
    }
}
}