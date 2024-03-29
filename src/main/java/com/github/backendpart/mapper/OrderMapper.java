package com.github.backendpart.mapper;

import com.github.backendpart.web.dto.order.OrderProductListDto;
import com.github.backendpart.web.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "productname", source = "product.productName")
    @Mapping(target = "size", source = "option.optionName")
    @Mapping(target = "quantity", source = "cartQuantity")
    @Mapping(target = "price", source = "product.productPrice")
    @Mapping(target = "productDescription", source = "product.productDescription")
    OrderProductListDto CartEntityToDTO(CartEntity cartEntity);
}
