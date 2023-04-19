package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.Committee;
import mx.edu.utez.sigev.entity.Users;
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

    @GetMapping(value = "/list")
    public String findAll(Model model, Pageable pageable) {
        Page<Committee> listCommittees = committeeService.listarPaginacion(PageRequest.of(pageable.getPageNumber(), 4, Sort.by("id").ascending()));
        model.addAttribute("listCommittees", listCommittees);
        return "committee/listAllCommittees";
    }



    @GetMapping("/create")
    public String createCommittee(Committee committee, Model model, Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        //model.addAttribute("listCities", cityService.findAllCitiesByStateId(linkService.findByUserId(user.getId()).getCity().getState().getId()));
        model.addAttribute("listSuburbs", suburbService.findAll());
        model.addAttribute("listStates", stateService.findAll());
        return "committee/create";
    }


    @GetMapping(value = "/find/{id}")
    public String findOne(Model model, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Committee committee = committeeService.findById(id);
        if (!committee.equals(null)) {
            model.addAttribute("committee", committee);
            return "commitee/listCommittee";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró el comité solicitado");
            return "committee/listAllCommittees";
        }
    }
    @PostMapping(value = "/save")
    public String save(Model model, Committee committee, RedirectAttributes redirectAttributes) {
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
    public String update(@PathVariable long id, Model modelo, RedirectAttributes redirectAttributes) {
        Committee committee = committeeService.findById(id);
        if (committee != null) {
            modelo.addAttribute("committee", committee);
            modelo.addAttribute("listSuburbs", suburbService.findAll());
            return "committee/create";
        }else{
            return "committee/listAllCommittees";
        }
    }

    @GetMapping (value = "/details/{id}")
    public String detalles(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        Committee committee = committeeService.findById(id);
        if (!committee.equals(null)){
            model.addAttribute("committee", committeeService.findById(id));
            model.addAttribute("listCommittees", committeeService.findAll());
            return "committee/listCommittee";
        }else {
            redirectAttributes.addFlashAttribute("msg_error", "Registro No Encontrado");
            return "redirect:/committee/listAllCommittees";
        }


    }


    @DeleteMapping(value = "/delete/{id}")
    public String delete(Model model, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Committee committee = committeeService.findById(id);
        if (!committee.equals(null)) {
            boolean res = committeeService.delete(id);
            if (res) {
                redirectAttributes.addFlashAttribute("msg_success", "Comité eliminado correctamente");
                return "committe/listAllCommittees";
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "No se pudo eliminar el comité");
                return "committe/listAllCommittees";
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró el comité solicitado");
            return "committe/listAllCommittees";
        }
    }

}