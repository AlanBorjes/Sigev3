package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.*;
import mx.edu.utez.sigev.entity.DataTransferObject.RecoverPasswordDto;
import mx.edu.utez.sigev.entity.DataTransferObject.UserUpdateDTO;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.*;
import mx.edu.utez.sigev.util.DocumentoUtileria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "/committee")
public class CommitteeController {
    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private CommitteePresidentService committeePresidentService;

    @Autowired
    private CityService cityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired CityLinkService cityLinkService;

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

    @Autowired
    private RolesService rolesService;
    /* @Autowired
    private CommitteeTeamService committeeTeamService;*/



    @GetMapping(value = "/list")
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
            return "/committee/editPassword";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró el usuario solicitado");
        }
        return "redirect:/committee/listar";
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
                return "committee/editPassword";
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
                return "redirect:/committee/listar";
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar la contraseña");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }
        return ("redirect:/committee/edit/" + id);
    }


    @GetMapping("/listar")
    public String pruebaCommittee(Model model, Authentication authentication){
        List<CommitteePresident> comites = committeePresidentService.findAll();
        List<CommitteePresident> filteredList = new ArrayList<>();
        System.out.println(committeePresidentService.findAll());
        Users user = userService.findByUsername(authentication.getName());
        City city = cityLinkService.findByUserId(user.getId()).getCity();
        for (CommitteePresident comite : comites){
            if (comite.getCommittee().getSuburb().getCity().getId() == city.getId()){
                filteredList.add(comite);
            }
        }
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("color", color);
        model.addAttribute("image", image);
        model.addAttribute("comites", filteredList);
        return "committee/comites";
    }

    @GetMapping("/{id}")
    public String comiteById(Model model, Authentication authentication, @PathVariable("id") long id){
        CommitteePresident comite = committeePresidentService.findById(id);
        Users user = userService.findByUsername(authentication.getName());
        City city = cityLinkService.findByUserId(user.getId()).getCity();
        if (!(comite.getCommittee().getSuburb().getCity().getId() == city.getId())){
            return "error/403";
        }
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("color", color);
        model.addAttribute("image", image);
        model.addAttribute("comite", comite);
        return "committee/comite";
    }



    @GetMapping("/create")
    public String createCommittee(Committee committee, Model model, Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        City city = cityLinkService.findByUserId(user.getId()).getCity();
        user.setPassword(null);
        session.setAttribute("user", user);
        //model.addAttribute("listCities", cityService.findAllCitiesByStateId(linkService.findByUserId(user.getId()).getCity().getState().getId()));
        model.addAttribute("listSuburbs", suburbService.findByCity(city.getId()));
        model.addAttribute("listStates", stateService.findAll());
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("color", color);
        model.addAttribute("image", image);
        model.addAttribute("committee", committee);

        System.out.println(committee);
        return "committee/crear";
    }

    @PostMapping("/guardar")
    public String createCommittee(@ModelAttribute("committee") @Validated Committee committee, BindingResult result,
                                  RedirectAttributes redirectAttributes, Model model, Authentication authentication,
                                  @RequestParam("user0") String user0, @RequestParam("user0LastName") String user0LastName, @RequestParam("user0Surname") String user0Surname, @RequestParam("user0Username") String user0Username, @RequestParam("user0Email") String user0Email, @RequestParam("user0Phone") String user0Phone, @RequestParam("user0Password") String user0Password, @RequestParam("picture0") MultipartFile file,
                                  @RequestParam("user1") String user1, @RequestParam("user1LastName") String user1LastName, @RequestParam("user1Surname") String user1Surname, @RequestParam("user1Username") String user1Username, @RequestParam("user1Email") String user1Email, @RequestParam("user1Phone") String user1Phone, @RequestParam("user1Password") String user1Password, @RequestParam("picture1") MultipartFile file1,
                                  @RequestParam("user2") String user2, @RequestParam("user2LastName") String user2LastName, @RequestParam("user2Surname") String user2Surname, @RequestParam("user2Username") String user2Username, @RequestParam("user2Email") String user2Email, @RequestParam("user2Phone") String user2Phone, @RequestParam("user2Password") String user2Password, @RequestParam("picture2") MultipartFile file2,
                                  @RequestParam("user3") String user3, @RequestParam("user3LastName") String user3LastName, @RequestParam("user3Surname") String user3Surname, @RequestParam("user3Username") String user3Username, @RequestParam("user3Email") String user3Email, @RequestParam("user3Phone") String user3Phone, @RequestParam("user3Password") String user3Password, @RequestParam("picture3") MultipartFile file3
                                  ) throws IOException {


        Users presidente = new Users(user0, user0LastName, user0Surname, user0Username, user0Phone, passwordEncoder.encode(user0Password) , user0Email, null);
        Users u1 = new Users(user1, user1LastName, user1Surname, user1Username, user1Phone, passwordEncoder.encode(user1Password) , user1Email, null);
        Users u2 = new Users(user2, user2LastName, user2Surname, user2Username, user2Phone, passwordEncoder.encode(user2Password) , user2Email, null);


        if (!file.isEmpty()) {
            String ruta = "C:/sigev/docs";
            String nombre = DocumentoUtileria.  guardarDocumento(file, ruta);
            if (nombre != null) {
                presidente.setProfilePicture(nombre);
            }
        }
        if (!file1.isEmpty()) {
            String ruta = "C:/sigev/docs";
            String nombre = DocumentoUtileria.  guardarDocumento(file1, ruta);
            if (nombre != null) {
                u1.setProfilePicture(nombre);
            }
        }
        if (!file2.isEmpty()) {
            String ruta = "C:/sigev/docs";
            String nombre = DocumentoUtileria.  guardarDocumento(file2, ruta);
            if (nombre != null) {
                u2.setProfilePicture(nombre);
            }
        }

        Users userLog = userService.findByUsername(authentication.getName());
        boolean res;
        int iter = 0;
        CommitteePresident committeePresident = new CommitteePresident();
        Set<Users> equipo = new HashSet<>();
        //committee.setStatus(1);

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            model.addAttribute("userLog", userLog);
            model.addAttribute("color", color);
            model.addAttribute("image", image);
            System.out.println("entre1");
            return "committee/crear";

        }else{
            System.out.println("entre2");
            System.out.println(committee.getUsers());
            res = committeeService.save(committee);
            if (res){
                if (user3.isEmpty() && user3LastName.isEmpty() && user3Username.isEmpty() && user3Surname.isEmpty() && user3Phone.isEmpty() && user3Password.isEmpty() && user3Email.isEmpty()){
                    System.out.println("No registra usuario 4");
                }else{
                    if (user3.isEmpty() || user3LastName.isEmpty() || user3Username.isEmpty() || user3Phone.isEmpty() || user3Password.isEmpty() || user3Email.isEmpty()){
                        System.out.println("ALGUN CAMPO VACIO");
                        Color color = colorService.findColors(1);
                        Images image = imagesService.findImages(1);
                        model.addAttribute("msg_error", "Si registra un 4 usuario asegurese que todos los campos estén llenos");
                        model.addAttribute("userLog", userLog);
                        model.addAttribute("color", color);
                        model.addAttribute("image", image);
                        return "committee/crear";
                    }else{
                        Users u3 = new Users(user3, user3LastName, user3Surname, user3Username, user3Phone, passwordEncoder.encode(user3Password) , user3Email, null);
                        if (!file3.isEmpty()) {
                            String ruta = "C:/sigev/docs";
                            String nombre = DocumentoUtileria.  guardarDocumento(file3, ruta);
                            if (nombre != null) {
                                u3.setProfilePicture(nombre);
                            }
                        }
                        u3.addRole(rolesService.findByAuthority("ROL_MIEMBRO"));
                        userService.save(u3);
                        equipo.add(u3);
                    }
                }
                presidente.addRole(rolesService.findByAuthority("ROL_PRESIDENTE"));
                userService.save(presidente);
                committeePresident.setUser(presidente);
                committeePresident.setCommittee(committee);
                committeePresidentService.save(committeePresident);
                u1.addRole(rolesService.findByAuthority("ROL_MIEMBRO"));
                userService.save(u1);
                equipo.add(u1);
                u2.addRole(rolesService.findByAuthority("ROL_MIEMBRO"));
                userService.save(u2);
                equipo.add(u2);

                committee.setUsers(equipo);
                committeeService.save(committee);
                redirectAttributes.addFlashAttribute("msg_error", "Se creó correctamente el comite");
            }else{
                redirectAttributes.addFlashAttribute("msg_error", "No se encontró el comité solicitado");
                return "committee/crear";
            }
        }

        return "redirect:/committee/listar";
    }



    @GetMapping(value = "/find/{id}")
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

    @PostMapping(value = "/president/{idComi}")
    public String presidentNew( @RequestParam("idUser") long id, @PathVariable("idComi") long idComi){
        CommitteePresident committeePresident = committeePresidentService.findById(idComi);
        Users presiAct = committeePresidentService.findById(idComi).getUser();
        Users presiNew = userService.findById(id);


        System.out.println(id);

        return "/committee/comite/" + idComi;
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
            return "/committee/edit";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró el usuario solicitado");
        }
        return "redirect:/committee/listar";
    }


    /*@GetMapping(value = "/update/{id}")
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
    }*/

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
                return "committee/edit";
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
                    return "redirect:/committee/listar";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un problema al modificar el registro");
                }
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }
        return ("redirect:/committee/edit/" + id);
    }

    @RequestMapping (value = "/details/{id}", method = RequestMethod.GET)
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