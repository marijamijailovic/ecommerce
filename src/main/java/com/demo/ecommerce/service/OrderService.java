package com.demo.ecommerce.service;

import com.demo.ecommerce.exception.NoEntityFoundException;
import com.demo.ecommerce.request.OrderRequestBody;
import com.demo.ecommerce.response.OrdersResponse;

import java.util.Date;

public interface OrderService {
  void processOrder(OrderRequestBody order) throws NoEntityFoundException;
  OrdersResponse getOrdersHistoryByUserInGivenTimeRange(Date from, Date to, String userId) throws NoEntityFoundException;
}
