package com.core.model.Product;

import lombok.Data;

@Data
public class CreateProductDTO {
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Long productStatus;
}
