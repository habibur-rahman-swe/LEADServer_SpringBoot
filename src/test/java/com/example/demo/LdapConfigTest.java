package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LdapConfigTest {

	@Autowired
	LdapConfig ldapConfig;
	
	@Test
	void test() {
		assertTrue(false);
	}

}
