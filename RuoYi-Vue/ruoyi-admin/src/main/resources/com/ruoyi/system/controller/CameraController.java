package com.ruoyi.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CameraController {
    @RequestMapping("/Camera/pullCount")
    public ModelAndView home() {

        return new ModelAndView("/index");
    }
}