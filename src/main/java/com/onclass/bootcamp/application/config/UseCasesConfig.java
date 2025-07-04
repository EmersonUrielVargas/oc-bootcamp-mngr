package com.onclass.bootcamp.application.config;

import com.onclass.bootcamp.domain.api.BootcampServicePort;
import com.onclass.bootcamp.domain.spi.BootcampPersistencePort;
import com.onclass.bootcamp.domain.spi.CapabilitiesGateway;
import com.onclass.bootcamp.domain.usecase.BootcampUseCase;
import com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.BootcampPersistenceAdapter;
import com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.mapper.BootcampEntityMapper;
import com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.repository.BootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {
        private final BootcampRepository bootcampRepository;
        private final BootcampEntityMapper bootcampEntityMapper;

        @Bean
        public BootcampPersistencePort bootcampPersistencePort() {
                return new BootcampPersistenceAdapter(bootcampRepository,bootcampEntityMapper);
        }

        @Bean
        public BootcampServicePort bootcampServicePort(BootcampPersistencePort bootcampPersistencePort, CapabilitiesGateway capabilitiesGateway){
                return new BootcampUseCase(bootcampPersistencePort, capabilitiesGateway);
        }
}
