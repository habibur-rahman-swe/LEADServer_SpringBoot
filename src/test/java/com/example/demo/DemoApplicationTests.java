package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.ldap.task.LdapService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	LdapService ldapService;

	@Test
	void contextLoads() {
	}

	@Test
	void testLdapAuthenticate() {
		String bindDn = "cn=admin,dc=example,dc=org";
		String password = "admin123";
		
		assertThat(ldapService.LdapAuthenticate(bindDn, password)).isEqualTo(true);
	}

	@Test
	void testGetBaseCNs() {
		String bindDn = "cn=admin,dc=example,dc=org";
		String password = "admin123";
		
		// Assuming ldapService is already initialized and available
        Map<String, List<String>> map = new HashMap<>();

        // Print the result
        map.keySet().forEach(key -> {
			System.out.println(key + " : " + map.get(key));
		});

		assertThat(ldapService.getBaseCNs(bindDn, password));;
	}

}
