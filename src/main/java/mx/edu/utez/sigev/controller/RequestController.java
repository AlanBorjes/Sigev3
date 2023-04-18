package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.*;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.*;
import mx.edu.utez.sigev.util.Runner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.nio.file.LinkOption;

@Controller
@RequestMapping(value = "/request")
public class RequestController {

    private static final Logger logger = LogManager.getLogger(Runner.class);

    @Autowired
    private RequestService requestService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentaryService commentaryService;
    @Autowired
    RequestAttachmentsService attachmentsService;
    @Autowired
    private CityLinkService linkService;

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private ColorService colorService;
    @Autowired
    private RequestAttachmentsService requestAttachmentsService;
    @RequestMapping(value = "/amount/{id}", method = RequestMethod.GET)
    public String amount(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Request request = requestService.findById(id);
        if (!request.equals(null)) {
            model.addAttribute("request", requestService.findById(id));
            model.addAttribute("listRequests", requestService.findAll());
            return "requests/amountRequests";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Registro No Encontrado");
            return "redirect:/request/list";
        }
    }

    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    public String details(Authentication authentication,Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes,Commentary commentary,Request request) {
        Users user = userService.findByUsername(authentication.getName());
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);

        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        model.addAttribute("listComents", commentaryService.findAllByRequestId(id));
        Request sa = requestService.findById(id);
        if (!requestService.findById(id).equals(null)) {
            model.addAttribute("request", requestService.findById(id));;
            System.out.println(sa.getRequestAttachment());
            return "/requests/detailsRequests";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "La solicitud que buscas no existe");
            return "redirect:/request/list";
        }
    }
