package TaskListManagmentSecurity.controllers;

import TaskListManagmentSecurity.models.User;
import TaskListManagmentSecurity.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping()
    public String getTasks(Model model) {
        model.addAttribute("usersList", adminService.findAll());
        return "admin/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("usersList", adminService.findAll());
        return "admin/index";
    }


    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", adminService.findOne(id));
        return "admin/show";
    }

    @PatchMapping("/{id}")
    public String updateTask(@PathVariable("id") int id, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors())
            return "admin/edit";

        adminService.update(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", adminService.findOne(id));
        return "admin/edit";
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable("id") int id) {
        adminService.delete(id);
        return "redirect:/admin";
    }
}
