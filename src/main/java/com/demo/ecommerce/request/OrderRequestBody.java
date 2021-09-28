package com.demo.ecommerce.request;

import com.demo.ecommerce.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestBody {

  @NotNull(message = Constants.PRODUCTS_ID_NULL_MSG)
  private List<String> productsIds;

  @NotNull(message = Constants.USER_ID_NULL_MSG)
  private String userId;
}
