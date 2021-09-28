package com.demo.ecommerce.rest;

import com.demo.ecommerce.exception.NoEntityFoundException;
import com.demo.ecommerce.request.ProductRequestBody;
import com.demo.ecommerce.response.ProductResponseBody;
import com.demo.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

  @Autowired
  private ProductService productService;

  @PostMapping(value="/add-product")
  @ApiResponses(value =
      { @ApiResponse(
          responseCode = "201",
          description = "Successfully add product",
          content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "400",
            description = "The resource already exist",
            content = @Content(mediaType = "application/json"))
      })
  public ResponseEntity<ProductResponseBody> addProduct(@Valid @RequestBody ProductRequestBody productRequest) {
    ProductResponseBody response = productService.saveProduct(productRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping(value="/update-product/{productId}")
  @ApiResponses(value =
      { @ApiResponse(
          responseCode = "204",
          description = "Successfully update product",
          content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "404",
            description = "The product does not exist",
            content = @Content(mediaType = "application/json"))
      })
  public ResponseEntity<ProductResponseBody> updateProduct(@PathVariable String productId, @Valid @RequestBody ProductRequestBody productRequest) throws NoEntityFoundException {
      productService.updateProduct(productId, productRequest);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
