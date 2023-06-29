package com.codenotknock.websocketpractice.pojo;

import lombok.Data;

@Data
public class User {
    private Integer userId;
    private String username;

    private String password;
}
