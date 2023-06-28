package com.codenotknock.websocketpractice.util;

import com.alibaba.fastjson.JSON;
import com.codenotknock.websocketpractice.pojo.ResultMessage;

public class MessageUtils {

    public static String getMessage(boolean isSystemMessage, String formName, Object message){
        ResultMessage result = new ResultMessage();
        result.setMessage(message);
        result.setSystem(isSystemMessage);
        if (formName != null){
            result.setFormName(formName);
        }
        return JSON.toJSONString(result);
    }
}
