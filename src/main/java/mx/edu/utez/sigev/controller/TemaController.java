package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.CityLink;
import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.DataTransferObject.RecoverPasswordDto;
import mx.edu.utez.sigev.entity.DataTransferObject.UserUpdateDTO;
import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.ColorService;
import mx.edu.utez.sigev.service.UserService;
import mx.edu.utez.sigev.util.DocumentoUtileria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/tema")
public class TemaController {

    @Autowired
    ColorService colorService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String findAllEnlaces(Model model, Authentication authentication, HttpSession session) {
        Color color = colorService.findColors(1);
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        model.addAttribute("color", color);
        return "administrador/tema";
    }

    @RequestMapping(value = "/update/color", method = RequestMethod.POST)
    public String updateColor(Model model,RedirectAttributes redirectAttributes, Authentication authentication,
                              HttpSession session, Color color) {
        Color tmp = colorService.findColors(1);
        tmp.setColorMain(color.getColorMain());
        tmp.setColorSecundary(color.getColorSecundary());
        tmp.setColorText(color.getColorText());
        tmp.setColorTitle(color.getColorTitle());
        Users user = userService.findByUsername(authentication.getName());
        session.setAttribute("user", user);

        boolean res;

        res = colorService.save(tmp);

        if (res) {
            redirectAttributes.addFlashAttribute("msg_success", "Se modificaron los colores correctamente");
            return "redirect:/tema/";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar los colores");
        }

        return ("redirect:/tema/");
    }
}
