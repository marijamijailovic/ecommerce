package com.demo.ecommerce.service.impl;

import com.demo.ecommerce.exception.DoubleEntityFoundException;
import com.demo.ecommerce.exception.NoEntityFoundException;
import com.demo.ecommerce.exception.RequestNotValidException;
import com.demo.ecommerce.model.Products;
import com.demo.ecommerce.repository.ProductsRepository;
import com.demo.ecommerce.request.ProductRequestBody;
import com.demo.ecommerce.response.ProductResponse;
import com.demo.ecommerce.response.ProductResponseBody;
import com.demo.ecommerce.service.ProductService;
import com.demo.ecommerce.util.Constants;
import com.demo.ecommerce.util.Helpers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService{

  @Autowired
  ProductsRepository productsRepository;

  @Override
  public ProductResponse getAllProducts() {
    List<Products> allProducts = productsRepository.findAll();
    List<ProductResponseBody> response = allProducts.stream()
        .map(ProductResponseBody::createResponseFromProduct)
        .collect(Collectors.toList());
    return ProductResponse.builder()
        .products(response)
        .build();
  }

  @Override
  public ProductResponseBody saveProduct(ProductRequestBody productRequestBody) {
    Optional<Products> productDb = productsRepository.findByCategoryAndNameAndPrice(
        productRequestBody.getCategory(),
        productRequestBody.getName(),
        productRequestBody.getPrice());
    if(productDb.isPresent()) {
      throw new DoubleEntityFoundException(Constants.PRODUCT_ALREADY_ADDED);
    }
    Products product = Products.builder()
        .category(productRequestBody.getCategory())
        .name(productRequestBody.getName())
        .price(productRequestBody.getPrice())
        .build();
    Products savedProduct = productsRepository.save(product);
    return ProductResponseBody.createResponseFromProduct(savedProduct);
  }

  @Override
  public Products updateProduct(String productId, ProductRequestBody productRequest) throws NoEntityFoundException {
    if(!Helpers.validUUID(productId)) {
      throw new RequestNotValidException(Constants.PARAM_REQUEST_FORMAT_NOT_VALID_MSG);
    }

    Optional<Products> productDb = productsRepository.findByUuid(UUID.fromString(productId));
    if(productDb.isEmpty()) {
      throw new NoEntityFoundException(Constants.PRODUCT_NOT_FOUND_MSG);
    }
    Products product = productDb.get();
    product.setCategory(productRequest.getCategory());
    product.setName(productRequest.getName());
    product.setPrice(productRequest.getPrice());
    product.setUpdated(new Date());
    productsRepository.save(product);

    return product;
  }
}
