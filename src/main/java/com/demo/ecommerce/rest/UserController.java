package com.demo.ecommerce.rest;

import com.demo.ecommerce.exception.NoEntityFoundException;
import com.demo.ecommerce.request.OrderRequestBody;
import com.demo.ecommerce.response.OrdersResponse;
import com.demo.ecommerce.response.OrdersResponseBody;
import com.demo.ecommerce.response.ProductResponse;
import com.demo.ecommerce.service.OrderService;
import com.demo.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  @Autowired
  private ProductService productService;

  @Autowired
  private OrderService orderService;

  @GetMapping(value="/products")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Successfully fetch all products",
          content = @Content(mediaType = "application/json"))})
  public ResponseEntity<ProductResponse> getAllProducts() {
    ProductResponse response = productService.getAllProducts();
    return ResponseEntity.ok(response);
  }

  @PostMapping(value="/process-order")
  @ApiResponses(value =
      { @ApiResponse(
          responseCode = "201",
          description = "Successfully create order",
          content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "404",
            description = "The resource does not exist",
            content = @Content(mediaType = "application/json"))
      })
  public ResponseEntity<OrdersResponseBody> processOrder(@Valid @RequestBody OrderRequestBody orderRequest) throws NoEntityFoundException {
    orderService.processOrder(orderRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping(value="/orders-history/{userId}")
  @ApiResponses(value =
      { @ApiResponse(
          responseCode = "200",
          description = "Successfully create order",
          content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "404",
            description = "The resource does not exist",
            content = @Content(mediaType = "application/json"))
      })
  public ResponseEntity<OrdersResponse> getOrdersHistoryByUserInGivenTimeRange(
      @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
      @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to,
      @PathVariable String userId) throws NoEntityFoundException {
    OrdersResponse response = orderService.getOrdersHistoryByUserInGivenTimeRange(from, to, userId);
    return ResponseEntity.ok(response);
  }
}
