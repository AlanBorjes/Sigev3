package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.ContentApp;
import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.service.ColorService;
import mx.edu.utez.sigev.service.ContentAppService;
import mx.edu.utez.sigev.service.UserService;
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

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private ColorService colorService;
    @Autowired
    private ContentAppService ContentAppService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping( value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)

    public String showLogin(Model model) {
        Color color = colorService.findById(1);
        ContentApp conten = ContentAppService.findById(1);
        model.addAttribute("logo1", conten.getLogo1());
        model.addAttribute("logo2", conten.getLogo2());
        model.addAttribute("colorFondo", color.getColorMain());
        model.addAttribute("colorSecundario", color.getColorSecundary());
        model.addAttribute("color_text", color.getColorText());
        model.addAttribute("color_title", color.getColorTitle());
        return "login";
    }

    @RequestMapping(value = "/administrador/dashboard", method = RequestMethod.GET)
	public String dashboardAdministrador(Authentication authentication, HttpSession session) {
		if (session.getAttribute("user") == null) {
			Users user = userService.findByUsername(authentication.getName());
			user.setPassword(null);
			session.setAttribute("user", user);
		}
		return "administrador/dashboard";
	}

    @RequestMapping(value = "/enlace/dashboard", method = RequestMethod.GET)
	public String dashboardEnlace(Authentication authentication, HttpSession session) {
		if (session.getAttribute("user") == null) {
			Users user = userService.findByUsername(authentication.getName());
			user.setPassword(null);
			session.setAttribute("user", user);
		}
		return "enlace/dashboard";
	}

    @RequestMapping(value = "/miembro/dashboard", method = RequestMethod.GET)
	public String dashboardAMiembro(Authentication authentication, HttpSession session) {
		if (session.getAttribute("user") == null) {
			Users user = userService.findByUsername(authentication.getName());
			user.setPassword(null);
			session.setAttribute("user", user);
		}
		return "miembro/dashboard";
	}

    @RequestMapping(value = "/presidente/dashboard", method = RequestMethod.GET)
	public String dashboardPresidente(Authentication authentication, HttpSession session) {
		if (session.getAttribute("user") == null) {
			Users user = userService.findByUsername(authentication.getName());
			user.setPassword(null);
			session.setAttribute("user", user);
		}
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
