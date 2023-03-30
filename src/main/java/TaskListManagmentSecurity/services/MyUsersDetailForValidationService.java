package TaskListManagmentSecurity.services;

import TaskListManagmentSecurity.models.User;
import TaskListManagmentSecurity.repositories.UserRepository;
import TaskListManagmentSecurity.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUsersDetailForValidationService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public MyUsersDetailForValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> person = userRepository.findByUsername(username);
        return person.map(MyUserDetails::new).orElse(null);
    }
}
