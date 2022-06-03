package com.core.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product")
@Setter
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Product extends Auditable<String> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_description")
    private String productDescription;
    @Column(name = "product_price")
    private Double productPrice;
    //status
    @Column(name = "product_status")
    private String productStatus;
    //seller
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    //isDelete
    @Column(name = "is_delete", columnDefinition = "bigint default 0")
    private Long isDelete = 0L;
}
