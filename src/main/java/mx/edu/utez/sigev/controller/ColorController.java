package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.service.CityService;
import mx.edu.utez.sigev.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/color")
public class ColorController {

    @Autowired
    private ColorService colorService;


}
