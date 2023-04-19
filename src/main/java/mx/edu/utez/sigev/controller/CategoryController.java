package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.Category;
import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.Images;
import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.CategoryService;
import mx.edu.utez.sigev.service.ColorService;
import mx.edu.utez.sigev.service.ImagesService;
import mx.edu.utez.sigev.service.UserService;
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
import java.util.List;

@Controller
@RequestMapping(value = "/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/list")
    public String listCategories(Model model, RedirectAttributes redirectAttributes, Pageable pageable,
            Authentication authentication, HttpSession session) {
        Users user = userService.findByUsername(authentication.getName());
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        List<Category> listCategory = categoryService.findAll();
        model.addAttribute("userLog", user);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        model.addAttribute("listCategories", listCategory);
        return "category/categorias";
    }

    @GetMapping(value = "/create")
    public String createCategory(Model model, RedirectAttributes redirectAttributes, Category category, Authentication authentication) {
        Users user = userService.findByUsername(authentication.getName());
        model.addAttribute("userLog", user);
        Images image = imagesService.findImages(1);
        Color color = colorService.findColors(1);
        model.addAttribute("image", image);
        model.addAttribute("color", color);
        return "category/create";
    }

    @GetMapping(value = "/edit/{id}")
    public String categoryEdit(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            Category category, Authentication authentication) {
        Users user = userService.findByUsername(authentication.getName());
        Category tmp = categoryService.findById(id);
        if (tmp!=null) {
            Images image = imagesService.findImages(1);
            Color color = colorService.findColors(1);
            model.addAttribute("userLog", user);
            model.addAttribute("image", image);
            model.addAttribute("color", color);
            model.addAttribute("category", tmp);
            return "category/edit";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "La categoría solicitada no existe.");
            return "redirect:/category/list";
        }
    }

    @PostMapping(value = "/update/{id}")
    public String categoryUpdate(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            Category category) {
        Category tmp = categoryService.findById(id);
        if (tmp!=null) {
            if (!BlacklistController.checkBlacklistedWords(tmp.getName())) {
                tmp.setName(category.getName());
                boolean res = categoryService.save(tmp);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success", "Categoría actualizada");
                    return "redirect:/category/list";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error",
                            "Ocurrió un error al actualizar el Servicio Público");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "La categoría solicitada no existe.");
        }
        return "redirect:/category/edit/" + id;
    }

    @GetMapping(value = "/desactivate/{id}")
    public String categoryDesactivate(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") long id,
                                 Category category) {
        String msg;
        Category tmp = categoryService.findById(id);
        if (tmp!=null) {
            if (!BlacklistController.checkBlacklistedWords(tmp.getName())) {
                if (tmp.getStatus() == 1){
                    tmp.setStatus(0);
                    msg = "Se inhabilitó la categoría";
                }else{
                    tmp.setStatus(1);
                    msg = "Se habilitó la categoría";
                }
                boolean res = categoryService.save(tmp);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success", msg);
                    return "redirect:/category/list";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error",
                            "Ocurrió un error al actualizar la categoría");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "La categoría solicitada no existe.");
        }
        return "redirect:/category/edit/" + id;
    }

    @PostMapping(value = "/save")
    public String save(Model model, RedirectAttributes redirectAttributes, Category category) {
        if (!categoryService.exists(category.getName())) {
            if (!BlacklistController.checkBlacklistedWords(category.getName())) {
                category.setStatus(1);
                boolean res = categoryService.save(category);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success", "Categoría registrada exitosamente");
                    return "redirect:/category/list";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error", "No se pudo registrar la categoría");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Esta categoría ya existe");
        }
        return "redirect:/category/create";
    }

}
