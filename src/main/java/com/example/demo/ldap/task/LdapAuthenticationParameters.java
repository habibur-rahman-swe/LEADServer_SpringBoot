package com.example.demo.ldap.task;

public class LdapAuthenticationParameters {
	private String bindDn;
    private String password;
	public String getBindDn() {
		return bindDn;
	}
	public void setBindDn(String bindDn) {
		this.bindDn = bindDn;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
