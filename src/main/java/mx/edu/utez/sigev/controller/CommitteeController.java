package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.Committee;
import mx.edu.utez.sigev.entity.Images;
import mx.edu.utez.sigev.entity.Users;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE'))")
@RequestMapping(value = "/committee")
public class CommitteeController {
    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SuburbService suburbService;

    @Autowired
    private StateService stateService;

    @Autowired
    private UserService userService;

    @Autowired
    private CityLinkService linkService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private ImagesService imagesService;



    @GetMapping(value = "/list")
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE'))")
    public String findAll(Authentication authentication, HttpSession session, Model model, Pageable pageable) {
        Users user = userService.findByUsername(authentication.getName());
        if (session.getAttribute("user") == null) {
            user.setPassword(null);
            session.setAttribute("user", user);
        }

        Page<Committee> listCommittees = committeeService.listarPaginacion(PageRequest.of(pageable.getPageNumber(), 4, Sort.by("id").ascending()));
        model.addAttribute("listCommittees", listCommittees);
        System.out.println(listCommittees);
        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-");

        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("color", color);
        model.addAttribute("image", image);
        return "committee/listAllCommittees";
    }



    @GetMapping("/create")
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE'))")
    public String createCommittee(Committee committee, Model model, Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        //model.addAttribute("listCities", cityService.findAllCitiesByStateId(linkService.findByUserId(user.getId()).getCity().getState().getId()));
        model.addAttribute("listSuburbs", suburbService.findAll());
        model.addAttribute("listStates", stateService.findAll());
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("color", color);
        model.addAttribute("image", image);
        return "committee/create";
    }


    @GetMapping(value = "/find/{id}")
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE'))")
    public String findOne(Authentication authentication, HttpSession session, Model model, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {

        Users user = userService.findByUsername(authentication.getName());
        if (session.getAttribute("user") == null) {
            user.setPassword(null);
            session.setAttribute("user", user);
        }
        Committee committee = committeeService.findById(id);
        if (!committee.equals(null)) {
            model.addAttribute("committee", committee);
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            model.addAttribute("color", color);
            model.addAttribute("image", image);
            return "commitee/listCommittee";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró el comité solicitado");
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            model.addAttribute("userLog", user);
            model.addAttribute("color", color);
            model.addAttribute("image", image);
            return "committee/listAllCommittees";
        }
    }
    @PostMapping(value = "/save")
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE'))")
    public String save(Authentication authentication, HttpSession session, Model model, Committee committee, RedirectAttributes redirectAttributes) {
        String msgOk = "";
        String msgError = "";

        if(committee.getId() != null){
            msgOk = "Comité Actualizada correctamente";
            msgError = "El comité NO pudo ser Actualizado correctamente";
        }else{
            msgOk = "Comité Guardado correctamente";
            msgError = "El comité NO pudo ser guardado correctamente";
        }

        boolean res = committeeService.save(committee);
        if (res) {
            redirectAttributes.addFlashAttribute("msg_success", msgOk);
            return "redirect:/committee/list";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", msgError);
            return "redirect:/committee/create";
        }
    }


    @GetMapping(value = "/update/{id}")
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE'))")
    public String update(Authentication authentication, HttpSession session, @PathVariable long id, Model modelo, RedirectAttributes redirectAttributes) {
        Users user = userService.findByUsername(authentication.getName());
        if (session.getAttribute("user") == null) {
            user.setPassword(null);
            session.setAttribute("user", user);
        }
        Committee committee = committeeService.findById(id);
        if (committee != null) {
            modelo.addAttribute("committee", committee);
            modelo.addAttribute("listSuburbs", suburbService.findAll());
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            modelo.addAttribute("userLog", user);
            modelo.addAttribute("color", color);
            modelo.addAttribute("image", image);
            return "committee/create";
        }else{
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            modelo.addAttribute("userLog", user);
            modelo.addAttribute("color", color);
            modelo.addAttribute("image", image);
            return "committee/listAllCommittees";
        }
    }

    @RequestMapping (value = "/details/{id}", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE'))")
    public String detalles(Authentication authentication, HttpSession session, Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        Users user = userService.findByUsername(authentication.getName());
        if (session.getAttribute("user") == null) {
            user.setPassword(null);
            session.setAttribute("user", user);
        }
        Committee committee = committeeService.findById(id);
        if (!committee.equals(null)){
            model.addAttribute("committee", committeeService.findById(id));
            model.addAttribute("listCommittees", committeeService.findAll());
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            model.addAttribute("userLog", user);
            model.addAttribute("color", color);
            model.addAttribute("image", image);
            return "committee/listCommittee";
        }else {
            redirectAttributes.addFlashAttribute("msg_error", "Registro No Encontrado");
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            model.addAttribute("color", color);
            model.addAttribute("image", image);
            return "redirect:/committee/listAllCommittees";
        }


    }


    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("isAuthenticated() and (hasRole('ROL_ENLACE'))")
    public String delete(Authentication authentication, HttpSession session,Model model, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Users user = userService.findByUsername(authentication.getName());
        if (session.getAttribute("user") == null) {
            user.setPassword(null);
            session.setAttribute("user", user);
        }

        Committee committee = committeeService.findById(id);
        if (!committee.equals(null)) {
            boolean res = committeeService.delete(id);
            if (res) {
                redirectAttributes.addFlashAttribute("msg_success", "Comité eliminado correctamente");
                Color color = colorService.findColors(1);
                Images image = imagesService.findImages(1);
                model.addAttribute("userLog", user);
                model.addAttribute("color", color);
                model.addAttribute("image", image);
                return "committe/listAllCommittees";
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "No se pudo eliminar el comité");
                Color color = colorService.findColors(1);
                Images image = imagesService.findImages(1);
                model.addAttribute("userLog", user);
                model.addAttribute("color", color);
                model.addAttribute("image", image);
                return "committe/listAllCommittees";
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró el comité solicitado");
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            model.addAttribute("userLog", user);
            model.addAttribute("color", color);
            model.addAttribute("image", image);
            return "committe/listAllCommittees";
        }
    }

}