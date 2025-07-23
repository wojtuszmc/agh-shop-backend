package com.agh.shop.config;

import com.agh.shop.jpa.*;
import com.agh.shop.model.OrderStatus;
import com.agh.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository,
                                   CategoryRepository categoryRepository,
                                   OrderRepository orderRepository) {
        return args -> {
            // Inicjalizuj kategorie
            log.info("Inicjalizacja kategorii...");
            Category elektronika = new Category();
            elektronika.setName("Elektronika");
            elektronika.setDescription("Urządzenia elektroniczne i akcesoria");

            Category komputery = new Category();
            komputery.setName("Komputery");
            komputery.setDescription("Komputery i akcesoria komputerowe");

            Category telefony = new Category();
            telefony.setName("Telefony");
            telefony.setDescription("Telefony komórkowe i akcesoria");

            categoryRepository.saveAll(Arrays.asList(elektronika, komputery, telefony));

            // Inicjalizuj produkty
            log.info("Inicjalizacja produktów...");
            Product laptop = new Product();
            laptop.setName("Laptop Dell XPS 15");
            laptop.setDescription("Wydajny laptop do pracy i rozrywki");
            laptop.setCategory("Komputery");
            laptop.setPrice(5999.99);
            laptop.setQuantity(10);
            laptop.setSku("DELL-XPS-15");

            Product telefon = new Product();
            telefon.setName("iPhone 15 Pro");
            telefon.setDescription("Najnowszy smartfon od Apple");
            telefon.setCategory("Telefony");
            telefon.setPrice(4999.99);
            telefon.setQuantity(25);
            telefon.setSku("IPHONE-15-PRO");

            Product monitor = new Product();
            monitor.setName("Monitor Samsung 27\"");
            monitor.setDescription("Monitor 4K do pracy biurowej");
            monitor.setCategory("Komputery");
            monitor.setPrice(1499.99);
            monitor.setQuantity(15);
            monitor.setSku("SAMSUNG-27-4K");

            productRepository.saveAll(Arrays.asList(laptop, telefon, monitor));

            // Inicjalizuj przykładowe zamówienie
            log.info("Inicjalizacja zamówień...");
            Order order = new Order();
            order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            order.setCustomerName("Jan Kowalski");
            order.setCustomerEmail("jan.kowalski@example.com");
            order.setCustomerPhone("+48 123 456 789");

            Address shippingAddress = new Address();
            shippingAddress.setStreet("ul. Przykładowa 123");
            shippingAddress.setCity("Kraków");
            shippingAddress.setPostalCode("30-001");
            shippingAddress.setCountry("Polska");
            shippingAddress.setState("małopolskie");
            order.setShippingAddress(shippingAddress);
            order.setBillingAddress(shippingAddress);

            order.setStatus(OrderStatus.PROCESSING);
            order.setTotalAmount(7499.98);

            Order savedOrder = orderRepository.save(order);

            // Dodaj pozycje zamówienia
            OrderItem item1 = new OrderItem();
            item1.setOrder(savedOrder);
            item1.setProductId(laptop.getId());
            item1.setProductName(laptop.getName());
            item1.setQuantity(1);
            item1.setUnitPrice(laptop.getPrice());
            item1.setTotalPrice(laptop.getPrice());

            OrderItem item2 = new OrderItem();
            item2.setOrder(savedOrder);
            item2.setProductId(monitor.getId());
            item2.setProductName(monitor.getName());
            item2.setQuantity(1);
            item2.setUnitPrice(monitor.getPrice());
            item2.setTotalPrice(monitor.getPrice());

            savedOrder.getItems().add(item1);
            savedOrder.getItems().add(item2);
            orderRepository.save(savedOrder);

            log.info("Inicjalizacja danych zakończona!");
            log.info("Utworzono {} kategorii", categoryRepository.count());
            log.info("Utworzono {} produktów", productRepository.count());
            log.info("Utworzono {} zamówień", orderRepository.count());
        };
    }
}