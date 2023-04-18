package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.*;
import mx.edu.utez.sigev.entity.DataTransferObject.RecoverPasswordDto;
import mx.edu.utez.sigev.entity.DataTransferObject.UserDto;
import mx.edu.utez.sigev.entity.DataTransferObject.UserUpdateDTO;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.*;
import mx.edu.utez.sigev.util.DocumentoUtileria;
import mx.edu.utez.sigev.util.FileUtil;
import mx.edu.utez.sigev.util.ImagenUtileria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private  ColorService colorService;

    @RequestMapping(value = "/list/enlaces", method = RequestMethod.GET)
    public String findAllEnlaces(Model model, Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        user.setPassword(null);
        session.setAttribute("user", user);
        List<CityLink> listUsers = linkService.findAll();
        model.addAttribute("userLog", user);
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        return "enlace/enlaces";
    }

    @RequestMapping(value = "/list/administradores", method = RequestMethod.GET)
    public String findAllAdmins(Model model, Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        user.setPassword(null);
        session.setAttribute("user", user);
        List<Users> listUsers = userService.findAllByRole(1);
        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        model.addAttribute("listUsers", listUsers);
        return "administrador/administradores";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String findOne(Model model, @PathVariable("id") long id, RedirectAttributes redirectAttributes,
            RecoverPasswordDto recoverPasswordDto, Authentication authentication) {
        Users userTmp = userService.findByUsername(authentication.getName());
        UserUpdateDTO userDto = new UserUpdateDTO();
        Users user = userService.findById(id);
        userDto.setName(user.getName());
        userDto.setLastname(user.getLastname());
        userDto.setSurname(user.getSurname());
        userDto.setPhone(user.getPhone());
        if (!user.equals(null)) {
            Images image = imagesService.findImages(1);
            Color color = colorService.findColors(1);
            model.addAttribute("userLog", userTmp);
            model.addAttribute("image", image);
            model.addAttribute("color", color);
            model.addAttribute("user", user);
            model.addAttribute("userDto", userDto);
            model.addAttribute("user", user);
            return "/users/edit";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró el usuario solicitado");
        }
        return "redirect:/users/list";
    }

    @RequestMapping(value = "/edit/password/{id}", method = RequestMethod.GET)
    public String editPassword(Model model, @PathVariable("id") long id, RedirectAttributes redirectAttributes,
                          RecoverPasswordDto recoverPasswordDto, Authentication authentication) {
        Users userTmp = userService.findByUsername(authentication.getName());
        Users user = userService.findById(id);
        if (!user.equals(null)) {
            Images image = imagesService.findImages(1);
            Color color = colorService.findColors(1);
            model.addAttribute("image", image);
            model.addAttribute("color", color);
            model.addAttribute("user", user);
            model.addAttribute("userLog", userTmp);
            return "/users/editPassword";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró el usuario solicitado");
        }
        return "redirect:/users/list/enlaces";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(UserDto userDto,Users users, Model modelo, Authentication authentication) {
        Users userTmp = userService.findByUsername(authentication.getName());
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        modelo.addAttribute("userLog", userTmp);
        modelo.addAttribute("image", image);
        modelo.addAttribute("color", color);
        modelo.addAttribute("listCities", cityService.findAllByStatus(1));
        return "users/create";
    }

    @RequestMapping(value = "/create/admin", method = RequestMethod.GET)
    public String createAdmin(UserDto userDto, Users users, Model modelo, Authentication authentication) {
        Users userTmp = userService.findByUsername(authentication.getName());
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        modelo.addAttribute("userLog", userTmp);
        modelo.addAttribute("image", image);
        modelo.addAttribute("color", color);
        return "administrador/create";
    }

    @RequestMapping(value = "/recover/{id}", method = RequestMethod.POST)
    public String recoverPassword(@Valid RecoverPasswordDto recoverPasswordDto, BindingResult result, Model model,
            RedirectAttributes redirectAttributes, @PathVariable("id") long id, @RequestParam("confirmarContraseña") String confirmarContraseña,
            Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        if (!BlacklistController.checkBlacklistedWords(recoverPasswordDto.getPassword())) {
            Users tmpUser = userService.findById(id);
            if (!(recoverPasswordDto.getPassword().equals(confirmarContraseña))){
                Images image = imagesService.findImages(1);
                Color color = colorService.findColors(1);
                model.addAttribute("image", image);
                model.addAttribute("color", color);
                result.rejectValue("password", "error.password", "Las contraseñas no coinciden");
                model.addAttribute("user", tmpUser);
                model.addAttribute("userLog", user);
                return "users/editPassword";
            }
            boolean res;

            user.setPassword(null);
            session.setAttribute("user", user);
            user.setPassword(userService.findPasswordById(id));

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
                return "redirect:/users/list/enlaces";
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar la contraseña");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }
        return ("redirect:/users/edit/" + id);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String updateUser(Users users, @Valid UserUpdateDTO userDto, BindingResult results, RecoverPasswordDto recoverPasswordDto, Model model,
                             RedirectAttributes redirectAttributes, @PathVariable("id") long id,
                             Authentication authentication, HttpSession session, @RequestParam("picture")  MultipartFile file) throws IOException{
        Users user = userService.findByUsername(authentication.getName());
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        boolean res;

        if (!(BlacklistController.checkBlacklistedWords(users.getName())
                || BlacklistController.checkBlacklistedWords(users.getLastname())
                || BlacklistController.checkBlacklistedWords(users.getSurname())
                || BlacklistController.checkBlacklistedWords(users.getPhone()))) {

            Users tmp = userService.findById(id);
            tmp.setName(userDto.getName());
            tmp.setLastname(userDto.getLastname());
            tmp.setSurname(userDto.getSurname());
            tmp.setPhone(userDto.getPhone());

            if (results.hasErrors()){
                System.out.println(results);
                model.addAttribute("bindingResult", results);
                model.addAttribute("user", tmp);
                model.addAttribute("userDto", userDto);
                model.addAttribute("image", image);
                model.addAttribute("color", color);
                model.addAttribute("userLog", user);
                return "users/edit";
            }else{

                session.setAttribute("user", user);

                if (!file.isEmpty()) {
                    String ruta = "C:/sigev/docs";
                    String nombre = DocumentoUtileria.guardarDocumento(file, ruta);
                    if (nombre != null) {
                        tmp.setProfilePicture(nombre);
                    }
                }

                res = userService.save(tmp);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success", "Se modificó el registro correctamente");
                    return "redirect:/users/list/enlaces";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar el registro");
                }
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

    @RequestMapping(value = "/admin/signup", method = RequestMethod.POST)
    public String adminSignup( @Valid Users users, BindingResult result, @RequestParam("confirmarContraseña") String confirmarContraseña,
                               Model model, Authentication authentication, RedirectAttributes redirectAttributes, @RequestParam("picture")  MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        Users user = userService.findByUsername(authentication.getName());
        if (result.hasErrors()){
            model.addAttribute("image", image);
            model.addAttribute("color", color);
            model.addAttribute("userLog", user);
            return "administrador/create";
        }else{
            if (!(BlacklistController.checkBlacklistedWords(users.getName())
                    || BlacklistController.checkBlacklistedWords(users.getLastname())
                    || BlacklistController.checkBlacklistedWords(users.getUsername())
                    || BlacklistController.checkBlacklistedWords(users.getPhone())
                    || BlacklistController.checkBlacklistedWords(users.getPassword()))) {
                if (!users.getPassword().equals(confirmarContraseña)) {
                    result.rejectValue("password", "error.password", "Las contraseñas no coinciden");
                    model.addAttribute("image", image);
                    model.addAttribute("color", color);
                    model.addAttribute("userLog", user);
                    return "administrador/create";
                }else if (userService.existByUsername(users.getUsername())){
                    result.rejectValue("username", "error.username", "El nombre de usuario ya existe en el sistema");
                    model.addAttribute("image", image);
                    model.addAttribute("color", color);
                    model.addAttribute("userLog", user);
                    return "administrador/create";
                }if (userService.existByEmail(users.getEmail())) {
                    result.rejectValue("email", "error.email", "El correo ya existe en el sistema");
                    model.addAttribute("image", image);
                    model.addAttribute("color", color);
                    model.addAttribute("userLog", user);
                    return "administrador/create";
                }

                if (!file.isEmpty()) {
                    String ruta = "C:/sigev/docs";
                    String nombre = DocumentoUtileria.guardarDocumento(file, ruta);
                    if (nombre != null) {
                        users.setProfilePicture(nombre);
                    }
                }

                users.setPassword(passwordEncoder.encode(users.getPassword()));
                users.addRole(rolesService.findByAuthority("ROL_ADMINISTRADOR"));

                boolean res = userService.save(users);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success","Administrador registrado correctamente");
                    return "redirect:/users/list/administradores";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un error al registrar al Enlace");
                }

            }else{
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        }
        return "redirect:/users/create/admin";
    }

    @RequestMapping(value = "/enlace/signup", method = RequestMethod.POST)
    public String enlaceSignup( @Valid UserDto userDto, BindingResult result, @RequestParam("confirmarContraseña") String confirmarContraseña,
                               Model model, Authentication authentication, RedirectAttributes redirectAttributes, @RequestParam("picture")  MultipartFile file) throws IOException {
        model.addAttribute("listCities", cityService.findAllByStatus(1));
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        Users user = userService.findByUsername(authentication.getName());
        Users users = new Users();
        if (result.hasErrors()){
            model.addAttribute("image", image);
            model.addAttribute("color", color);
            model.addAttribute("userLog", user);
            return "users/create";
        }else{
            if (!(BlacklistController.checkBlacklistedWords(userDto.getName())
                    || BlacklistController.checkBlacklistedWords(userDto.getLastname())
                    || BlacklistController.checkBlacklistedWords(userDto.getUsername())
                    || BlacklistController.checkBlacklistedWords(userDto.getPhone())
                    || BlacklistController.checkBlacklistedWords(userDto.getPassword()))) {
                if (!userDto.getPassword().equals(confirmarContraseña)) {
                    result.rejectValue("password", "error.password", "Las contraseñas no coinciden");
                    model.addAttribute("image", image);
                    model.addAttribute("color", color);
                    model.addAttribute("userLog", user);
                    return "users/create";
                }else if (userService.existByUsername(userDto.getUsername())){
                    result.rejectValue("username", "error.username", "El nombre de usuario ya existe en el sistema");
                    model.addAttribute("image", image);
                    model.addAttribute("color", color);
                    model.addAttribute("userLog", user);
                    return "users/create";
                }if (userService.existByEmail(userDto.getEmail())) {
                    result.rejectValue("email", "error.email", "El correo ya existe en el sistema");
                    model.addAttribute("image", image);
                    model.addAttribute("color", color);
                    model.addAttribute("userLog", user);
                    return "users/create";
                }
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

                    if (!file.isEmpty()) {
                        String ruta = "C:/sigev/docs";
                        String nombre = DocumentoUtileria.  guardarDocumento(file, ruta);
                        if (nombre != null) {
                            obj.setProfilePicture(nombre);
                        }
                    }

                    boolean res = userService.save(obj);

                    CityLink tmpLink = new CityLink();
                    tmpLink.setCity(cityService.findOne(userDto.getCity()));
                    tmpLink.setUser(obj);
                    boolean res2 = linkService.save(tmpLink);
                    if (res && res2) {
                        redirectAttributes.addFlashAttribute("msg_success",
                                "Enlace registrado correctamente, Ahora puede iniciar sesión con este usuario");
                        return "redirect:/users/list/enlaces";
                    } else {
                        redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un error al registrar al Enlace");
                    }
                } else {
                    redirectAttributes.addFlashAttribute("msg_error", "El municipio ya tiene un enlace asignado");
                }

            }else{
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
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
            return "redirect:/users/list/enlaces";
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
        return "redirect:/users/list/enlaces";
    }

    @RequestMapping(value = "/disable/admin/{id}", method = RequestMethod.GET)
    public String disableAdmin(@PathVariable("id") long id, RedirectAttributes redirectAttributes,
                              Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        user.setPassword(userService.findPasswordById(user.getId()));
        Users tmp = userService.findById(id);
        tmp.setPassword(userService.findPasswordById(id));
        if (user.getUsername().equals(tmp.getUsername())) {
            redirectAttributes.addFlashAttribute("msg_error", "No puedes deshabilitarte");
            return "redirect:/users/list/administradores";
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
        return "redirect:/users/list/administradores";
    }

}
