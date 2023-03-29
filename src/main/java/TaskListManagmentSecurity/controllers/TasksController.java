package TaskListManagmentSecurity.controllers;

import TaskListManagmentSecurity.dto.TaskDTO;
import TaskListManagmentSecurity.models.Task;
import TaskListManagmentSecurity.models.User;
import TaskListManagmentSecurity.security.MyUserDetails;
import TaskListManagmentSecurity.services.TasksService;
import TaskListManagmentSecurity.util.TaskErrorResponse;
import TaskListManagmentSecurity.util.TaskNotCreatedException;
import TaskListManagmentSecurity.util.TaskNotFoundException;
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

import javax.validation.Valid;
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

    @GetMapping()
    public String getTasks(Model model) {
        model.addAttribute("taskList",
                tasksService.findAll().stream()
                        .map(this::convertToTaskDTO)
                        .collect(Collectors.toList()));
        model.addAttribute("currentRole", getRole());
        return "tasks/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("taskList",
                tasksService.findAll().stream()
                        .map(this::convertToTaskDTO)
                        .collect(Collectors.toList()));
        model.addAttribute("currentRole", getRole());
        return "tasks/index";
    }


    @GetMapping("/{id}")
    public String getTask(@PathVariable("id") int id, Model model) {
        model.addAttribute("task", convertToTaskDTO(tasksService.findOne(id)));
        model.addAttribute("currentRole", getRole());
        return "tasks/show";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
        model.addAttribute("currentRole", getRole());
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

    private String getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails personDetails = (MyUserDetails) authentication.getPrincipal();
        User user = personDetails.getUser();
        return user.getRole();
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
