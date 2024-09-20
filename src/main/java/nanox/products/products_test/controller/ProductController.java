package nanox.products.products_test.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nanox.products.products_test.entity.Product;
import nanox.products.products_test.manager.ProductManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductManager productManager;

    @GetMapping("/all-products")
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search) {

        try {
            Pageable pageable = PageRequest.of(page, size);

            if (search.isEmpty()) {
                return productManager.getAllProducts(pageable);
            } else {
                return productManager.searchProducts(search, pageable);
            }
        } catch (Exception e) {
            log.error("Unable to fetch products from API page :%d, size :%d, search :%s".formatted(page, size, search), e);
        }

        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productManager.getProductById(id);
            return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Unable to fetch product by id :%d".formatted(id), e);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search-products")
    public ResponseEntity<Product> searchProducts(@PathVariable Long id) {
        try {
            Optional<Product> product = productManager.getProductById(id);
            return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Unable to fetch products by id :%d".formatted(id), e);
        }

        return ResponseEntity.notFound().build();
    }

    // Create a new product
    @PostMapping("/add-product")
    public Product addProduct(@RequestBody Product product) {
        try {
            return productManager.addProduct(product);
        } catch (Exception e) {
            log.error("Unable to fetch products from API for name %s".formatted(product.getTitle()), e);
        }
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        try {

            Product product = productManager.updateProduct(id, updatedProduct);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            log.error("Unable to fetch products from API for id %d".formatted(id), e);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productManager.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Unable to fetch products from API for id %d".formatted(id), e);
        }

        return ResponseEntity.notFound().build();
    }
}