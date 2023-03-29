package TaskListManagmentSecurity.util;

import TaskListManagmentSecurity.models.User;
import TaskListManagmentSecurity.services.MyUsersDetailForValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final MyUsersDetailForValidationService myUsersDetailForValidationService;

    @Autowired
    public UserValidator(MyUsersDetailForValidationService myUsersDetailForValidationService) {
        this.myUsersDetailForValidationService = myUsersDetailForValidationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User person = (User) target;
        if (myUsersDetailForValidationService.loadUserByUsername(person.getUsername()) != null)
        errors.rejectValue("username", "", "User with this username already exists");
    }
}
