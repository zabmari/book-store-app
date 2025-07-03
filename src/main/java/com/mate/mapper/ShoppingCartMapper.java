package com.mate.mapper;

import com.mate.config.MapperConfig;
import com.mate.dto.shoppingcart.ShoppingCartResponseDto;
import com.mate.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
