package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private LdapService ldapService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	

	@Override
	public void run(String... args) throws Exception {

		String bindDn = "cn=admin,dc=example,dc=org";
		String password = "admin123";

		System.out.println("Authenticate: " + ldapService.LdapAuthenticate(bindDn, password));
		Map<String, List<String>> cnsList = ldapService.getBaseCNs(bindDn, password);
		cnsList.keySet().forEach(key -> {
			System.out.println(key + " : " + cnsList.get(key));
		});
	}
}
