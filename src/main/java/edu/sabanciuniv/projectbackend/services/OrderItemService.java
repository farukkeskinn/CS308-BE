package edu.sabanciuniv.projectbackend.services;
import org.springframework.transaction.annotation.Transactional;
import edu.sabanciuniv.projectbackend.models.OrderItem;
import edu.sabanciuniv.projectbackend.repositories.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public OrderItem getOrderItemById(String orderItemId) {
        return orderItemRepository.findById(orderItemId).orElse(null);
    }

    @Transactional
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public void deleteOrderItem(String orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }
}
