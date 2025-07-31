package com.mate.controller;

import com.mate.dto.order.OrderRequestDto;
import com.mate.dto.order.OrderResponseDto;
import com.mate.dto.order.UpdateOrderStatusDto;
import com.mate.dto.orderitems.OrderItemDto;
import com.mate.model.User;
import com.mate.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Endpoints for managing orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add new order", description = "Add new order")
    public OrderResponseDto addOrder(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.addOrder(user, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get user orders", description = "Get user orders")
    public List<OrderResponseDto> getOrders(@AuthenticationPrincipal User user) {
        return orderService.getByUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order status", description = "Update order status")
    public OrderResponseDto updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusDto updateOrderStatusDto) {
        return orderService.updateOrderStatus(id, updateOrderStatusDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all order items by id",
            description = "Get all order items from specific order")
    public List<OrderItemDto> getOrderItems(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return orderService.getOrderItemsByOrderId(user, orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item by id",
            description = "Get specific order item from specific order")
    public OrderItemDto getOrderItem(@AuthenticationPrincipal User user,
                                     @PathVariable Long orderId,
                                     @PathVariable Long itemId) {
        return orderService.getOrderItemByOrderItemId(user, orderId, itemId);
    }
}
