package com.demo.ecommerce.rest;

import com.demo.ecommerce.exception.NoEntityFoundException;
import com.demo.ecommerce.request.ProductRequestBody;
import com.demo.ecommerce.response.ProductResponseBody;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
@EnableWebMvc
public class AdminControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AdminController adminController;

  @MockBean
  private ProductService mockedProductService;

  private JacksonTester<ProductRequestBody> jsonProductRequest;

  private JacksonTester<ProductResponseBody> jsonProductResponse;

  private final String PRODUCT_ID = UUID.randomUUID().toString();

  private final String SAVE_PRODUCT_ENDPOINT = "/api/admin/add-product";

  private final String UPDATE_PRODUCT_ENDPOINT = "/api/admin/update-product/{productId}";

  @BeforeAll
  private void setup() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  private ProductRequestBody getMockedProductRequestBody() {
    ProductRequestBody request = ProductRequestBody.builder()
        .category("Jackets")
        .name("Stone island")
        .price(new BigDecimal(280))
        .build();
    return request;
  }

  private ProductResponseBody getMockedProductResponseBody() {
    ProductResponseBody response = ProductResponseBody.builder()
        .category("Jackets")
        .name("Stone island")
        .price(new BigDecimal(280))
        .build();
    return response;
  }

  @Test
  public void saveProductReturnsSavedProduct() throws Exception {
    when(mockedProductService.saveProduct(getMockedProductRequestBody())).thenReturn(getMockedProductResponseBody());
    String mockedRequestJson = jsonProductRequest.write(getMockedProductRequestBody()).getJson();

    MockHttpServletResponse mockResponse = mockMvc.perform(
        post(SAVE_PRODUCT_ENDPOINT)
        .content(mockedRequestJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertEquals(HttpStatus.CREATED.value(), mockResponse.getStatus());
    assertEquals(jsonProductResponse.write(getMockedProductResponseBody()).getJson(), mockResponse.getContentAsString());
  }

  @Test
  public void saveProductReturns400WhenMissingParams() throws Exception {
    ProductRequestBody mockedProductRequestBody = getMockedProductRequestBody();
    mockedProductRequestBody.setPrice(null);

    String mockedRequestJson = jsonProductRequest.write(mockedProductRequestBody).getJson();

    MvcResult mockResult = mockMvc.perform(
        post(SAVE_PRODUCT_ENDPOINT)
        .content(mockedRequestJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_PRICE_NULL_MSG))
        .andReturn();
  }

  @Test
  public void updateProductReturns204WhenRequestValid() throws Exception {
    String mockedRequestJson = jsonProductRequest.write(getMockedProductRequestBody()).getJson();

    MvcResult mockResult = mockMvc.perform(
        put(UPDATE_PRODUCT_ENDPOINT, PRODUCT_ID)
        .content(mockedRequestJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();

    assertTrue(mockResult.getResponse().getContentAsString().isEmpty());
  }

  @Test
  public void updateProductReturns404WhenNoProductFound() throws Exception {
    String mockedRequestJson = jsonProductRequest.write(getMockedProductRequestBody()).getJson();
    when(mockedProductService.updateProduct(PRODUCT_ID, getMockedProductRequestBody())).thenThrow(new NoEntityFoundException(Constants.PRODUCT_NOT_FOUND_MSG));

    MvcResult mockResult = mockMvc.perform(
        put(UPDATE_PRODUCT_ENDPOINT, PRODUCT_ID)
        .content(mockedRequestJson)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  public void updateProductReturns400WhenMissingParams() throws Exception {
    ProductRequestBody mockedProductRequestBody = getMockedProductRequestBody();
    mockedProductRequestBody.setCategory(null);
    String mockedRequestJson = jsonProductRequest.write(mockedProductRequestBody).getJson();

    MvcResult mockResult = mockMvc.perform(
        put(UPDATE_PRODUCT_ENDPOINT, PRODUCT_ID)
            .content(mockedRequestJson)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(Constants.PRODUCT_CATEGORY_NULL_MSG))
        .andReturn();
  }
}
