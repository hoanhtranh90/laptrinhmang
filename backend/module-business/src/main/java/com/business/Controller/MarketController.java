package com.business.Controller;

import com.business.services.MarketService;
import com.core.config.ApplicationConfig;
import com.core.exception.BadRequestException;
import com.core.exception.PermissionException;
import com.core.model.Product.CreateProductDTO;
import com.core.model.Product.UpdateProductDTO;
import com.core.model.ResponseBody;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/market")
public class MarketController {
    @Autowired
    private MarketService marketService;
    @Autowired
    private ApplicationConfig.MessageSourceVi messageSourceVi;
    //list products
    @PostMapping("/search")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> getAllContract(
            @ApiParam(value = "Số trang", defaultValue = "0") @RequestParam(name = "page",
                    defaultValue = "0") int page,
            @ApiParam(value = "Số bản ghi / trang", defaultValue = "10") @RequestParam(name = "size",
                    defaultValue = "10") int size,
            @ApiParam(value = "Sắp xếp theo các thuộc tính", defaultValue = "modifiedDate") @RequestParam(
                    name = "properties", defaultValue = "modifiedDate", required = true) String sortByProperties,
            @ApiParam(value = "Loại sắp xếp ", defaultValue = "ASC") @RequestParam(name = "sortBy",
                    defaultValue = "ASC") String sortBy,
            @ApiParam(value = "Từ khóa tìm kiếm: ") @RequestParam(name = "keyword",
                    required = false) String keyword) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(marketService.getAllProduct(page, size, sortByProperties, sortBy, keyword)).message(messageSourceVi.getMessageVi("OK002")).build());
    }
    //add products
    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> addProduct(@Valid @RequestBody CreateProductDTO createProductDTO) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(marketService.addProduct(createProductDTO)).message(messageSourceVi.getMessageVi("OK002")).build());
    }
    //update products
    @PostMapping("/update")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductDTO updateProductDTO) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(marketService.updateProduct(updateProductDTO)).message(messageSourceVi.getMessageVi("OK002")).build());
    }
    //delete products
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) throws BadRequestException, PermissionException {
        return ResponseEntity
                .ok(ResponseBody.builder().body(marketService.deleteProduct(id)).message(messageSourceVi.getMessageVi("OK002")).build());
    }

}
