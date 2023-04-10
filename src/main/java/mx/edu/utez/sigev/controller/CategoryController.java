package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.Category;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listCategories(Model model, RedirectAttributes redirectAttributes, Pageable pageable,
            Authentication authentication, HttpSession session) {
        List<Category> listCategory = categoryService.findAll(1);
        model.addAttribute("listCategories", listCategory);
        return "category/categorias";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createCategory(Model model, RedirectAttributes redirectAttributes, Category category) {
        return "category/create";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String categoryEdit(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            Category category) {
        Category tmp = categoryService.findById(id);
        if (!tmp.equals(null)) {
            model.addAttribute("category", tmp);
            return "category/edit";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "El Servicio público solicitado no existe.");
            return "redirect:/category/list";
        }
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String categoryUpdate(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            Category category) {
        Category tmp = categoryService.findById(id);
        if (!tmp.equals(null)) {
            if (!BlacklistController.checkBlacklistedWords(tmp.getName())) {
                tmp.setName(category.getName());
                boolean res = categoryService.save(tmp);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success", "Servicio Público actualizado");
                    return "redirect:/category/list";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error",
                            "Ocurrió un error al actualizar el Servicio Público");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "El Servicio público solicitado no existe.");
        }
        return "redirect:/category/edit/" + id;
    }

    @RequestMapping(value = "/desactivate/{id}", method = RequestMethod.GET)
    public String categoryDesactivate(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") long id,
                                 Category category) {
        Category tmp = categoryService.findById(id);
        if (!tmp.equals(null)) {
            if (!BlacklistController.checkBlacklistedWords(tmp.getName())) {
                tmp.setStatus(0);
                boolean res = categoryService.save(tmp);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success", "Se inhabilitó la categoría");
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

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Model model, RedirectAttributes redirectAttributes, Category category) {
        if (!categoryService.exists(category.getName())) {
            if (!BlacklistController.checkBlacklistedWords(category.getName())) {
                category.setStatus(1);
                boolean res = categoryService.save(category);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success", "Servicio Público registrado exitosamente");
                    return "redirect:/category/list";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error", "No se pudo registrar el Servicio Público");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "Este servicio público ya existe");
        }
        return "redirect:/category/create";
    }

}
