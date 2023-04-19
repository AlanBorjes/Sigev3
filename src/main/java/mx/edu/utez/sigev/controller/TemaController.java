package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.CityLink;
import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.DataTransferObject.RecoverPasswordDto;
import mx.edu.utez.sigev.entity.DataTransferObject.UserUpdateDTO;
import mx.edu.utez.sigev.entity.Images;
import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.ColorService;
import mx.edu.utez.sigev.service.ImagesService;
import mx.edu.utez.sigev.service.UserService;
import mx.edu.utez.sigev.util.DocumentoUtileria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    ImagesService imagesService;


    @GetMapping(value = "/")
    public String findAllEnlaces(Model model, Authentication authentication, HttpSession session) {
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        model.addAttribute("userLog", user);
        model.addAttribute("color", color);
        model.addAttribute("image", image);
        return "administrador/tema";
    }

    @PostMapping(value = "/update/color")
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

    @PostMapping(value = "/update/logo1")
    public String updateLogo1(Model model,RedirectAttributes redirectAttributes, Authentication authentication,
                              HttpSession session, @RequestParam("logo1")  MultipartFile file) throws IOException{
        Images tmp = imagesService.findImages(1);

        if (!file.isEmpty()) {
            String ruta = "C:/sigev/docs";
            String nombre = DocumentoUtileria.guardarDocumento(file, ruta);
            if (nombre != null) {
                tmp.setLogo1(nombre);
            }
        }

        Users user = userService.findByUsername(authentication.getName());
        session.setAttribute("user", user);

        boolean res;
        res = imagesService.save(tmp);

        if (res) {
            redirectAttributes.addFlashAttribute("msg_success", "Se modificó el logo correctamente");
            return "redirect:/tema/";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar el logo");
        }

        return ("redirect:/tema/");
    }

    @PostMapping(value = "/update/logo2")
    public String updateLogo2(Model model,RedirectAttributes redirectAttributes, Authentication authentication,
                              HttpSession session, @RequestParam("logo2")  MultipartFile file) throws IOException{
        Images tmp = imagesService.findImages(1);

        if (!file.isEmpty()) {
            String ruta = "C:/sigev/docs";
            String nombre = DocumentoUtileria.guardarDocumento(file, ruta);
            if (nombre != null) {
                tmp.setLogo2(nombre);
            }
        }

        Users user = userService.findByUsername(authentication.getName());
        session.setAttribute("user", user);

        boolean res;
        res = imagesService.save(tmp);

        if (res) {
            redirectAttributes.addFlashAttribute("msg_success", "Se modificó el logo correctamente");
            return "redirect:/tema/";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar el logo");
        }

        return ("redirect:/tema/");
    }

    @PostMapping(value = "/update/imgLogin")
    public String updateimgLogin(Model model,RedirectAttributes redirectAttributes, Authentication authentication,
                              HttpSession session, @RequestParam("imgLogin")  MultipartFile file) throws IOException{
        Images tmp = imagesService.findImages(1);

        if (!file.isEmpty()) {
            String ruta = "C:/sigev/docs";
            String nombre = DocumentoUtileria.guardarDocumento(file, ruta);
            if (nombre != null) {
                tmp.setLogin(nombre);
            }
        }

        Users user = userService.findByUsername(authentication.getName());
        session.setAttribute("user", user);

        boolean res;
        res = imagesService.save(tmp);

        if (res) {
            redirectAttributes.addFlashAttribute("msg_success", "Se modificó la imagen correctamente");
            return "redirect:/tema/";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar la imagen");
        }

        return ("redirect:/tema/");
    }
}
