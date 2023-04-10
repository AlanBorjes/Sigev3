package mx.edu.utez.sigev.controller;

import mx.edu.utez.sigev.entity.City;
import mx.edu.utez.sigev.security.BlacklistController;
import mx.edu.utez.sigev.service.CityService;
import mx.edu.utez.sigev.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        Page<City> listCities = cityService
                .listarPaginacion(PageRequest.of(pageable.getPageNumber(), 110, Sort.by("id").descending()));
        model.addAttribute("listCities", listCities);
        return "city/list";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, RedirectAttributes redirectAttributes, City city) {
        model.addAttribute("listStates", stateService.findAll());
        return "city/create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Model model, RedirectAttributes redirectAttributes, City city) {
        if (!BlacklistController.checkBlacklistedWords(city.getName())) {
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
                tmp.setState(city.getState());
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

}
