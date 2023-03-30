package TaskListManagmentSecurity.controllers;

import TaskListManagmentSecurity.dto.TaskDTO;
import TaskListManagmentSecurity.models.Task;
import TaskListManagmentSecurity.models.User;
import TaskListManagmentSecurity.security.MyUserDetails;
import TaskListManagmentSecurity.services.TasksService;
import TaskListManagmentSecurity.util.TaskErrorResponse;
import TaskListManagmentSecurity.util.TaskNotCreatedException;
import TaskListManagmentSecurity.util.TaskNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TasksController {
    private final TasksService tasksService;
    private final ModelMapper modelMapper;

    @Autowired
    public TasksController(TasksService tasksService,
                           ModelMapper modelMapper) {
        this.tasksService = tasksService;
        this.modelMapper = modelMapper;
    }

    public static void authenticate(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().toString().equals("anonymousUser")) {
            model.addAttribute("currentRole", "ROLE_ANONYMOUS");
        } else {
            MyUserDetails personDetails = (MyUserDetails) authentication.getPrincipal();
            User user = personDetails.getUser();
            model.addAttribute("currentRole", user.getRole());
        }
    }

    @GetMapping()
    public String getTasks(Model model) {
        model.addAttribute("taskList",
                tasksService.findAll().stream()
                        .map(this::convertToTaskDTO)
                        .collect(Collectors.toList()));
        authenticate(model);
        return "tasks/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("taskList",
                tasksService.findAll().stream()
                        .map(this::convertToTaskDTO)
                        .collect(Collectors.toList()));
        authenticate(model);
        return "tasks/index";
    }

    @GetMapping("/{id}")
    public String getTask(@PathVariable("id") int id, Model model) {
        model.addAttribute("task", convertToTaskDTO(tasksService.findOne(id)));
        authenticate(model);
        return "tasks/show";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/new")
    public String newTask(@ModelAttribute("task") TaskDTO taskDTO) {
        return "tasks/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("task") @Valid TaskDTO taskDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "tasks/new";
        tasksService.save(convertToTask(taskDTO));
        return "redirect:/tasks";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public String updateTask(@PathVariable("id") int id, @ModelAttribute("task")
    @Valid TaskDTO taskDTO, Model model,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "tasks/edit";
        authenticate(model);
        tasksService.update(id, convertToTask(taskDTO));
        return "redirect:/tasks";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("task", convertToTaskDTO(tasksService.findOne(id)));
        return "tasks/edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable("id") int id) {
        tasksService.delete(id);
        return "redirect:/tasks";
    }

    private Task convertToTask(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    private TaskDTO convertToTaskDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<TaskErrorResponse> handleException(TaskNotFoundException e) {
        TaskErrorResponse response = new TaskErrorResponse(
                "Task with this id wasn't found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<TaskErrorResponse> handleException(TaskNotCreatedException e) {
        TaskErrorResponse response = new TaskErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
