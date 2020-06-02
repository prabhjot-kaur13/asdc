package CSCI5308.GroupFormationTool.Service;

import java.util.ArrayList;

import CSCI5308.GroupFormationTool.Injector;
import CSCI5308.GroupFormationTool.AccessControl.ICourse;
import CSCI5308.GroupFormationTool.AccessControl.IUserCourses;
import CSCI5308.GroupFormationTool.AccessControl.IUserCoursesRepository;
import CSCI5308.GroupFormationTool.AccessControl.IUserCoursesService;

public class UserCoursesService implements IUserCoursesService {

	IUserCoursesRepository userCoursesRepository;

	@Override
	public ArrayList<IUserCourses> getRoleBasedCourses(String emailId) {

		userCoursesRepository = Injector.instance().getUserCoursesRepository();

		return userCoursesRepository.getRoleBasedCourses(emailId);

	}

	@Override
	public String getUserRoleByEmailId(String emailId) {
		
		userCoursesRepository = Injector.instance().getUserCoursesRepository();

		return userCoursesRepository.getUserRoleByEmailId(emailId);
	}
	
	@Override
	public ArrayList<ICourse> getStudentCourses(String emailId) {

		userCoursesRepository = Injector.instance().getUserCoursesRepository();

		return userCoursesRepository.getStudentCourses(emailId);

	}
	
	@Override
	public ArrayList<ICourse> getTACourses(String emailId) {

		userCoursesRepository = Injector.instance().getUserCoursesRepository();

		return userCoursesRepository.getTACourses(emailId);

	}
}