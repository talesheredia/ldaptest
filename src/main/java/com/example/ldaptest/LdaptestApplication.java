package com.example.ldaptest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

@SpringBootApplication
public class LdaptestApplication {


    public static void main(String[] args) {
        SpringApplication.run(LdaptestApplication.class, args);
    }

}
