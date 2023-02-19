package com.dodo.customerservice

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
	classes = [CustomerServiceApplication::class],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CustomerServiceApplicationTests {
	@Test
	fun contextLoads() {
	}
}
