package TaskListManagmentSecurity.services;

import TaskListManagmentSecurity.models.Task;
import TaskListManagmentSecurity.repositories.TasksRepository;
import TaskListManagmentSecurity.util.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TasksService {

    private final TasksRepository tasksRepository;

    @Autowired
    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public List<Task> findAll() {
        return tasksRepository.findAll();
    }

    public Task findOne(int id) {
        Optional<Task> foundTask = tasksRepository.findById(id);
        return foundTask.orElseThrow(TaskNotFoundException::new);
    }

    public void save(Task task) {
        enrichTask(task);
        tasksRepository.save(task);
    }

    private void enrichTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
    }

    public Task update(int id, Task task) {
        Optional<Task> optionalTask = tasksRepository.findById(id);
        if (optionalTask.isPresent()) {
            task.setId(id);
            return tasksRepository.save(task);
        } else throw new TaskNotFoundException();
    }

    public void delete(int id) {
        tasksRepository.deleteById(id);
    }

}
