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
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllProducts(String category, String search, String sortBy, Boolean inStock) {
        List<Product> products = productRepository.findAll();

        // Filter by category
        if (category != null && !category.trim().isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getCategory().equalsIgnoreCase(category.trim()))
                    .collect(Collectors.toList());
        }

        // Filter by search term (name or description)
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.trim().toLowerCase();
            products = products.stream()
                    .filter(product -> 
                        product.getName().toLowerCase().contains(searchLower) ||
                        (product.getDescription() != null && product.getDescription().toLowerCase().contains(searchLower)))
                    .collect(Collectors.toList());
        }

        // Filter by stock availability
        if (inStock != null && inStock) {
            products = products.stream()
                    .filter(product -> product.getQuantity() > 0)
                    .collect(Collectors.toList());
        }

        // Sort products
        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "price-asc":
                    products.sort(Comparator.comparing(Product::getPrice));
                    break;
                case "price-desc":
                    products.sort(Comparator.comparing(Product::getPrice).reversed());
                    break;
                case "name":
                default:
                    products.sort(Comparator.comparing(Product::getName));
                    break;
            }
        }

        return products;
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