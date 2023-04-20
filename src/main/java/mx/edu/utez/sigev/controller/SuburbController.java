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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE')) and (hasRole('ROL_ADMINISTRADOR'))")
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

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE')) and (hasRole('ROL_ADMINISTRADOR'))")
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

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE')) and (hasRole('ROL_ADMINISTRADOR'))")
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

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE')) and (hasRole('ROL_ADMINISTRADOR'))")
    public String edit(Model model, RedirectAttributes redirectAttributes, Suburb suburb, @PathVariable("id") long id,
            Authentication authentication, HttpSession session) {

        Suburb tmp = suburbService.findOne(id);
        if (!tmp.equals(null)) {
            Users user = userService.findByUsername(authentication.getName());
            user.setPassword(null);
            session.setAttribute("user", user);
            model.addAttribute("userLog", user);
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            model.addAttribute("image", image);
            model.addAttribute("color", color);
            model.addAttribute("listCities",
                    cityService.findAllCitiesByStateId(linkService.findOne(user.getId()).getCity().getId()));
            model.addAttribute("suburb", tmp);
            return "suburb/edit";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "La colonia seleccionada no existe");
        }
        return "redirect:/suburb/list";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE')) and (hasRole('ROL_ADMINISTRADOR'))")
    public String update(Model model, RedirectAttributes redirectAttributes, Suburb suburb,
            @PathVariable("id") long id) {
        if (!(BlacklistController.checkBlacklistedWords(suburb.getName())
                || BlacklistController.checkBlacklistedWords(suburb.getPostalCode()))) {
            Suburb tmp = suburbService.findOne(id);
            if (!tmp.equals(null)) {
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
