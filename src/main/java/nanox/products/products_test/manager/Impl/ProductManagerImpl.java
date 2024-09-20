package nanox.products.products_test.manager.Impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import nanox.products.products_test.api.ProductApiClient;
import nanox.products.products_test.entity.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nanox.products.products_test.manager.ProductManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import nanox.products.products_test.repository.ProductRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProductManagerImpl implements ProductManager {

    protected final ProductRepository productRepository;
    private ProductApiClient productApiClient;


    @PostConstruct
    public void fetchProductsFromApi() throws Exception {

        Gson gson = new Gson();
        String jsonResponse = productApiClient.getResponse("https://dummyjson.com/products");

        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray productsArray = jsonObject.getAsJsonArray("products");
        Type productListType = new TypeToken<List<Product>>() {
        }.getType();
        List<Product> productList = gson.fromJson(productsArray, productListType);

        productRepository.saveAll(productList);
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchProducts(String search, Pageable pageable) {
        return productRepository.findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCase(search, search, pageable);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {

        Product existingProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update fields with values from updatedProduct
        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setRating(updatedProduct.getRating());
        existingProduct.setPrice(updatedProduct.getPrice());

        // Handle nested lists if necessary
        if (updatedProduct.getImages() != null) {
            existingProduct.setImages(updatedProduct.getImages());
        }
        if (updatedProduct.getTags() != null) {
            existingProduct.setTags(updatedProduct.getTags());
        }
        if (updatedProduct.getMeta() != null) {
            existingProduct.setMeta(updatedProduct.getMeta());
        }

        // Save the updated product
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository
                .findById(id)
                .ifPresent(productRepository::delete);
    }
}
