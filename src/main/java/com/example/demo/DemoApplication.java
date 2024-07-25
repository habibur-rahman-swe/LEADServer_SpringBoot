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

	@Autowired
	private LdapConfig ldapConfig;
	
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    	
    	try {
    		System.out.println(ldapConfig.configureLdap());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
        LdapConnectionParameters connectionParams = new LdapConnectionParameters();
        connectionParams.setHostname("localhost");
        connectionParams.setPort(1389);
        connectionParams.setConnectionTimeout(5);

        LdapAuthenticationParameters authParams = new LdapAuthenticationParameters();
        //authParams.setBindDn("cn=admin,dc=example,dc=org");
        authParams.setBindDn("cn=user1,ou=users,dc=example,dc=org");
        authParams.setPassword("password1");

        if (ldapService.checkNetworkParameters(connectionParams)) {
            System.out.println("Network parameters are valid.");
            
            if (ldapService.authenticate(connectionParams, authParams)) {
                System.out.println("Authentication successful.");
                
                Map<String, List<String>> baseDNs = ldapService.printBaseDNs(authParams);
                baseDNs.keySet().forEach(key -> {
                	System.out.println(key + " : " + baseDNs.get(key));
                });
                
            } else {
                System.out.println("Authentication failed.");
            }
        } else {
            System.out.println("Network parameters are invalid.");
        }
    }
}
