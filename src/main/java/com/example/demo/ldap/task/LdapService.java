package com.example.demo.ldap.task;

import java.util.List;
import java.util.Map;

public interface LdapService {

	public boolean LdapAuthenticate(String bindDN, String password);

	public Map<String, List<String>> getBaseCNs(String bindDN, String password);
}
