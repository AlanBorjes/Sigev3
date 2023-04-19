package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.*;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.*;
import mx.edu.utez.sigev.util.DocumentoUtileria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/list")
    public String list(Model model, Pageable pageable, Authentication authentication) {
        Users user = userService.findByUsername(authentication.getName());
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        List<City> listCities = cityService.findAll();
        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        model.addAttribute("listCities", listCities);
        return "city/municipios";
    }

    @GetMapping(value = "/create")
    public String create(Model model, RedirectAttributes redirectAttributes, City city, Authentication authentication) {
        Users user = userService.findByUsername(authentication.getName());
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        model.addAttribute("listStates", stateService.findAll());
        return "city/create";
    }

    @PostMapping(value = "/save")
    public String save(Model model, RedirectAttributes redirectAttributes,
                       Authentication authentication, @Valid City city, BindingResult result, @RequestParam("picture") MultipartFile file) throws IOException {
        Users user = userService.findByUsername(authentication.getName());
        State state = stateService.findById(1);
        city.setStatus(1);
        city.setState(state);
        System.out.println(city.getId());
        System.out.println(city.getName());
        System.out.println(city.getStatus());
        System.out.println(city.getState());

        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        model.addAttribute("userLog", user);
        if (result.hasErrors()){
            System.out.println("Entre");
            redirectAttributes.addFlashAttribute("msg_error", "UPS");
            return "city/create";
        }else{
            System.out.println("Entre2");
            if (!BlacklistController.checkBlacklistedWords(city.getName())) {
                if (!file.isEmpty()) {
                    System.out.println("entre aca");
                    String ruta = "C:/sigev/docs";
                    String nombre = DocumentoUtileria.guardarDocumento(file, ruta);
                    if (nombre != null) {
                        city.setShield(nombre);
                    }
                }

                boolean res = cityService.save(city);
                if (res) {
                    redirectAttributes.addAttribute("msg_success", "Municipio registrado correctamente");
                    return "redirect:/city/list";
                } else {
                    redirectAttributes.addAttribute("msg_error", "Ocurrió un problema al guardar el Municipio");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        }

        return "redirect:/city/create";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(Model model, RedirectAttributes redirectAttributes, Authentication authentication, City category, @PathVariable("id") long id) {
        Users user = userService.findByUsername(authentication.getName());
        City tmp = cityService.findOne(id);
        if (tmp!=null) {
            Images image = imagesService.findImages(1);
            Color color = colorService.findColors(1);
            model.addAttribute("image", image);
            model.addAttribute("color", color);
            model.addAttribute("listStates", stateService.findAll());
            model.addAttribute("city", tmp);
            model.addAttribute("userLog", user);
            return "city/edit";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "El municipio seleccionado no existe");
        }
        return "redirect:/city/list";
    }

    @PostMapping(value = "/update/{id}")
    public String update(Model model, RedirectAttributes redirectAttributes, City city, @PathVariable("id") long id) {
        if (!BlacklistController.checkBlacklistedWords(city.getName())) {
            City tmp = cityService.findOne(id);
            if (tmp!=null) {
                tmp.setName(city.getName());
                boolean res = cityService.save(tmp);
                if (res) {
                    model.addAttribute("msg_success", "Municipio actualizado correctamente");
                    return "redirect:/city/list";
                } else {
                    model.addAttribute("msg_error", "Ocurrió un error al actualizar el Municipio");
                }
            } else {
                model.addAttribute("msg_error", "El Municipio seleccionado no existe");
            }
        } else {
            model.addAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }
        return ("redirect:/city/edit/" + id);
    }

    @GetMapping(value = "/desactivate/{id}")
    public String categoryDesactivate(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") long id,
                                      City city) {
        String msg = "";
        City tmp = cityService.findOne(id);
        if (tmp!=null) {
            if (!BlacklistController.checkBlacklistedWords(tmp.getName())) {
                if (tmp.getStatus() == 1){
                    tmp.setStatus(0);
                    msg = "Se inhabilitó el municipio";
                }else{
                    tmp.setStatus(1);
                    msg = "Se habilitó el municipio";
                }
                boolean res = cityService.save(tmp);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success", msg);
                    return "redirect:/city/list";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error",
                            "Ocurrió un error al actualizar el municipio");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "El municipio solicitado no existe.");
        }
        return "redirect:/city/edit/" + id;
    }

}
