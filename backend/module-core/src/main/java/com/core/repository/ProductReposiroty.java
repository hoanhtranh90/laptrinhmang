package com.core.repository;

import com.core.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductReposiroty extends JpaRepository<Product, Long> {
    @Query("select p from Product p where (p.isDelete = :isDelete)"
                + " and (:keyword is null or LOWER(p.productName)like :keyword)"
    )
    Page<Product> searchProduct(Long isDelete, String keyword, Pageable pageable);
}
