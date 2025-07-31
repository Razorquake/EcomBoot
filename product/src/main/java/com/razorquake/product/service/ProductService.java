package com.razorquake.product.service;


import com.razorquake.product.dto.ProductRequest;
import com.razorquake.product.dto.ProductResponse;
import com.razorquake.product.model.Product;
import com.razorquake.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public ProductResponse createProduct(ProductRequest productRequest) {
        return mapToProductResponse(
            productRepository.save(updateProduct(new Product(), productRequest))
        );
    }

    private Product updateProduct(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
        return product;
    }
    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setCategory(product.getCategory());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setActive(product.getActive());
        return productResponse;
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct -> mapToProductResponse(productRepository.save(
                    updateProduct(existingProduct, productRequest)
                )));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    public Boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false); // Mark as inactive instead of deleting
                    productRepository.save(product);// Return null to indicate successful deletion
                    return true;
                }).orElse(false);
    }



    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProductsByKeyword(keyword)
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }


    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .map(this::mapToProductResponse);
    }
}
