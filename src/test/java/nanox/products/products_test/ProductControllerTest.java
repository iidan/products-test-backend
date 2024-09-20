package nanox.products.products_test;

import nanox.products.products_test.controller.ProductController;
import nanox.products.products_test.entity.Product;
import nanox.products.products_test.manager.ProductManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductManager productManager;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product().setId(1L).setTitle("Product 1");
        Product product2 = new Product().setId(2L).setTitle("Product 2");
        // Mocking the List of products
        List<Product> mockProducts = Arrays.asList(product1, product2);

        Page<Product> mockPage = new PageImpl<>(mockProducts, PageRequest.of(0, 10), mockProducts.size());

        when(productManager.getAllProducts(PageRequest.of(0, 10))).thenReturn(mockPage);
        Page<Product> result = productController.getAllProducts(0, 10,"");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.toList().size());
        verify(productManager, times(1)).getAllProducts(any(Pageable.class));
    }

    @Test
    void testGetProductById_Found() {
        Product product = new Product().setId(1L).setTitle("Product 1");
        when(productManager.getProductById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<Product> response = productController.getProductById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(product, response.getBody());
        verify(productManager, times(1)).getProductById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productManager.getProductById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        verify(productManager, times(1)).getProductById(1L);
    }

    @Test
    void testAddProduct() {
        Product product = new Product().setTitle("Product 1");
        when(productManager.addProduct(product)).thenReturn(product);

        Product result = productController.addProduct(product);

        assertNotNull(result);
        assertEquals("Product 1", result.getTitle());
        verify(productManager, times(1)).addProduct(product);
    }

    @Test
    void testUpdateProduct_Found() {
        Product updatedProduct = new Product().setId(1L).setTitle("Updated Product");
        when(productManager.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        ResponseEntity<Product> response = productController.updateProduct(1L, updatedProduct);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Updated Product", Objects.requireNonNull(response.getBody()).getTitle());
        verify(productManager, times(1)).updateProduct(1L, updatedProduct);
    }

    @Test
    void testUpdateProduct_NotFound() {
        // Arrange
        Product updatedProduct = new Product().setId(1L).setTitle("Updated Product");
        when(productManager.updateProduct(eq(1L), any(Product.class))).thenReturn(null);

        ResponseEntity<Product> response = productController.updateProduct(1L, updatedProduct);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        verify(productManager, times(1)).updateProduct(1L, updatedProduct);
    }

    @Test
    void testDeleteProduct() {
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        verify(productManager, times(1)).deleteProduct(1L);
    }
}
