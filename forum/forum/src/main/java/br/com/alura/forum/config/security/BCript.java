package br.com.alura.forum.config.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCript {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("13101995"));
    }


}
