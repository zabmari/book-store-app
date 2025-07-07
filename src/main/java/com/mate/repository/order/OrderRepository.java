package com.mate.repository.order;

import com.mate.model.Order;
import com.mate.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndUser(Long id, User user);

    List<Order> findAllByUser(User user);
}
