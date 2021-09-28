package com.demo.ecommerce.rest;

import com.demo.ecommerce.exception.NoEntityFoundException;
import com.demo.ecommerce.request.OrderRequestBody;
import com.demo.ecommerce.response.OrdersResponse;
import com.demo.ecommerce.response.OrdersResponseBody;
import com.demo.ecommerce.response.ProductResponse;
import com.demo.ecommerce.response.ProductResponseBody;
import com.demo.ecommerce.service.OrderService;
import com.demo.ecommerce.service.ProductService;
import com.demo.ecommerce.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
@EnableWebMvc
public class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserController userController;

  @MockBean
  private ProductService mockedProductService;

  @MockBean
  private OrderService mockedOrderService;

  private JacksonTester<OrderRequestBody> jsonOrderRequest;

  private JacksonTester<ProductResponse> jsonProductResponse;

  private JacksonTester<OrdersResponse> jsonOrderResponse;

  private JacksonTester<OrdersResponseBody> jsonOrderResponseBody;

  private final String USER_ID = UUID.randomUUID().toString();

  private final String GET_PRODUCT_ENDPOINT = "/api/user/products";

  private final String USER_PLACE_ORDER_ENDPOINT = "/api/user/process-order";

  private final String USER_ORDER_HISTORY_ENDPOINT = "/api/user/orders-history/{userId}";

  @BeforeAll
  private void setup() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  private OrderRequestBody getMockedOrderRequestBody() {
    OrderRequestBody request = OrderRequestBody.builder()
        .productsIds(Arrays.asList("93927900-aa75-4455-a558-90643895c1a1", "3120aadf-778d-42dc-ba6f-d59ad1a90e06", "80d1f9fe-ff56-4525-a00e-347f940e1c09"))
        .userId(USER_ID)
        .build();
    return request;
  }

  private ProductResponse getMockedProductsResponse() {
    ProductResponseBody responseBody1 = ProductResponseBody.builder()
        .id(UUID.fromString("3120aadf-778d-42dc-ba6f-d59ad1a90e06"))
        .category("Jackets")
        .name("Stone island")
        .price(new BigDecimal(280))
        .build();

    ProductResponseBody responseBody2 = ProductResponseBody.builder()
        .id(UUID.fromString("73ada6f7-2dd3-4bea-91ea-8723df8b6554"))
        .category("Jackets")
        .name("Colorblock hooded jacket")
        .price(new BigDecimal(179))
        .build();

    ProductResponseBody responseBody3 = ProductResponseBody.builder()
        .id(UUID.fromString("93927900-aa75-4455-a558-90643895c1a1"))
        .category("Jackets")
        .name("Reversible down vest")
        .price(new BigDecimal(389))
        .build();

    ProductResponseBody responseBody4 = ProductResponseBody.builder()
        .id(UUID.fromString("80d1f9fe-ff56-4525-a00e-347f940e1c09"))
        .category("Shoes")
        .name("Leather high top sneaker")
        .price(new BigDecimal(139.50))
        .build();

    ProductResponseBody responseBody5 = ProductResponseBody.builder()
        .id(UUID.fromString("9b8a24f9-9dac-479c-84fb-b4afc3ad589d"))
        .category("Shoes")
        .name("Retro sneaker")
        .price(new BigDecimal(119.50))
        .build();

    return ProductResponse.builder().products(Arrays.asList(responseBody1, responseBody2, responseBody3, responseBody4, responseBody5)).build();
  }

  private OrdersResponse getMockedOrdersResponse() {
    OrdersResponseBody responseBody1 = OrdersResponseBody.builder()
        .email("test@test.com")
        .orderCreated(new Date(1626127200))
        .products(filterProductResponse(1,3))
        .totalSum(filterProductResponseSum(1,3))
        .build();

    OrdersResponseBody responseBody2 = OrdersResponseBody.builder()
        .email("test@test.com")
        .orderCreated(new Date(1626559200))
        .products(filterProductResponse(2,3))
        .totalSum(filterProductResponseSum(2,3))
        .build();

    OrdersResponseBody responseBody3 = OrdersResponseBody.builder()
        .email("test@test.com")
        .orderCreated(new Date(1628114400))
        .products(filterProductResponse(0,5))
        .totalSum(filterProductResponseSum(0,5))
        .build();

    return OrdersResponse.builder().ordersHistory(Arrays.asList(responseBody1, responseBody2, responseBody3)).build();
  }

  private Date setDate(int year, int month, int day){
    Calendar calendar = Calendar.getInstance();
    calendar.set(year,month,day);
    return calendar.getTime();
  }

  private ProductResponse filterProductResponse(int from, int to) {
    List<ProductResponseBody> subList = getMockedProductsResponse().getProducts().subList(from, to);
    return ProductResponse.builder().products(subList).build();
  }

  private BigDecimal filterProductResponseSum(int from, int to) {
    ProductResponse subList = filterProductResponse(from, to);
    return subList.getProducts().stream().map(product -> product.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Test
  public void getProductsReturnsAllProducts() throws Exception {
    when(mockedProductService.getAllProducts()).thenReturn(getMockedProductsResponse());

    MockHttpServletResponse mockResponse = mockMvc.perform(
        get(GET_PRODUCT_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
    assertEquals(jsonProductResponse.write(getMockedProductsResponse()).getJson(), mockResponse.getContentAsString());
  }

  @Test
  public void saveOrderReturns201WhenRequestValid() throws Exception {
    String mockedRequestJson = jsonOrderRequest.write(getMockedOrderRequestBody()).getJson();

    MvcResult mockResult = mockMvc.perform(
        post(USER_PLACE_ORDER_ENDPOINT)
        .content(mockedRequestJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn();

    assertTrue(mockResult.getResponse().getContentAsString().isEmpty());
  }

  @Test
  public void saveOrderReturns404WhenNoUserFound() throws Exception {
    doThrow(new NoEntityFoundException(Constants.USER_NOT_FOUND_MSG)).when(mockedOrderService).processOrder(getMockedOrderRequestBody());
    String mockedRequestJson = jsonOrderRequest.write(getMockedOrderRequestBody()).getJson();

    MvcResult mockResult = mockMvc.perform(
        post(USER_PLACE_ORDER_ENDPOINT)
        .content(mockedRequestJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(Constants.USER_NOT_FOUND_MSG))
        .andReturn();
  }

  @Test
  public void saveOrderReturns404WhenNoProductsFound() throws Exception {
    doThrow(new NoEntityFoundException(Constants.PRODUCT_NOT_FOUND_MSG)).when(mockedOrderService).processOrder(getMockedOrderRequestBody());
    String mockedRequestJson = jsonOrderRequest.write(getMockedOrderRequestBody()).getJson();

    MvcResult mockResult = mockMvc.perform(
        post(USER_PLACE_ORDER_ENDPOINT)
        .content(mockedRequestJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_NOT_FOUND_MSG))
        .andReturn();
  }

  @Test
  public void saveOrderReturns400WhenMissingParams() throws Exception {
    OrderRequestBody mockedOrderRequestBody = getMockedOrderRequestBody();
    mockedOrderRequestBody.setUserId(null);
    String mockedRequestJson = jsonOrderRequest.write(mockedOrderRequestBody).getJson();

    MvcResult mockResult = mockMvc.perform(
        post(USER_PLACE_ORDER_ENDPOINT)
        .content(mockedRequestJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(Constants.USER_ID_NULL_MSG))
        .andReturn();
  }

  @Test
  public void getUserOrdersHistoryReturnsAllProducts() throws Exception {
    String from = "2021-07-01";
    String to = "2021-08-10";
    Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
    Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);
    when(mockedOrderService.getOrdersHistoryByUserInGivenTimeRange(fromDate, toDate, USER_ID)).thenReturn(getMockedOrdersResponse());

    MockHttpServletResponse mockResponse = mockMvc.perform(
        get(USER_ORDER_HISTORY_ENDPOINT, USER_ID)
        .param("from", from)
        .param("to", to)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertEquals(HttpStatus.OK.value(), mockResponse.getStatus());
    assertEquals(jsonOrderResponse.write(getMockedOrdersResponse()).getJson(), mockResponse.getContentAsString());
  }

  @Test
  public void getUserOrdersHistoryReturns400WhenMissingParams() throws Exception {
    String from = "2021-07-01";
    OrderRequestBody mockedOrderRequestBody = getMockedOrderRequestBody();
    mockedOrderRequestBody.setUserId(null);
    String mockedRequestJson = jsonOrderRequest.write(mockedOrderRequestBody).getJson();

    MockHttpServletResponse mockResult = mockMvc.perform(
        get(USER_ORDER_HISTORY_ENDPOINT, USER_ID)
        .param("from", from)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();
  }

}