/*
    @PostMapping(value = "/update")
    public String actualizar(@ModelAttribute("request") Request request, Model modelo,
                             RedirectAttributes redirectAttributes) {
        Request obj = requestService.findById(request.getId());
        if (!BlacklistController.checkBlacklistedWords(obj.getDescription())) {
            obj.setPaymentAmount(request.getPaymentAmount());
            if (obj != null) {
                requestService.save(obj);
                modelo.addAttribute("listRequests", requestService.findAll());
            }
            return "redirect:/request/list";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas.");
            return "redirect:/request/list";
        }

    }*/

    @PostMapping(value = "/update")
    public String actualizar(@ModelAttribute("request") Request request, Model modelo,
                             RedirectAttributes redirectAttributes) {
        Request obj = requestService.findById(request.getId());
        if (!BlacklistController.checkBlacklistedWords(obj.getDescription())) {
            obj.setPaymentAmount(request.getPaymentAmount());
            if (obj != null) {
                requestService.save(obj);
                modelo.addAttribute("listRequests", requestService.findAll());
            }
            return "redirect:/request/list";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas.");
            return "redirect:/request/list";
        }

    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String findAll(Model model, Pageable pageable, Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        Images image = imagesService.findImages(1);

        user.setPassword(null);
        session.setAttribute("user", user);
        /*Page<Request> listRequests = requestService
                .listarPaginacion(PageRequest.of(pageable.getPageNumber(), 2, Sort.by("startDate").descending()));*/
        model.addAttribute("listRequests", requestService.findAllByCityId(linkService.findByUserId(user.getId()).getCity().getId()));

        Color color = colorService.findColors(1);
        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        return "requests/listRequests";
    }

    @GetMapping("/create")

    public String create(Request request, Model modelo) {
        modelo.addAttribute("listRequests", requestService.findAll());
        return "requests/amountRequests";
    }
    @GetMapping(value = "/find/{id}")
    public String findOne(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Request request = requestService.findById(id);
        if (!request.equals(null)) {
            model.addAttribute("request", request);
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "No se encontró la solicitud solicitada");
        }
        return "requests/listRequests";
    }

    @PostMapping(value = "/save")
    public String save(Model model, Request request, RedirectAttributes redirectAttributes) {
        String msgOk = "";
        String msgError = "";

        if (!BlacklistController.checkBlacklistedWords(request.getDescription())) {
            if (request.getId() != null) {
                msgOk = "Solictud Actualizada correctamente";
                msgError = "La solicitud NO pudo ser Actualizada correctamente";
            } else {
                msgOk = "Solicitud Guardada correctamente";
                msgError = "La solicitud NO pudo ser Guardada correctamente";
            }

            boolean res = requestService.save(request);
            if (res) {
                redirectAttributes.addFlashAttribute("msg_success", msgOk);
            } else {
                redirectAttributes.addFlashAttribute("msg_error", msgError);
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas.");
        }
        return "redirect:/request/list";

    }

    @PostMapping(value = "/save/paymentAmount")
    public String savepaymentAmount(Model model, Request request, RedirectAttributes redirectAttributes) {
        Request obj = requestService.findById(request.getId());
        obj.setPaymentAmount(request.getPaymentAmount());
        String msgOk = "";
        String msgError = "";
            if (obj.getId() != null) {
                msgOk = "Solictud Actualizada correctamente";
                msgError = "La solicitud NO pudo ser Actualizada correctamente";
            } else {
                msgOk = "Solicitud Guardada correctamente";
                msgError = "La solicitud NO pudo ser Guardada correctamente";
            }

            boolean res = requestService.save(obj);
            if (res) {
                redirectAttributes.addFlashAttribute("msg_success", msgOk);
            } else {
                redirectAttributes.addFlashAttribute("msg_error", msgError);
            }
        return ("redirect:/request/details/"+obj.getId());

    }

    @PostMapping(value = "/save/savepaymentStatus")
    public String savepaymentStatus(Model model, Request request, RedirectAttributes redirectAttributes) {
        Request obj = requestService.findById(request.getId());
        System.out.println(obj.getPaymentStatus());
        obj.setPaymentStatus(request.getPaymentStatus());
        System.out.println(obj.getPaymentStatus());
        System.out.println(request.getPaymentStatus());

        String msgOk = "";
        String msgError = "";
        if (obj.getId() != null) {
            msgOk = "Solictud Actualizada correctamente";
            msgError = "La solicitud NO pudo ser Actualizada correctamente";
        } else {
            msgOk = "Solicitud Guardada correctamente";
            msgError = "La solicitud NO pudo ser Guardada correctamente";
        }

        boolean res = requestService.save(obj);
        if (res) {
            redirectAttributes.addFlashAttribute("msg_success", msgOk);
        } else {
            redirectAttributes.addFlashAttribute("msg_error", msgError);
        }
        return ("redirect:/request/details/"+obj.getId());

    }
    @RequestMapping(value = "/commentary/{id}", method = RequestMethod.GET)
    public String chat(@PathVariable("id") long id, Authentication authentication, HttpSession session, Model model,
                       RedirectAttributes redirectAttributes, Commentary commentary) {
        Users user = userService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        model.addAttribute("listComents", commentaryService.findAllByRequestId(id));
        model.addAttribute("request", requestService.findById(id));
        return "/requests/detailsRequests";
    }

    @RequestMapping(value = "/commentary/save/{id}", method = RequestMethod.POST)
    public String saveCommentary(Model model, Commentary commentary, Authentication authentication,
                                 HttpSession session, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Users user = userService.findByUsername(authentication.getName());
        Color color = colorService.findColors(1);
        Images image = imagesService.findImages(1);
        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        model.addAttribute("listComents", commentaryService.findAllByRequestId(id));
        session.setAttribute("user", user);
        commentary.setRequest(requestService.findById(id));
         Long Idrequesr = commentary.getRequest().getId();
        if (!BlacklistController.checkBlacklistedWords(commentary.getContent())) {
            Users tmp = userService.findById(user.getId());
            tmp.setPassword(userService.findPasswordById(tmp.getId()));
            Roles tmpRole = (Roles) tmp.getRoles().toArray()[0];
            if (tmpRole.getAuthority().equals("ROL_PRESIDENTE")) {
                commentary.setAutor("Presidente");
            } else {
                commentary.setAutor("Enlace");
            }
            commentary.setId(null);
            boolean res = commentaryService.save(commentary);
            if (res) {
                redirectAttributes.addFlashAttribute("msg_success", "Comentario publicado");
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un error al publicar el comentario");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas.");
        }
        return ("redirect:/request/details/"+Idrequesr);
    }
}
