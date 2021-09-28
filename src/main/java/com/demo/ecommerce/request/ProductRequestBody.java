package com.demo.ecommerce.request;

import com.demo.ecommerce.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestBody {
  @NotNull(message = Constants.PRODUCT_CATEGORY_NULL_MSG)
  private String category;
  @NotNull(message = Constants.PRODUCT_NAME_NULL_MSG)
  private String name;
  @NotNull(message = Constants.PRODUCT_PRICE_NULL_MSG)
  private BigDecimal price;
}
