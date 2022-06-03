package com.business.services.impl;

import com.business.services.MarketService;
import com.business.utilts.ApplicationUtils;
import com.business.utilts.H;
import com.core.entity.Product;
import com.core.exception.BadRequestException;
import com.core.mapper.MapperObject;
import com.core.model.Product.CreateProductDTO;
import com.core.model.Product.ProductResponseDTO;
import com.core.model.Product.UpdateProductDTO;
import com.core.repository.ProductReposiroty;
import com.core.utils.Constants;
import com.core.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketServiceImpl implements MarketService {
    @Autowired
    private ProductReposiroty productReposiroty;
    @Autowired
    private MapperObject mapperObject;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Page<ProductResponseDTO> getAllProduct(int pageNumber, int size, String sortByProperties, String sortBy, String keyword) {
        Sort sort = ApplicationUtils.getSort(sortByProperties, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        keyword = StringUtils.buildLikeExp(keyword);
        List<ProductResponseDTO> products = new ArrayList<>();
        /* nếu là trang thông tin thì phải inner join còn admin là left join */
        Page<Product> page =
                productReposiroty.searchProduct(
                        Constants.DELETE.NORMAL,
                        keyword,
                        pageable);
        page.getContent().stream().forEach(u -> {
            products.add(mapperObject.convertToProductResponseDTO(u));
        });
        return new PageImpl<>(products, pageable, page.getTotalElements());
    }

    @Override
    public ProductResponseDTO addProduct(CreateProductDTO createProductDTO) {
        Product product = modelMapper.map(createProductDTO, Product.class);
        product.setSeller(ApplicationUtils.getCurrentUser());
        productReposiroty.save(product);
        return mapperObject.convertToProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(UpdateProductDTO updateProductDTO) throws BadRequestException {
        Product product = productReposiroty.findById(updateProductDTO.getProductId()).orElseThrow(() -> new BadRequestException("Không tìm thấy sản phẩm"));
        if(H.isTrue(updateProductDTO.getProductPrice())){
            product.setProductPrice(updateProductDTO.getProductPrice());
        }
        if(H.isTrue(updateProductDTO.getProductName())){
            product.setProductName(updateProductDTO.getProductName());
        }
        if(H.isTrue(updateProductDTO.getProductDescription())){
            product.setProductDescription(updateProductDTO.getProductDescription());
        }
        productReposiroty.save(product);
        return mapperObject.convertToProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO deleteProduct(Long id) throws BadRequestException {
        Product product = productReposiroty.findById(id).orElseThrow(() -> new BadRequestException("Không tìm thấy sản phẩm"));
        product.setIsDelete(Constants.DELETE.DELETED);
        productReposiroty.save(product);
        return mapperObject.convertToProductResponseDTO(product);
    }
}
