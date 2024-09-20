package nanox.products.products_test.manager;

import nanox.products.products_test.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductManager {
    void fetchProductsFromApi() throws Exception;

    Page<Product> getAllProducts(Pageable pageable);

    Page<Product> searchProducts(String search, Pageable pageable);

    Optional<Product> getProductById(Long id);

    Product addProduct(Product product);

    Product updateProduct(Long id, Product updatedProduct);

    void deleteProduct(Long id);
}
