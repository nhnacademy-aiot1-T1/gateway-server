package com.nhnacademy.gateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GatewayApplicationTests {

	@Test
	void contextLoads() {
		int i = 0;
		Assertions.assertEquals(2,i);
	}

}
