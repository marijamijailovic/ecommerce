package com.demo.ecommerce.service;

import com.demo.ecommerce.exception.NoEntityFoundException;
import com.demo.ecommerce.model.Products;
import com.demo.ecommerce.request.ProductRequestBody;
import com.demo.ecommerce.response.ProductResponse;
import com.demo.ecommerce.response.ProductResponseBody;

public interface ProductService {
  ProductResponse getAllProducts();
  ProductResponseBody saveProduct(ProductRequestBody ProductRequestBody);
  Products updateProduct(String productId, ProductRequestBody product) throws NoEntityFoundException;
}
