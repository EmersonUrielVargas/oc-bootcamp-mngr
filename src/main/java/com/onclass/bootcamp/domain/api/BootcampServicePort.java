package com.onclass.bootcamp.domain.api;

import com.onclass.bootcamp.domain.enums.ItemSortList;
import com.onclass.bootcamp.domain.enums.OrderList;
import com.onclass.bootcamp.domain.model.Bootcamp;
import com.onclass.bootcamp.domain.model.spi.BootcampList;
import com.onclass.bootcamp.domain.utilities.CustomPage;
import reactor.core.publisher.Mono;

public interface BootcampServicePort {
    Mono<Bootcamp> registerBootcamp(Bootcamp bootcamp);
    Mono<CustomPage<BootcampList>> listBootcamps(OrderList order, ItemSortList item, Integer page, Integer size);
    Mono<Bootcamp> deleteBootcamp(Long bootcampId);
}
