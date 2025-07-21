package com.agh.shop.service;

import com.agh.shop.model.ProductRequest;
import com.agh.shop.jpa.Product;
import com.agh.shop.exception.ResourceNotFoundException;
import com.agh.shop.exception.DuplicateResourceException;
import com.agh.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produkt o ID " + id + " nie został znaleziony"));
    }

    public Product createProduct(ProductRequest request) {
        if (request.getSku() != null && productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Produkt o SKU " + request.getSku() + " już istnieje");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setSku(request.getSku());

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);

        // Sprawdź czy nowy SKU nie jest zajęty przez inny produkt
        if (request.getSku() != null && !request.getSku().equals(product.getSku())) {
            if (productRepository.existsBySku(request.getSku())) {
                throw new DuplicateResourceException("Produkt o SKU " + request.getSku() + " już istnieje");
            }
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setSku(request.getSku());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produkt o ID " + id + " nie został znaleziony");
        }
        productRepository.deleteById(id);
    }
}