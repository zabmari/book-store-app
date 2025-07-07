package com.mate.mapper;

import com.mate.config.MapperConfig;
import com.mate.dto.orderitems.OrderItemDto;
import com.mate.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);
}
