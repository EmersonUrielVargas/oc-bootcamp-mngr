package com.onclass.bootcamp.infrastructure.entrypoints;

import com.onclass.bootcamp.infrastructure.entrypoints.handler.BootcampHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(BootcampHandlerImpl bootcampHandler) {
        return route(POST("/bootcamp"), bootcampHandler::createBootcamp);
    }
}
