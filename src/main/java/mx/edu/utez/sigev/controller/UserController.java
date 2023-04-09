package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.CityLink;
import mx.edu.utez.sigev.entity.DataTransferObject.RecoverPasswordDto;
import mx.edu.utez.sigev.entity.DataTransferObject.UserDto;
import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.CityLinkService;
import mx.edu.utez.sigev.service.CityService;
import mx.edu.utez.sigev.service.RolesService;
import mx.edu.utez.sigev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CityLinkService linkService;

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String findAll(Model model, Pageable pageable, Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        Page<Users> listUsers = userService
                .listPagination(PageRequest.of(pageable.getPageNumber(), 10, Sort.by("id").ascending()));
        model.addAttribute("listUsers", listUsers);
        return "users/list";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String findOne(Model model, @PathVariable("id") long id, RedirectAttributes redirectAttributes,
            RecoverPasswordDto recoverPasswordDto) {
        Users user = userService.findById(id);
        if (!user.equals(null)) {
            model.addAttribute("user", user);
            return "/users/edit";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró el usuario solicitado");
        }
        return "redirect:/users/list";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(UserDto userDto, Model modelo) {
        modelo.addAttribute("listCities", cityService.findAll());
        return "users/create";
    }

    @RequestMapping(value = "/recover/{id}", method = RequestMethod.POST)
    public String recoverPassword(RecoverPasswordDto recoverPasswordDto, Model model,
            RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            Authentication authentication, HttpSession session) {
        if (!BlacklistController.checkBlacklistedWords(recoverPasswordDto.getPassword())) {
            boolean res;
            Users user = userService.findByUsername(authentication.getName());
            user.setPassword(null);
            session.setAttribute("user", user);
            user.setPassword(userService.findPasswordById(id));
            Users tmpUser = userService.findById(id);
            tmpUser.setPassword(passwordEncoder.encode(recoverPasswordDto.getPassword()));
            if(user.getUsername().equals(tmpUser.getUsername())) {
                res = userService.save(tmpUser);
                redirectAttributes.addFlashAttribute("msg_success", "Se modificó tu contraseña correctamente, vuelve a iniciar sesión");
                if (res) {
                    return "redirect:/logout";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar la contraseña");
                }
            } else {
                res = userService.save(tmpUser);
            }
            if (res) {
                redirectAttributes.addFlashAttribute("msg_success", "Se modificó la contraseña correctamente");
                return "redirect:/users/list";
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar la contraseña");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }
        return ("redirect:/users/edit/" + id);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(UserDto userDto, Model model, RedirectAttributes redirectAttributes) {
        if (!(BlacklistController.checkBlacklistedWords(userDto.getName())
                || BlacklistController.checkBlacklistedWords(userDto.getLastname())
                || BlacklistController.checkBlacklistedWords(userDto.getUsername())
                || BlacklistController.checkBlacklistedWords(userDto.getPhone())
                || BlacklistController.checkBlacklistedWords(userDto.getPassword()))) {
            if (!linkService.hasCityLink(userDto.getCity())) {
                Users obj = new Users();
                obj.setName(userDto.getName());
                obj.setLastname(userDto.getLastname());
                obj.setSurname(userDto.getSurname());
                obj.setUsername(userDto.getUsername());
                obj.setPhone(userDto.getPhone());
                obj.setEmail(userDto.getEmail());
                obj.setPassword(passwordEncoder.encode(userDto.getPassword()));
                obj.addRole(rolesService.findByAuthority("ROL_ENLACE"));

                boolean res = userService.save(obj);

                CityLink tmpLink = new CityLink();
                tmpLink.setCity(cityService.findOne(userDto.getCity()));
                tmpLink.setUser(obj);
                boolean res2 = linkService.save(tmpLink);
                if (res && res2) {
                    redirectAttributes.addFlashAttribute("msg_success",
                            "Enlace registrado correctamente, Ahora puede iniciar sesión con este usuario");
                    return "redirect:/users/list";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un error al registrar al Enlace");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "El municipio ya tiene un enlace asignado");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }

        return "redirect:/users/create";
    }

    @RequestMapping(value = "/disable/{id}", method = RequestMethod.GET)
    public String disableUser(@PathVariable("id") long id, RedirectAttributes redirectAttributes,
            Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        user.setPassword(userService.findPasswordById(user.getId()));
        Users tmp = userService.findById(id);
        tmp.setPassword(userService.findPasswordById(id));
        if (user.getUsername().equals(tmp.getUsername())) {
            redirectAttributes.addFlashAttribute("msg_error", "No puedes deshabilitarte");
            return "redirect:/users/list";
        } else {
            if (tmp.getEnabled() == 1) {
                tmp.setEnabled(0);
                redirectAttributes.addFlashAttribute("msg_success", "Usuario deshabilitado");
            } else {
                tmp.setEnabled(1);
                redirectAttributes.addFlashAttribute("msg_success", "Usuario habilitado");
            }
        }
        userService.save(tmp);
        return "redirect:/users/list";
    }

}
