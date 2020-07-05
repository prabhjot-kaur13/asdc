package CSCI5308.GroupFormationTool.User;

import CSCI5308.GroupFormationTool.ErrorHandling.PasswordException;
import CSCI5308.GroupFormationTool.ErrorHandling.UserAlreadyExistsException;
import CSCI5308.GroupFormationTool.Common.DomainConstants;
import CSCI5308.GroupFormationTool.Common.Injector;
import CSCI5308.GroupFormationTool.Security.IPasswordEncryptor;
import CSCI5308.GroupFormationTool.Password.IPasswordHistoryManager;
import CSCI5308.GroupFormationTool.Password.IPolicyService;

public class UserService implements IUserService {

    private IUserRepository userRepository;

    private IPasswordEncryptor encryptor;

    private IPolicyService policyService;

    private IPasswordHistoryManager passwordHistoryManager;

    @Override
    public boolean createUser(IUser user) {
        if (!checkForValues(user)) {
            return false;
        }

        policyService = Injector.instance().getPolicyService();
        String password = user.getPassword();
        String passwordSecurityError = policyService.passwordSPolicyCheck(password);

        if (passwordSecurityError != null) {
            throw new PasswordException(passwordSecurityError);
        }

        if (!(user.getPassword().equals(user.getConfirmPassword()))) {
            throw new PasswordException(DomainConstants.passwordsDontMatch);
        }

        userRepository = Injector.instance().getUserRepository();
        passwordHistoryManager = Injector.instance().getPasswordHistoryManager();
        boolean success = false;
        encryptor = Injector.instance().getPasswordEncryptor();

        user.setPassword(encryptor.encoder(user.getPassword()));
        IUser userByEmailId = userRepository.getUserByEmailId(user);

        if (userByEmailId == null) {
            success = userRepository.createUser(user);
            IUser userWithUserId = userRepository.getUserIdByEmailId(user);
            passwordHistoryManager.addPasswordHistory(userWithUserId, user.getPassword());
        } else {
            throw new UserAlreadyExistsException(DomainConstants.userAlreadyExists
            		.replace("[[emailId]]", user.getEmailId()));
        }
        return success;
    }

    @Override
    public boolean checkCurrentUserIsAdmin(String emailId) {
        userRepository = Injector.instance().getUserRepository();
        IUser adminDetails = userRepository.getAdminDetails();
        boolean outcome = adminDetails.getEmailId().equalsIgnoreCase(emailId);
        return outcome;
    }

    private boolean checkForValues(IUser user) {
        boolean outcome = true;

        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getEmailId().isEmpty()
                || user.getPassword().isEmpty() || user.getConfirmPassword().isEmpty()) {
            outcome = false;
        }
        return outcome;
    }

}
