package com.codenotknock.websocketpractice.controller;

import com.codenotknock.websocketpractice.pojo.Result;
import com.codenotknock.websocketpractice.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @PostMapping("user/login")
    public Result login(@RequestBody User user, HttpSession session){
        Result result  = new Result();
        if (user != null && "123".equals(user.getPassword())){
            result.setFlag(true);
            session.setAttribute("user", user.getUsername());
        } else {
            result.setFlag(false);
            result.setMessage("登录失败！");
        }
        return result;
    }

    @GetMapping("/getUsername")
    public String getUserName(HttpSession session){
        String username = (String) session.getAttribute("user");
        return username;
    }

}
