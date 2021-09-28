package com.demo.ecommerce.service.impl;

import com.demo.ecommerce.exception.NoEntityFoundException;
import com.demo.ecommerce.exception.RequestNotValidException;
import com.demo.ecommerce.model.*;
import com.demo.ecommerce.repository.OrderProductRepository;
import com.demo.ecommerce.repository.OrdersRepository;
import com.demo.ecommerce.repository.ProductsRepository;
import com.demo.ecommerce.repository.UsersRepository;
import com.demo.ecommerce.request.OrderRequestBody;
import com.demo.ecommerce.response.OrdersResponse;
import com.demo.ecommerce.response.OrdersResponseBody;
import com.demo.ecommerce.response.ProductResponse;
import com.demo.ecommerce.response.ProductResponseBody;
import com.demo.ecommerce.service.OrderService;
import com.demo.ecommerce.util.Constants;
import com.demo.ecommerce.util.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  OrdersRepository ordersRepository;

  @Autowired
  ProductsRepository productsRepository;

  @Autowired
  UsersRepository usersRepository;

  @Autowired
  OrderProductRepository orderProductRepository;

  @Override
  public void processOrder(OrderRequestBody orderRequest) throws NoEntityFoundException {
      if(!validOrderRequest(orderRequest)) {
        throw new RequestNotValidException(Constants.PARAM_REQUEST_FORMAT_NOT_VALID_MSG);
      }
      Optional<Users> user = getUser(orderRequest.getUserId());
      if(user.isEmpty()) {
        throw new NoEntityFoundException(Constants.USER_NOT_FOUND_MSG);
      }
      List<String> productsIds = orderRequest.getProductsIds();

      createAndSaveOrderProduct(user.get(), productsIds);
  }

  @Override
  public OrdersResponse getOrdersHistoryByUserInGivenTimeRange(Date from, Date to, String userId) throws NoEntityFoundException {
    if(!Helpers.validUUID(userId)) {
      throw new RequestNotValidException(Constants.PARAM_REQUEST_FORMAT_NOT_VALID_MSG);
    }

    Optional<Users> user = getUser(userId);
    if(user.isEmpty()) {
      throw new NoEntityFoundException(Constants.USER_NOT_FOUND_MSG);
    }

    Optional<List<Orders>> orders = ordersRepository.findByUserAndCreatedBetween(user.get(), from, to);
    if(orders.isEmpty()) {
      throw new NoEntityFoundException(Constants.USER_ORDERS_HISTORY_NOT_FOUND_MSG);
    }

    List<OrdersResponseBody> ordersHistory = new ArrayList<>();
    orders.get().forEach(order -> {
      BigDecimal orderTotalSum = calculateTotalOrderSum(order);
      ProductResponse allProducts = getAllProducts(order);
      OrdersResponseBody ordersResponseBody = createOrderResponse(user.get(), order, allProducts, orderTotalSum);
      ordersHistory.add(ordersResponseBody);
    });

    OrdersResponse allOrdersByUser = OrdersResponse.builder().ordersHistory(ordersHistory).build();

    return allOrdersByUser;
  }

  private ProductResponse getAllProducts(Orders order) {
    Optional<List<OrderProduct>> orderProducts = orderProductRepository.findByOrders(order);
    if(orderProducts.isEmpty()) {
      throw new NoEntityFoundException(Constants.ORDER_PRODUCTS_NOT_FOUND_MSG);
    }

    List<ProductResponseBody> productsList = orderProducts.get().stream()
        .map(OrderProduct::getProducts)
        .map(ProductResponseBody::createResponseFromProduct)
        .collect(Collectors.toList());

    return ProductResponse.builder().products(productsList).build();
  }

  private BigDecimal calculateTotalOrderSum(Orders order) {
    Optional<BigDecimal> orderTotalSum = orderProductRepository.getTotalOrderSum(order.getId());
    if(orderTotalSum.isEmpty()) {
      throw new NoEntityFoundException(Constants.ORDER_NOT_FOUND_MSG);
    }
    return orderTotalSum.get();
  }

  private boolean validOrderRequest(OrderRequestBody orderRequest) {
    if(!Helpers.validUUID(orderRequest.getUserId())) {
      return false;
    }
    if(orderRequest.getProductsIds().stream().anyMatch(id -> !Helpers.validUUID(id))) {
      return false;
    }
    return true;
  }

  private Optional<Users> getUser(String userId) throws IllegalArgumentException{
    return usersRepository.findByUuid(UUID.fromString(userId));
  }

  private OrdersResponseBody createOrderResponse(Users user, Orders order, ProductResponse allProducts, BigDecimal orderTotalSum) {
    return OrdersResponseBody.builder()
        .email(user.getEmail())
        .orderCreated(order.getCreated())
        .products(allProducts)
        .totalSum(orderTotalSum)
        .build();
  }

  private void createAndSaveOrderProduct(Users user, List<String> productsIds) {
    List<OrderProduct> orderProductList = new ArrayList<>();
    Orders order = createOrder(user);

    productsIds.forEach(productId -> {
      Products productDb = findProductInDb(productId);
      OrderProduct orderProduct = createOrderProduct(order, productDb);
      orderProductList.add(orderProduct);
    });
    ordersRepository.saveAndFlush(order);
    orderProductRepository.saveAllAndFlush(orderProductList);
  }

  private Orders createOrder(Users user) {
    return Orders.builder()
        .id(UUID.randomUUID())
        .created(new Date())
        .user(user)
        .build();
  }

  private Products findProductInDb(String productId) {
    Optional<Products> productDb = productsRepository.findByUuid(UUID.fromString(productId));
    if(productDb.isEmpty()) {
      throw new NoEntityFoundException(Constants.PRODUCT_NOT_FOUND_MSG);
    }
    return productDb.get();
  }

  private OrderProduct createOrderProduct(Orders order, Products productDb) {
    return  OrderProduct.builder()
        .orderProductPK(OrderProductPK.builder()
            .ordersId(order.getId())
            .productsId(productDb.getId())
            .build())
        .orders(order)
        .products(productDb)
        .productPrice(productDb.getPrice())
        .build();
  }
}
