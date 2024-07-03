package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;

@Service
public class LdapServiceImpl implements LdapService {

	private LdapContextSource contextSource;

	private LdapConnectionParameters connectionParams;
	private LdapAuthenticationParameters authParams;

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

	@Override
	public boolean LdapAuthenticate(String bindDN, String password) {

		connectionParams = new LdapConnectionParameters();
		connectionParams.setHostname("localhost");
		connectionParams.setPort(1389);
		connectionParams.setConnectionTimeout(5);

		authParams = new LdapAuthenticationParameters();
		authParams.setBindDn(bindDN);
		authParams.setPassword(password);

		try {
			if (checkNetworkParameters(connectionParams)) {
				contextSource = new LdapContextSource();
				contextSource.setUrl("ldap://" + connectionParams.getHostname() + ":" + connectionParams.getPort());
				contextSource.setUserDn(authParams.getBindDn());
				contextSource.setPassword(authParams.getPassword());
				contextSource.afterPropertiesSet();
				LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
				ldapTemplate.getContextSource().getContext(authParams.getBindDn(), authParams.getPassword());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	@Override
	public Map<String, List<String>> getBaseCNs(String bindDN, String password) {
		
        // store the user and their mails only
        Map<String, List<String>> tResult = new HashMap<>();
        List<String> mails = new ArrayList<>();
        LdapContext ctx;
        
		if (LdapAuthenticate(bindDN, password)) {
	        try {
	        	ctx = (LdapContext) contextSource.getContext(authParams.getBindDn(), authParams.getPassword());
	        	
	        	// Define search controls
	            SearchControls searchControls = new SearchControls();
	            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Search the entire subtree

	            // Perform the search cxt.serarch(baseDN, ... )
	            javax.naming.NamingEnumeration<SearchResult> namingEnum = ctx.search("dc=example,dc=org", "(objectClass=*)", searchControls);
	            while (namingEnum.hasMore()) {
	                SearchResult result = namingEnum.next();
	                
	                javax.naming.directory.Attributes attrs = result.getAttributes();
	                
	                for (javax.naming.NamingEnumeration<?> ae = attrs.getAll(); ae.hasMore();) {
	                    javax.naming.directory.Attribute attr = (javax.naming.directory.Attribute) ae.next();
	                    
	                    for (javax.naming.NamingEnumeration<?> vals = attr.getAll(); vals.hasMore();) {
	                    	String value = vals.next().toString();
	                    	
	                        if (attr.getID().compareTo("cn") == 0) {
	                        	tResult.put(value, mails);
	                        	mails = new ArrayList<String>();
	                        }
	                    	
	                    	if (attr.getID().compareTo("mail") == 0) {
	                        	mails.add(value);
	                        }
	                    	
	                    }
	                }
	            }
	            
	            namingEnum.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return tResult;
		}
		return null;
	}

}
