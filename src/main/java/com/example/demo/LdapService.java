package com.example.demo;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.security.MessageDigest;
import java.util.Base64;

import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;

@Service
public class LdapService {

    private LdapContextSource contextSource;

    public boolean checkNetworkParameters(LdapConnectionParameters params) {
        try {
            Hashtable<String, Object> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
            env.put("java.naming.provider.url", "ldap://" + params.getHostname() + ":" + params.getPort());
            env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(params.getConnectionTimeout() * 1000));
            DirContext ctx = new InitialLdapContext(env, null);
            ctx.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticate(LdapConnectionParameters params, LdapAuthenticationParameters authParams) {
        try {
            contextSource = new LdapContextSource();
            contextSource.setUrl("ldap://" + params.getHostname() + ":" + params.getPort());
            contextSource.setUserDn(authParams.getBindDn());
            contextSource.setPassword(authParams.getPassword());
            contextSource.afterPropertiesSet();
            LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
            ldapTemplate.getContextSource().getContext(authParams.getBindDn(), authParams.getPassword());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, List<String>> printBaseDNs(LdapAuthenticationParameters authParams) {
        List<String> results = new ArrayList<>();
        
        // store the user and their mails only
        Map<String, List<String>> tResult = new HashMap<>();
        List<String> mails = new ArrayList<>();
        
        try {
        	LdapContext ctx = (LdapContext) contextSource.getContext(authParams.getBindDn(), authParams.getPassword());
        	
        	// Define search controls
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Search the entire subtree

            // Perform the search cxt.serarch(baseDN, ... )
            javax.naming.NamingEnumeration<SearchResult> namingEnum = ctx.search("dc=example,dc=org", "(objectClass=*)", searchControls);
            while (namingEnum.hasMore()) {
                SearchResult result = namingEnum.next();
                results.add(result.getNameInNamespace());
                javax.naming.directory.Attributes attrs = result.getAttributes();
                for (javax.naming.NamingEnumeration<?> ae = attrs.getAll(); ae.hasMore();) {
                    javax.naming.directory.Attribute attr = (javax.naming.directory.Attribute) ae.next();
                    // results.add("Attribute: " + attr.getID());
                    
                    for (javax.naming.NamingEnumeration<?> vals = attr.getAll(); vals.hasMore();) {
                    	String value = vals.next().toString();
                    	
                    	// habib_48
                        if (attr.getID().compareTo("cn") == 0) {
                        	tResult.put(value, mails);
                        	mails = new ArrayList<String>();
                        }
                    	
                    	if (attr.getID().compareTo("mail") == 0) {
                        	mails.add(value);
                        }

                    	if (attr.getID().compareTo("userPassword") == 0) {
                            System.out.println(attr.getID() + " : " + value);
                            if (checkPassword(value, "password1")) {
                                System.out.println("Hello! I'm the password....");
                            }
                        }
                    	// results.add("Value: " + value);
                    }
                }
            }
            
            namingEnum.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tResult;
    }

    public boolean checkPassword(String ldapPass, String givenPass) {
        try {
            // Decode the Base64 encoded LDAP password
            byte[] storedPassword = decodeBase64(ldapPass);
            System.out.println("Decoded LDAP Password (byte array): " + Arrays.toString(storedPassword));

            // Extract the salt and stored hash
            byte[] salt = Arrays.copyOfRange(storedPassword, 0, 8);
            byte[] storedHash = Arrays.copyOfRange(storedPassword, 8, storedPassword.length);

            // Print salt and hash for debugging
            System.out.println("Salt: " + Arrays.toString(salt));
            System.out.println("Stored Hash: " + Arrays.toString(storedHash));

            // Generate the hash for the given password with the extracted salt
            byte[] givenHash = hashPassword(givenPass, salt);

            // Compare the generated hash with the stored hash
            return Arrays.equals(storedHash, givenHash);
        } catch (IllegalArgumentException e) {
            System.err.println("Error decoding Base64 or processing password: " + e.getMessage());
            return false;
        }
    }

    private byte[] decodeBase64(String base64String) {
        try {
            return Base64.getDecoder().decode(base64String);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 input: " + base64String, e);
        }
    }

    private byte[] hashPassword(String password, byte[] salt) {
        try {
            // Create a MessageDigest instance for SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // Add the salt to the digest
            md.update(salt);

            // Add the password to the digest
            md.update(password.getBytes());

            // Compute the hash
            byte[] hashedPassword = md.digest();

            // Combine salt and hash for final result
            byte[] saltAndHash = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
            System.arraycopy(hashedPassword, 0, saltAndHash, salt.length, hashedPassword.length);

            // Return the combined result
            return saltAndHash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
