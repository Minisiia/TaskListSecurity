package TaskListManagmentSecurity.services;

import TaskListManagmentSecurity.models.User;
import TaskListManagmentSecurity.repositories.UserRepository;
import TaskListManagmentSecurity.util.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Transactional
@Service
public class AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(int id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElseThrow(TaskNotFoundException::new);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User update(int id, User user) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            user.setId(id);
            user.setPassword(optionalUser.get().getPassword());
            return userRepository.save(user);
        } else throw new Exception();

    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

}
