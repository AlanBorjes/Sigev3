package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.Images;
import mx.edu.utez.sigev.entity.Suburb;
import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/suburb")
public class SuburbController {

    @Autowired
    private SuburbService suburbService;

    @Autowired
    private CityService cityService;

    @Autowired
    private UserService userService;

    @Autowired
    private CityLinkService linkService;
    @Autowired
    private ImagesService imagesService;

    @Autowired
    private ColorService colorService;

    @GetMapping(value = "/list")
    public String list(Model model, RedirectAttributes redirectAttributes, Pageable pageable,Authentication authentication) {
        Users user = userService.findByUsername(authentication.getName());
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        Page<Suburb> listSuburbs = suburbService
                .listPagination(PageRequest.of(pageable.getPageNumber(), 10, Sort.by("id").ascending()));
        model.addAttribute("listSuburbs", listSuburbs);
        return "suburb/list";
    }

    @GetMapping(value = "/create")
    public String create(Model model, RedirectAttributes redirectAttributes, Suburb suburb,
            Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        user.setPassword(null);
        session.setAttribute("user", user);
        model.addAttribute("listCities",
                cityService.findAllByStatus(1));
        return "suburb/create";
    }

    @PostMapping(value = "/save")
    public String save(Model model, RedirectAttributes redirectAttributes, Suburb suburb, Authentication authentication,
            HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        user.setPassword(userService.findPasswordById(user.getId()));
        if (!(BlacklistController.checkBlacklistedWords(suburb.getName())
                || BlacklistController.checkBlacklistedWords(suburb.getPostalCode()))) {
            suburb.setCity(linkService.findByUserId(user.getId()).getCity());
            boolean res = suburbService.save(suburb);
            if (res) {
                model.addAttribute("msg_success", "Se registró la colonia correctamente");
                return "redirect:/suburb/list";
            } else {
                model.addAttribute("msg_error", "Ocurrió un error al registrar la colonia");
            }
        } else {
            model.addAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }
        return "redirect:/suburb/create";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(Model model, RedirectAttributes redirectAttributes, Suburb suburb, @PathVariable("id") long id,
            Authentication authentication, HttpSession session) {

        Suburb tmp = suburbService.findOne(id);
        if (tmp!=null) {
            Users user = userService.findByUsername(authentication.getName());
            user.setPassword(null);
            session.setAttribute("user", user);
            model.addAttribute("userLog", user);
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            model.addAttribute("image", image);
            model.addAttribute("color", color);
            model.addAttribute("listCities",
                    cityService.findAllCitiesByStateId(linkService.findOne(user.getId()).getCity().getState().getId()));
            model.addAttribute("suburb", tmp);
            return "suburb/edit";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "La colonia seleccionada no existe");
        }
        return "redirect:/suburb/list";
    }

    @PostMapping(value = "/update/{id}")
    public String update(Model model, RedirectAttributes redirectAttributes, Suburb suburb,
            @PathVariable("id") long id) {
        if (!(BlacklistController.checkBlacklistedWords(suburb.getName())
                || BlacklistController.checkBlacklistedWords(suburb.getPostalCode()))) {
            Suburb tmp = suburbService.findOne(id);
            if (tmp!=null) {
                suburb.setId(id);
                boolean res = suburbService.save(suburb);
                if (res) {
                    model.addAttribute("msg_success", "Colonia actualizada correctamente");
                    return "redirect:/suburb/list";
                } else {
                    model.addAttribute("msg_error", "Ocurrió un error al actualizar la Colonia");
                }
            } else {
                model.addAttribute("msg_error", "La Colonia seleccionada no existe");
            }
        } else {
            model.addAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }
        return ("redirect:/suburb/edit/" + id);
    }

}
