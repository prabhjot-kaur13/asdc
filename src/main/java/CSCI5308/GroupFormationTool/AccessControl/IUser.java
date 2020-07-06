package CSCI5308.GroupFormationTool.AccessControl;

public interface IUser {

    long getId();

    void setId(long id);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getBannerId();

    void setBannerId(String bannerId);

    String getEmailId();

    void setEmailId(String emailId);

    String getPassword();

    void setPassword(String password);

    String getConfirmPassword();

    void setConfirmPassword(String confirmPassword);

	boolean createUser(IUser user);

	boolean checkCurrentUserIsAdmin(String emailId);

}
