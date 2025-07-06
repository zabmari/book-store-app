package com.mate.mapper;

import com.mate.config.MapperConfig;
import com.mate.dto.order.OrderResponseDto;
import com.mate.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);
}
