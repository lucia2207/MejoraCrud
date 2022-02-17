package com.example.MejoraCrud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiRestController {

    @GetMapping(value = "/hola")
    public String hola(){
        return "Hola Mundo";
    }
}
