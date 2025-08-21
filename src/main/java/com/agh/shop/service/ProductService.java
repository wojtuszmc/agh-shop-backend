package com.agh.shop.service;

import com.agh.shop.jpa.Category;
import com.agh.shop.model.ProductRequest;
import com.agh.shop.jpa.Product;
import com.agh.shop.exception.ResourceNotFoundException;
import com.agh.shop.exception.DuplicateResourceException;
import com.agh.shop.repository.CategoryRepository;
import com.agh.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

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

    /*public Product createProduct(ProductRequest request) {
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
    }*/

    public Product createProduct(ProductRequest request) {
        Category category = categoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kategoria '" + request.getCategory() + "' nie istnieje"
                ));

        String normalizedName = request.getName().trim().toLowerCase();
        boolean nameExists = productRepository.findAll().stream()
                .anyMatch(product -> product.getName().toLowerCase().equals(normalizedName));

        if (nameExists) {
            throw new DuplicateResourceException(
                    "Produkt o nazwie '" + request.getName() + "' już istnieje"
            );
        }

        String sku = generateProductSku(category.getName(), request.getName());

        Product product = new Product();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        product.setCategory(category.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setSku(sku);

        return productRepository.save(product);
    }

    // Metoda pomocnicza do generowania SKU
    private String generateProductSku(String category, String productName) {
        // Format: KAT-NAZWA-XXXX
        // np. ELEKTRONIKA-IPHONE-1234

        String categoryCode = category.toUpperCase()
                .replace(" ", "")
                .substring(0, Math.min(category.length(), 3));

        String productCode = productName.toUpperCase()
                .replaceAll("[^A-Z0-9]", "")
                .substring(0, Math.min(productName.replaceAll("[^A-Z0-9]", "").length(), 10));

        String randomCode = String.format("%04d", new Random().nextInt(10000));

        String sku = categoryCode + "-" + productCode + "-" + randomCode;

        // Sprawdź unikalność
        while (productRepository.existsBySku(sku)) {
            randomCode = String.format("%04d", new Random().nextInt(10000));
            sku = categoryCode + "-" + productCode + "-" + randomCode;
        }

        return sku;
    }

    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produkt o ID " + id + " nie został znaleziony");
        }
        productRepository.deleteById(id);
    }
}