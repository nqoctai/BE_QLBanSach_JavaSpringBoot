package doancuoiki.db_cnpm.QuanLyNhaSach.controller;


import doancuoiki.db_cnpm.QuanLyNhaSach.services.OrderShippingEventService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderShippingEventController {
    private final OrderShippingEventService orderShippingEventService;

    public OrderShippingEventController(OrderShippingEventService orderShippingEventService) {
        this.orderShippingEventService = orderShippingEventService;
    }
}
