package com.onclass.bootcamp;

import com.onclass.bootcamp.domain.spi.BootcampPersistencePort;
import com.onclass.bootcamp.domain.usecase.BootcampUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = BootcampApplication.class)
class BootcampApplicationTests {

	@MockitoBean
	private BootcampPersistencePort bootcampPersistencePort;

	@Autowired
	private BootcampUseCase technologyUseCase;

	@Test
	void contextLoads() {
	}

}
