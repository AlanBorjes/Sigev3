package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.Category;
import mx.edu.utez.sigev.entity.City;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.CityService;
import mx.edu.utez.sigev.service.StateService;
import mx.edu.utez.sigev.util.DocumentoUtileria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        List<City> listCities = cityService.findAll();
        model.addAttribute("listCities", listCities);
        return "city/municipios";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, RedirectAttributes redirectAttributes, City city) {
        model.addAttribute("listStates", stateService.findAll());
        return "city/create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Model model, RedirectAttributes redirectAttributes, @Valid City city, BindingResult result, @RequestParam("picture") MultipartFile file) throws IOException {

        if (result.hasErrors()){
            return "city/create";
        }else{
            if (!BlacklistController.checkBlacklistedWords(city.getName())) {
                if (!file.isEmpty()) {
                    String ruta = "C:/sigev/docs";
                    String nombre = DocumentoUtileria.guardarDocumento(file, ruta);
                    if (nombre != null) {
                        city.setShield(nombre);
                    }
                }

                boolean res = cityService.save(city);
                if (res) {
                    redirectAttributes.addAttribute("msg_success", "Municipio registrado correctamente");
                    return "redirect:/city/list";
                } else {
                    redirectAttributes.addAttribute("msg_error", "Ocurrió un problema al guardar el Municipio");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        }

        return "redirect:/city/create";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, RedirectAttributes redirectAttributes, City category, @PathVariable("id") long id) {
        City tmp = cityService.findOne(id);
        if (!tmp.equals(null)) {
            model.addAttribute("listStates", stateService.findAll());
            model.addAttribute("city", tmp);
            return "city/edit";
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "El municipio seleccionado no existe");
        }
        return "redirect:/city/list";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(Model model, RedirectAttributes redirectAttributes, City city, @PathVariable("id") long id) {
        if (!BlacklistController.checkBlacklistedWords(city.getName())) {
            City tmp = cityService.findOne(id);
            if (!tmp.equals(null)) {
                tmp.setName(city.getName());
                boolean res = cityService.save(tmp);
                if (res) {
                    model.addAttribute("msg_success", "Municipio actualizado correctamente");
                    return "redirect:/city/list";
                } else {
                    model.addAttribute("msg_error", "Ocurrió un error al actualizar el Municipio");
                }
            } else {
                model.addAttribute("msg_error", "El Municipio seleccionado no existe");
            }
        } else {
            model.addAttribute("msg_error", "Ingresó una o más palabras prohibidas");
        }
        return ("redirect:/city/edit/" + id);
    }

    @RequestMapping(value = "/desactivate/{id}", method = RequestMethod.GET)
    public String categoryDesactivate(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") long id,
                                      City city) {
        String msg = "";
        City tmp = cityService.findOne(id);
        if (!tmp.equals(null)) {
            if (!BlacklistController.checkBlacklistedWords(tmp.getName())) {
                if (tmp.getStatus() == 1){
                    tmp.setStatus(0);
                    msg = "Se inhabilitó el municipio";
                }else{
                    tmp.setStatus(1);
                    msg = "Se habilitó el municipio";
                }
                boolean res = cityService.save(tmp);
                if (res) {
                    redirectAttributes.addFlashAttribute("msg_success", msg);
                    return "redirect:/city/list";
                } else {
                    redirectAttributes.addFlashAttribute("msg_error",
                            "Ocurrió un error al actualizar el municipio");
                }
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ingresó una o más palabras prohibidas");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg_error", "El municipio solicitado no existe.");
        }
        return "redirect:/city/edit/" + id;
    }

}
