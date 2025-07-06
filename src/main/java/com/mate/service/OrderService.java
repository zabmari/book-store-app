package com.mate.service;

import com.mate.dto.order.OrderRequestDto;
import com.mate.dto.order.OrderResponseDto;
import com.mate.dto.order.UpdateOrderStatusDto;
import com.mate.dto.orderitems.OrderItemDto;
import com.mate.model.User;
import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getByUser(User user);

    OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusDto updateOrderStatusDto);

    OrderResponseDto addOrder(User user, OrderRequestDto orderRequestDto);

    List<OrderItemDto> getOrderItemsByOrderId(User user, Long orderId);

    OrderItemDto getOrderItemByOrderItemId(User user, Long orderId, Long itemId);

}
