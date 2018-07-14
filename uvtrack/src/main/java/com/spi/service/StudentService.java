package com.spi.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.spi.VM.AlertConfigVM;
import com.spi.VM.ShortSchoolVM;
import com.spi.VM.ShortStudentVM;
import com.spi.VM.StudentVM;
import com.spi.VM.WayPointVM;
import com.spi.domain.Student;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.PickUpRequestModel;
import com.spi.user.api.StudentAssignment;
import com.spi.user.api.StudentRequest;
import com.spi.user.api.StudentRouteModel;

@Transactional
public interface StudentService {

	public StudentRequest addStudentDetails(StudentRequest request, String userId);
	
	public StudentRequest updateStudentDetails(StudentRequest request);

	public List<Student> getPresentStudents(String userId);

	public List<Student> getUsersAllRouteStudents(String userId, long routeId);

	public List<Student> getTodaysPresentStudents(String userId);

	public List<Student> getTodaysAbsentStudents(String userId, Date selectDate);

	public List<Student> getStudentDetails(String userId);
	
	public List<StudentVM> getStudentDetailsByFleet(long fleetId);

    public void updateStudentPresence(StudentRequest student ,long studentId);
    
    public void sendPickUpRequest(PickUpRequestModel pick,String UserId);
    
    public void sendPickUpRequest(PickUpRequestModel pick,long StudentId);
    
    public void aprovePickRequest(PickUpRequestModel pick);
    public void updatePickRequest(PickUpRequestModel request ,String userId);
    
	public void updateStudentPresence(String userId ,StudentRequest student);

	public List<Student> getAllStudentDetails(String userId);
	
	public List<PickUpRequestModel> getpickAlerts(String userId);

	public StudentRouteModel getStudentRouteDetail(long studentId);

//	public void updateStudentRouteDetail(long studentId, long routeId, long wayPointId, long schoolID);

	public List<StudentVM> getAllStudentDetailsAdmin(String userUuid);
	
	public List<StudentVM> transformToStudentVM(List<Student> studentDetailList);
	
	public List<ShortStudentVM> getStudentUnassignedWithParent(String userUuid);

	public void approveStudent(String studentUUID, String action);

	public Student getStudent(long studentId);
	
	public Map<String, List<StudentVM>> getStudentReport(String userId, Date selectDate);
	
	public List<StudentVM> getTeacherStudentList(String userId, long routeId, Boolean isMorning);

    public ExternalUser updateAlertSettings(String userId, List<AlertConfigVM> alertConfigs);
    
    public List<AlertConfigVM> getAlertSettings(String userId);  
    
    public List<ShortSchoolVM> getUnassignedSchool(String userUuid);
    
    public List<StudentVM> getStudentRouteList(String routeId);
    
    public List<StudentVM> getStudentListByClass(String userId ,String className);

	public void addUpdateAssigment(StudentAssignment req, String userId);
	 public List<StudentVM> getStudentsByWayPoint(long waypointId);
}
