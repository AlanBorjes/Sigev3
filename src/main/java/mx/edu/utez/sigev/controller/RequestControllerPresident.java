package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.*;
import mx.edu.utez.sigev.entity.DataTransferObject.RequestDto;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.*;
import mx.edu.utez.sigev.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/president")
public class RequestControllerPresident {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService usersService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RequestAttachmentsService attachmentsService;

    @Autowired
    private CommentaryService commentaryService;

    @Autowired
    private CommitteePresidentService presidentService;

    @RequestMapping(value = "/list/unpaid", method = RequestMethod.GET)
    public String listAllPresidentUnpaidRequests(Authentication authentication, HttpSession session, Model model,
            RedirectAttributes redirectAttributes) {
        Users user = usersService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        user.setPassword(usersService.findPasswordById(user.getId()));
        model.addAttribute("unpaidList", requestService.findAllUnpaidByCommitteeId(presidentService.findByUser(user.getId()).getCommittee().getId()));
        return "president-request/unpaidList";
    }

    @RequestMapping(value = "/pay/{id}/{status}", method = RequestMethod.GET)
    public ResponseEntity<Object> payRequest(@PathVariable("id") long id, @PathVariable("status") String status,
            RedirectAttributes redirectAttributes) {
        Map<String, Object> data = new HashMap<>();
        if (status.equals("COMPLETED")) {
            Request tmp = requestService.findById(id);
            if (!tmp.equals(null)) {
                tmp.setPaymentStatus(2);
                tmp.setStatus(3);
                requestService.save(tmp);
                data.put("error", false);
            } else {
                data.put("error", true);
            }
        } else {
            data.put("error", true);
        }
        if (data.get("error").equals(false)) {
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    public String showRequestDetails(@PathVariable("id") long id, Authentication authentication, HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        Users user = usersService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        model.addAttribute("request", requestService.findById(id));
        model.addAttribute("attachment", attachmentsService.findByRequestId(id));
        return "president-request/details";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listAllPresidentRequests(Authentication authentication, HttpSession session, Model model,
            RedirectAttributes redirectAttributes, Pageable pageable) {
        Users user = usersService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        CommitteePresident president = presidentService.findByUser(user.getId());
        Page<Request> listRequests = requestService
                .listarPaginacion(PageRequest.of(pageable.getPageNumber(), 2, Sort.by("startDate").descending()));
        model.addAttribute("requestList", requestService.findAllByCommitteeId(president.getCommittee().getId()));
        model.addAttribute("requestList2", listRequests);
        return "president-request/list";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createPresidentRequest(Authentication authentication, HttpSession session, Model model,
            RedirectAttributes redirectAttributes, RequestDto requestDto) {
        Users user = usersService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        model.addAttribute("categoryList", categoryService.findAll(1));
        return "president-request/create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String savePresidentRequest(Authentication authentication, HttpSession session, Model model,
            RedirectAttributes redirectAttributes, RequestDto requestDto,
            @RequestParam("attachment") MultipartFile multipartFile) {
        Users user = usersService.findByUsername(authentication.getName());
        Users tmpuser = user;
        user.setPassword(null);
        session.setAttribute("user", user);
        if (!BlacklistController.checkBlacklistedWords(requestDto.getDescription())) {
            Request obj = new Request();
            obj.setCategory(requestDto.getCategory());
            obj.setDescription(requestDto.getDescription());
            obj.setStartDate(new Date());
            obj.setStatus(2);
            obj.setPaymentStatus(1);
            obj.setPresident(presidentService.findByUser(user.getId()));
            obj.getPresident().getUser().setPassword(usersService.findPasswordById(tmpuser.getId()));
            boolean res1 = requestService.save(obj);
            if (res1) {
                if (!multipartFile.isEmpty()) {
                    RequestAttachment attachments = new RequestAttachment();
                    String path = "C:/comve/docs";
                    String filename = FileUtil.saveFile(multipartFile, path);
                    if (filename != null) {
                        attachments.setName(filename.replaceAll(" ", "").replaceAll("-", "").replace("°", ""));
                        attachments.setRequest(obj);
                        boolean res2 = attachmentsService.save(attachments);
                        if (res2) {
                            redirectAttributes.addFlashAttribute("msg_success",
                                    "¡Se registró la solicitud con la evidencia!");
                            return "redirect:/president/list";
                        } else {
                            redirectAttributes.addFlashAttribute("msg_error",
                                    "Ocurrió un error al registrar la solicitud con la evidencia");
                            return "redirect:/president/create";
                        }
                    } else {
                        redirectAttributes.addFlashAttribute("msg_success", "¡Se registró la solicitud correctamente!");
                        return "redirect:/president/list";
                    }
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un error al registrar la solicitud");
                return "redirect:/president/create";
            }
            redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un fallo");
            return "redirect:/president/create";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas.");
            return "redirect:/president/create";
        }
    }

    @RequestMapping(value = "/commentary/{id}", method = RequestMethod.GET)
    public String chat(@PathVariable("id") long id, Authentication authentication, HttpSession session, Model model,
            RedirectAttributes redirectAttributes, Commentary commentary) {
        Users user = usersService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        model.addAttribute("listComents", commentaryService.findAllByRequestId(id));
        model.addAttribute("request", requestService.findById(id));
        return "president-request/comments";
    }

    @RequestMapping(value = "/commentary/save/{id}", method = RequestMethod.POST)
    public String saveCommentary(Model model, Commentary commentary, Authentication authentication,
            HttpSession session, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Users user = usersService.findByUsername(authentication.getName());
        user.setPassword(null);
        session.setAttribute("user", user);
        commentary.setRequest(requestService.findById(id));
        if (!BlacklistController.checkBlacklistedWords(commentary.getContent())) {
            Users tmp = usersService.findById(user.getId());
            tmp.setPassword(usersService.findPasswordById(tmp.getId()));
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
        return ("redirect:/president/commentary/" + id);
    }

}
