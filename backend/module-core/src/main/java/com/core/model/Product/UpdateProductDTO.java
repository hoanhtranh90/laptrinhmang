package com.core.model.Product;

import lombok.Data;

@Data
public class UpdateProductDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productStatus;
}
