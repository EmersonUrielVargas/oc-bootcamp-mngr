package com.onclass.bootcamp.infrastructure.entrypoints;

import com.onclass.bootcamp.infrastructure.entrypoints.handler.BootcampHandlerImpl;
import com.onclass.bootcamp.infrastructure.entrypoints.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(BootcampHandlerImpl bootcampHandler) {
        return route(POST(Constants.PATH_POST_BOOTCAMP), bootcampHandler::createBootcamp)
            .andRoute(GET(Constants.PATH_GET_PAGINATED_BOOTCAMPS), bootcampHandler::getAllBootcamps)
            .andRoute(DELETE(Constants.PATH_DELETE_BOOTCAMP), bootcampHandler::deleteBootcamp);
    }
}
