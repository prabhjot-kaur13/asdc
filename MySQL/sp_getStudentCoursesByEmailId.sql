USE `CSCI5308_22_DEVINT`;
DROP procedure IF EXISTS `CSCI5308_22_DEVINT`.`sp_getStudentCoursesByEmailId`;

DELIMITER $$
USE `CSCI5308_22_DEVINT`$$
CREATE DEFINER=`CSCI5308_22_DEVINT_USER`@`%` PROCEDURE `sp_getStudentCoursesByEmailId`(IN emailId VARCHAR(255))
BEGIN
	with course_info as (Select distinct course_id from role_assignment where user_id = 
	(select User_id from users where email=emailId)  and role_id = 3)
	Select * from course join course_info using (course_id);
END$$

DELIMITER ;

