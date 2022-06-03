package com.business.services;

import com.core.exception.BadRequestException;
import com.core.model.Product.CreateProductDTO;
import com.core.model.Product.ProductResponseDTO;
import com.core.model.Product.UpdateProductDTO;
import org.springframework.data.domain.Page;

public interface MarketService {
    Page<ProductResponseDTO> getAllProduct(int page, int size, String sortByProperties, String sortBy, String keyword);

    ProductResponseDTO addProduct(CreateProductDTO createProductDTO);

    ProductResponseDTO updateProduct(UpdateProductDTO updateProductDTO) throws BadRequestException;

    ProductResponseDTO deleteProduct(Long id) throws BadRequestException;
}
