package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ldap")
public class LdapController {

    @Autowired
    private LdapService ldapService;

    @PostMapping("/check-network")
    public boolean checkNetwork(@RequestBody LdapConnectionParameters params) {
        return ldapService.checkNetworkParameters(params);
    }

    @PostMapping("/authenticate")
    public boolean authenticate(@RequestBody LdapAuthenticationParameters authParams, @RequestParam String hostname, @RequestParam int port, @RequestParam int timeout) {
        LdapConnectionParameters params = new LdapConnectionParameters();
        params.setHostname(hostname);
        params.setPort(port);
        params.setConnectionTimeout(timeout);
        return ldapService.authenticate(params, authParams);
    }

    
}