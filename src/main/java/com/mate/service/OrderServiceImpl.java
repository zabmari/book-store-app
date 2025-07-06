package com.mate.service;

import com.mate.dto.order.OrderRequestDto;
import com.mate.dto.order.OrderResponseDto;
import com.mate.dto.order.UpdateOrderStatusDto;
import com.mate.dto.orderitems.OrderItemDto;
import com.mate.exception.EntityNotFoundException;
import com.mate.mapper.OrderItemMapper;
import com.mate.mapper.OrderMapper;
import com.mate.model.Order;
import com.mate.model.OrderItem;
import com.mate.model.ShoppingCart;
import com.mate.model.User;
import com.mate.repository.order.OrderRepository;
import com.mate.repository.orderitem.OrderItemRepository;
import com.mate.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderResponseDto> getByUser(User user) {
        List<Order> orders = orderRepository.findAllByUser(user);
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("Can't find orders for user: " + user.getEmail());
        }
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId,
                                              UpdateOrderStatusDto updateOrderStatusDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order with id: " + orderId));
        order.setStatus(updateOrderStatusDto.getStatus());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderResponseDto addOrder(User user, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping cart by user: " + user.getEmail()));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Shopping cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.NEW);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        BigDecimal total = orderItems.stream()
                        .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(User user, Long orderId) {
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find order for user: " + user.getEmail()));
        return orderItemRepository.findAllByOrderId(orderId)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemByOrderItemId(User user, Long orderId, Long itemId) {
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order with id: " + orderId + " doesn't belong to user: "
                                + user.getEmail()));
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .filter(i -> i.getOrder().getId().equals(orderId))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item with id: " + itemId
                                + " for order with id: " + orderId));
        return orderItemMapper.toDto(orderItem);
    }
}
