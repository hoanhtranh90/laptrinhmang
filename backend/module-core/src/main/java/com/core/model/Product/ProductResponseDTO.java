package com.core.model.Product;

import com.core.entity.User;
import com.core.model.user.UserDTO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class ProductResponseDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Long productStatus;
    private UserDTO seller;
}
