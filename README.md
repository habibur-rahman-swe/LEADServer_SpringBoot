Docker compose
```
 openldap:
    image: bitnami/openldap:2.6.8
    ports:
      - '1389:1389'
      - '1636:1636'
    environment:
      - LDAP_ADMIN_USERNAME=admin
      - LDAP_ADMIN_PASSWORD=admin123
      - LDAP_USERS=user1,user2
      - LDAP_PASSWORDS=password1,password2
    volumes:
      - './openldap_data:/bitnami/openldap'
```

dn: `dc=example,dc=org`


bindDn: `"cn=admin,dc=example,dc=org"`
