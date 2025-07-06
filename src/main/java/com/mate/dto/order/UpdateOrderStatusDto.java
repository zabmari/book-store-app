package com.mate.dto.order;

import com.mate.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    @NotNull
    private Order.Status status;
}
