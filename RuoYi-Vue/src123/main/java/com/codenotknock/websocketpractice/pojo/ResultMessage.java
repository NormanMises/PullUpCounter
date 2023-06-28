package com.codenotknock.websocketpractice.pojo;

import lombok.Data;

@Data
public class ResultMessage <T>{
    private Boolean system;
    private T message;
    private String formName;

}
