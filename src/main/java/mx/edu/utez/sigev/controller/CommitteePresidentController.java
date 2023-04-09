package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.CommitteePresident;
import mx.edu.utez.sigev.entity.DataTransferObject.PresidentDto;
import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/committee-president")
public class CommitteePresidentController {

    @Autowired
    private CommitteePresidentService presidentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private CityLinkService linkService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, RedirectAttributes redirectAttributes, Authentication authentication,
            HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        model.addAttribute("listUsers", presidentService.listPagination(linkService.findByUserId(user.getId()).getCity().getId()));
        return "committee_president/list";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String list(Model model, RedirectAttributes redirectAttributes, PresidentDto presidentDto) {
        model.addAttribute("committeeList", committeeService.findAll());
        return "committee_president/create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(PresidentDto presidentDto, Model model, RedirectAttributes redirectAttributes) {
        if (!(BlacklistController.checkBlacklistedWords(presidentDto.getName())
                || BlacklistController.checkBlacklistedWords(presidentDto.getLastname())
                || BlacklistController.checkBlacklistedWords(presidentDto.getUsername())
                || BlacklistController.checkBlacklistedWords(presidentDto.getPhone())
                || BlacklistController.checkBlacklistedWords(presidentDto.getPassword()))) {
            if (!presidentService.hasPresident(presidentDto.getCommittee())) {
                Users obj = new Users();
                obj.setName(presidentDto.getName());
                obj.setLastname(presidentDto.getLastname());
                obj.setSurname(presidentDto.getSurname());
                obj.setUsername(presidentDto.getUsername());
                obj.setPhone(presidentDto.getPhone());
                obj.setEmail(presidentDto.getEmail());
                obj.setPassword(passwordEncoder.encode(presidentDto.getPassword()));
                obj.addRole(rolesService.findByAuthority("ROL_PRESIDENTE"));

                boolean res = userService.save(obj);

                CommitteePresident tmpPresident = new CommitteePresident();
                tmpPresident.setCommittee(committeeService.findById(presidentDto.getCommittee()));
                tmpPresident.setUser(obj);
                boolean res2 = presidentService.save(tmpPresident);
                if (res && res2) {
                    redirectAttributes.addFlashAttribute("msg_success",
                            "Presidente registrado correctamente, Ahora puede iniciar sesión con este usuario");
                    return "redirect:/committee-president/list";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un error al registrar al Presidente");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "El comité ya tiene un presidente asignado");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }
        return "redirect:/committee-president/list";
    }

}
