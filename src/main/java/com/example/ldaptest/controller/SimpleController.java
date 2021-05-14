package com.example.ldaptest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import java.util.List;

@RestController
public class SimpleController {

    @Autowired
    LdapTemplate ldapTemplate;


    @GetMapping("/test")
    public String homePage(Model model) {


        String username = "tales.teste";
        String domain = "1gint";
        String password = "senha123!";

        final List<String> cn = ldapTemplate.search(
                "OU=Delegados,DC=" + domain + ",DC=tj,DC=rs",
                "(&(sAMAccountName=" + username + ")(objectclass=user))",
                (AttributesMapper<String>) attrs -> (String) attrs.get("sAMAccountName").get());

        final Attribute search = search(domain, username);

        try {
            updatePassword(String.valueOf(search.get()), password);
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }

        return cn.toString();
    }

    private Attribute search(String domain, String username) {
        Attribute searchResultList = ldapTemplate.search(
                "OU=Delegados,DC=" + domain + ",DC=tj,DC=rs",
                "(&(sAMAccountName=" + username + ")(objectclass=user))",
                SearchControls.SUBTREE_SCOPE,
                (AttributesMapper<Attribute>) attributes -> attributes.get("distinguishedName")).get(0);

        return searchResultList;

    }


    private void updatePassword(String domain, String password) {

        System.out.println("updating password...\n");

        String quotedPassword = "\"" + password + "\"";
        char unicodePwd[] = quotedPassword.toCharArray();
        byte pwdArray[] = new byte[unicodePwd.length * 2];
        for (int i = 0; i < unicodePwd.length; i++) {
            pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
            pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
        }

        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", pwdArray));
        ldapTemplate.modifyAttributes(domain, mods);

    }


}