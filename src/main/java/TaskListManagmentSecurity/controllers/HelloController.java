package TaskListManagmentSecurity.controllers;

import TaskListManagmentSecurity.models.User;
import TaskListManagmentSecurity.security.MyUserDetails;
import TaskListManagmentSecurity.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(Model model) {
        TasksController.authenticate(model);
        return "hello";
    }

}
