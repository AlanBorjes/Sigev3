package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.*;
import mx.edu.utez.sigev.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityLinkService linkService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ColorService colorService;

    @Autowired
    private ImagesService imagesService;




    @RequestMapping( value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin() {
        return "login";
    }

    @RequestMapping(value = "/administrador/dashboard", method = RequestMethod.GET)
	public String dashboardAdministrador(Authentication authentication, HttpSession session, Model model) {
        Users user = userService.findByUsername(authentication.getName());
		if (session.getAttribute("user") == null) {
			user.setPassword(null);
			session.setAttribute("user", user);
		}

        List<CityLink> listEnlaces = linkService.findAll();
        listEnlaces = listEnlaces.subList(0, Math.min(listEnlaces.size(), 2));
        List<Users> listAdmins = userService.findAllByRole(1);
        listAdmins = listAdmins.subList(0, Math.min(listAdmins.size(), 2));
        List<Category> listCategories = categoryService.findAll();
        listCategories = listCategories.subList(0, Math.min(listCategories.size(), 6));
        List<City> listMunicipios = cityService.findAll();
        listMunicipios = listMunicipios.subList(0, Math.min(listMunicipios.size(), 4));

        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("color", color);
        model.addAttribute("image", image);
        model.addAttribute("enlaces", listEnlaces);
        model.addAttribute("administradores", listAdmins);
        model.addAttribute("categorias", listCategories);
        model.addAttribute("municipios", listMunicipios);

		return "administrador/dashboard";
	}

    @RequestMapping(value = "/enlace/dashboard", method = RequestMethod.GET)
	public String dashboardEnlace(Authentication authentication, HttpSession session,Model model) {
        Users user = userService.findByUsername(authentication.getName());
        if (session.getAttribute("user") == null) {
			user.setPassword(null);
			session.setAttribute("user", user);
		}
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("color", color);
        model.addAttribute("image", image);
		return "enlace/dashboard";
	}

    @RequestMapping(value = "/miembro/dashboard", method = RequestMethod.GET)
	public String dashboardAMiembro(Authentication authentication, HttpSession session,Model model) {
        Users user = userService.findByUsername(authentication.getName());

        if (session.getAttribute("user") == null) {
			user.setPassword(null);
			session.setAttribute("user", user);
		}

        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
		return "miembro/dashboard";
	}

    @RequestMapping(value = "/presidente/dashboard", method = RequestMethod.GET)
	public String dashboardPresidente(Authentication authentication, HttpSession session,Model model) {
            Users user = userService.findByUsername(authentication.getName());
			user.setPassword(null);
            Color color = colorService.findColors(1);
            Images image = imagesService.findImages(1);
            model.addAttribute("userLog", user);
            model.addAttribute("image", image);
            model.addAttribute("color", color);

		return "presidente/dashboard";
	}

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, null, null);
            redirectAttributes.addFlashAttribute("msg_success", "¡Sesión cerrada! Hasta luego");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un error al cerrar la sesión, intenta nuevamente");
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/encriptar/{password}")
    @ResponseBody
    public String encryptPassword(@PathVariable("password") String password) {
        return password + " Encriptada con el algoritmo BCRYPT: " + passwordEncoder.encode(password);
    }
    
}
