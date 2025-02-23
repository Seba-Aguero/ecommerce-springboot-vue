package ecommerce_springboot_vue.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
@Tag(name = "Config", description = "Configuration related endpoints")
public class ConfigController {

    @Value("${app.order.shipping-cost}")
    private BigDecimal shippingCost;

    @GetMapping("/shipping-cost")
    public ResponseEntity<Map<String, BigDecimal>> getShippingCost() {
        return ResponseEntity.ok(Map.of("shippingCost", shippingCost));
    }
}