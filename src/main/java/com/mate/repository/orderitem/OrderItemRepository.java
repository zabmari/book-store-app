package com.mate.repository.orderitem;

import com.mate.model.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long orderId);
}
