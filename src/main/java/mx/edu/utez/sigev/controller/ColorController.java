package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.repository.IColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ColorController {

    @Autowired
    private IColorRepository coloresRepository;

    @RequestMapping(value = "/colores", method = RequestMethod.GET)
    public Color getColores() {
        List<Color> colores = coloresRepository.findAll();
        return colores.get(0); // enviar solo el primer resultado de la consulta
    }
}
