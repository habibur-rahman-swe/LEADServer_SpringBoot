package com.example.demo;

import java.util.Hashtable;

import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LdapConfig {

    @Value("${java.naming.factory.initial}")
    private String factoryInitial;

    @Value("${java.naming.provider.url}")
    private String providerUrl;

    @Value("${com.sun.jndi.ldap.connect.timeout}")
    private String connectTimeout;

    public boolean configureLdap() throws Exception {
    	Hashtable<String, Object> env = new Hashtable<>();
        env.put("java.naming.factory.initial", factoryInitial);
        env.put("java.naming.provider.url", providerUrl);
        env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(connectTimeout));
        DirContext ctx = new InitialLdapContext(env, null);
        ctx.close();
        return true;
    }
}