package com.example.ldaptest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapName;
import java.util.List;

@RestController
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;

    @Autowired
    LdapTemplate ldapTemplate;

    DirContextAdapter dirContextAdapter;

    @GetMapping("/test")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);

        final List<String> cn = ldapTemplate.search(
                "ou=people",
                "ou=" + "Delivering Crew",
                (AttributesMapper<String>) attrs -> (String) attrs.get("description").get());

        System.out.println(cn);
        updateDescription();
        return cn.toString();
    }

    public void updateDescription() {
        Name dn = buildDn(null);
//        Attribute attr = new BasicAttribute("description", "descricao");
        Attribute attr = new BasicAttribute("userPassword", "descricao");
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(dn, new ModificationItem[] {item});
    }

    protected Name buildDn(Person p) {
        final LdapName build = LdapNameBuilder.newInstance("ou=people")
//                .add("c", p.getCountry())
//                .add("ou", "people")
//               .add("uid", "leela")
                .add("cn", "Turanga Leela")
                .build();
        return build;
    }

}