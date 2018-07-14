package com.spi.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Validator;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spi.VM.AlertConfigVM;
import com.spi.VM.ShortSchoolVM;
import com.spi.VM.ShortStudentVM;
import com.spi.VM.StudentVM;
import com.spi.config.ApplicationConfig;
import com.spi.dao.AbsentDAO;
import com.spi.dao.AbsentReasonDAO;
import com.spi.dao.PickDropRequestDAO;
import com.spi.dao.RouteDAO;
import com.spi.dao.SchoolDAO;
import com.spi.dao.StudentsDAO;
import com.spi.dao.UserDAO;
import com.spi.dao.UserSchoolDAO;
import com.spi.dao.WayPointDAO;
import com.spi.domain.Absent;
import com.spi.domain.AbsentReason;
import com.spi.domain.Address;
import com.spi.domain.AlertConfig;
import com.spi.domain.PickOrDropRequest;
import com.spi.domain.Route;
import com.spi.domain.School;
import com.spi.domain.Student;
import com.spi.domain.User;
import com.spi.domain.UserSchool;
import com.spi.domain.WayPoint;
import com.spi.service.BaseService;
import com.spi.service.StudentService;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.PickUpRequestModel;
import com.spi.user.api.StudentAssignment;
import com.spi.user.api.StudentRequest;
import com.spi.user.api.StudentRouteModel;
import com.spi.util.FcmMessageService;
import com.spi.util.SmsPrimaryService;

@Service("studentService")
@Transactional
public class StudentServiceImpl extends BaseService implements StudentService {

	private static final Logger LOG = LoggerFactory.getLogger(StudentServiceImpl.class);

	private StudentsDAO studentRepository;

	private RouteDAO routeRepository;

	private AbsentDAO absentRepository;

	private UserDAO userRepository;

	@Autowired
	private WayPointDAO wayPointRepository;

	@Autowired
	private UserSchoolDAO userschoolRepository;

	@Autowired
	private PickDropRequestDAO pickUpRepository;

	public WayPointDAO getWayPointRepository() {
		return wayPointRepository;
	}

	public void setWayPointRepository(WayPointDAO wayPointRepository) {
		this.wayPointRepository = wayPointRepository;
	}

	@Autowired
	private SchoolDAO schoolRepository;

	public SchoolDAO getSchoolRepository() {
		return schoolRepository;
	}

	@Autowired
	private AbsentReasonDAO absentReasonDAO;

	private ApplicationConfig applicationConfig;

	public StudentServiceImpl(Validator validator) {
		super(validator);
	}

	@Autowired
	public StudentServiceImpl(Validator validator, ApplicationConfig applicationConfig) {
		this(validator);
		this.applicationConfig = applicationConfig;
	}

	@Autowired
	public void setRouteRepository(RouteDAO routeRepository) {
		this.routeRepository = routeRepository;
	}

	@Autowired
	public void setSchoolRepository(SchoolDAO schoolRepository) {
		this.schoolRepository = schoolRepository;
	}

	public StudentRequest addStudentDetails(StudentRequest studentRequest, String userId) {

		validate(studentRequest);
		User user = userRepository.findByUuid(userId);

		Student newStudent = new Student(studentRequest, null, null);

		if (studentRequest.getWayPointId() != null && studentRequest.getWayPointId() > 0) {
			WayPoint wayPoint = wayPointRepository.findOne(studentRequest.getWayPointId());
			newStudent.setWayPoint(wayPoint);
		}

		School school = user.getUserSchool().getSchool();

		newStudent.setSchool(school);
		newStudent = studentRepository.save(newStudent);

		return studentRequest;
	}

	public StudentRequest updateStudentDetails(StudentRequest studentReq) {

		validate(studentReq);

		Student modifiedStudent = studentRepository.findOne(studentReq.getStudentId());
		modifiedStudent.setFirstName(studentReq.getFirstName());
		modifiedStudent.setLastName(studentReq.getLastName());
		modifiedStudent.setRegId(studentReq.getRegId());
		modifiedStudent.setStudentClass(studentReq.getStudentClass());
		modifiedStudent.setSection(studentReq.getSection());

		// StudentRouteXREF routeXref = modifiedStudent.getStudentRouteXREF();
		//
		// if(!modifiedStudent.getStudentRouteXREF().getRoute().getId().equals(studentReq.getRouteId())){
		// Route route = routeRepository.findRouteByPK(studentReq.getRouteId());
		// routeXref.setRoute(route);
		// }

		WayPoint wayPoint = modifiedStudent.getWayPoint();
		if (wayPoint == null || (wayPoint != null && !studentReq.getWayPointId().equals(wayPoint.getId()))) {
			modifiedStudent.setWayPoint(wayPointRepository.findOne(studentReq.getWayPointId()));
		}
		studentRepository.save(modifiedStudent);

		return studentReq;
	}

	public List<Student> getPresentStudents(String userId) {
		List<Student> student = studentRepository.getPresentStudents(userId);
		return student;
	}

	public List<Student> getUsersAllRouteStudents(String userId, long routeId) {
		List<Student> student = studentRepository.getUsersAllRouteStudents(routeId);
		return student;
	}

	public List<Student> getTodaysPresentStudents(String userId) {
		Date dateOnly = DateUtils.truncate(new Date(), Calendar.DATE);
		List<Student> student = studentRepository.getTodaysPresentStudents(userId, 'I', dateOnly);
		return student;
	}

	public List<Student> getTodaysAbsentStudents(String userId, Date selectDate) {
		Date dateOnly = DateUtils.truncate(selectDate, Calendar.DATE);
		List<Student> student = studentRepository.getTodaysPresentStudents(userId, 'A', dateOnly);
		return student;
	}

	@Autowired
	public void setUserRepository(UserDAO userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setStudentRepository(StudentsDAO studentRepository) {
		this.studentRepository = studentRepository;
	}

	private AlertConfigVM getMatchingAlert(List<AlertConfigVM> alertConfigs, int student_id) {
		for (AlertConfigVM alertConfig : alertConfigs) {

			if (alertConfig.getStudentId() == student_id) {
				return alertConfig;
			}
		}
		return null;

	}

	@Override
	public ExternalUser updateAlertSettings(String userId, List<AlertConfigVM> alertConfigs) {

		User user = userRepository.findByUuid(userId);

		Iterator<Student> iter = user.getStudent().iterator();
		while (iter.hasNext()) {
			Student student = iter.next();
			WayPoint wayPoint = student.getWayPoint();
			AlertConfig alertConfig = student.getAlertConfig();
			AlertConfigVM updatedAlert = getMatchingAlert(alertConfigs, student.getId().intValue());
			alertConfig.setPickStartTime(updatedAlert.getPickTime());
			alertConfig.setDropStartTime(updatedAlert.getDropTime());
			alertConfig.setActiveTime(updatedAlert.getActiveTime());
			alertConfig.setDays(updatedAlert.getDays());
			alertConfig.setAlerts(updatedAlert.getAlerts());
			wayPoint.setTimeDrop(updatedAlert.getDropTime());
			wayPoint.setTimePick(updatedAlert.getPickTime());
			studentRepository.save(student);
		}

		return new ExternalUser(user);
	}

	@Override
	public List<AlertConfigVM> getAlertSettings(String userId) {

		User user = userRepository.findByUuid(userId);
		List<AlertConfigVM> tobeSent = new ArrayList<>();

		Iterator<Student> iter = user.getStudent().iterator();
		while (iter.hasNext()) {
			Student student = iter.next();
			WayPoint wayPoint = student.getWayPoint();

			AlertConfig alertConfig = student.getAlertConfig();
			AlertConfigVM alertVM = new AlertConfigVM();
			alertVM.setActiveTime(alertConfig.getActiveTime());
			alertVM.setAlerts(alertConfig.getAlerts());
			alertVM.setDays(alertConfig.getDays());
			alertVM.setDropTime(wayPoint.getTimeDrop());
			alertVM.setPickTime(wayPoint.getTimePick());
			alertVM.setStudentId(student.getId().intValue());
			tobeSent.add(alertVM);
		}

		return tobeSent;
	}

	@Autowired
	public void setAbsentRepository(AbsentDAO absentRepository) {
		this.absentRepository = absentRepository;
	}

	@Override
	public List<Student> getStudentDetails(String userId) {
		return studentRepository.getStudentDetails(userId);

	}

	@Override
	public List<StudentVM> getStudentDetailsByFleet(long fleetId) {
		List<Student> students = routeRepository.findStudentsForVehicle(fleetId);
		return transformToStudentVM(students);
	}

	public void updateStudentPresence(String userId, StudentRequest studentRequest) {
		List<Student> student = studentRepository.getStudentDetails(userId);
		for (Student st : student) {
			updateStudentPresence(studentRequest, st.getId());
		}

	}

	public void updateStudentPresence(StudentRequest studentRequest, long studentId) {
		LOG.debug("inside updateStudentPresence");

		validateAbsentRequest(studentRequest);
		Student student = studentRepository.findOne(studentId);
		Absent absent = absentRepository.getPresenceDetailsByDate(studentId, studentRequest.getAbsentFromDate(),
				studentRequest.getAbsentToDate());
		User user = userRepository.findAdminByStudentId(studentId);
		String fromDate = new SimpleDateFormat("dd/MM/yyyy").format(studentRequest.getAbsentFromDate().getTime());
		String endDate = new SimpleDateFormat("dd/MM/yyyy").format(studentRequest.getAbsentToDate().getTime());

		String absentMessage = "";

		if (studentRequest.getPresentFlag() == 'I') {
			absentMessage = WordUtils.capitalizeFully("Prarent of student " + student.getFirstName()) + " "
					+ WordUtils.capitalizeFully(student.getLastName()) + " of route "
					+ student.getWayPoint().getRoute().getRouteName() + " is marked in from " + fromDate + " to "
					+ endDate;
		} else if (studentRequest.getPresentFlag() == 'A') {
			absentMessage = WordUtils.capitalizeFully("Prarent of student " + student.getFirstName()) + " "
					+ WordUtils.capitalizeFully(student.getLastName()) + " of route "
					+ student.getWayPoint().getRoute().getRouteName() + " is requesting leave from " + fromDate + " to "
					+ endDate;
		} else if (studentRequest.getPresentFlag() == 'O') {
			absentMessage = WordUtils.capitalizeFully("Prarent of student " + student.getFirstName()) + " "
					+ WordUtils.capitalizeFully(student.getLastName()) + " of route "
					+ student.getWayPoint().getRoute().getRouteName() + " is marked out from " + fromDate + " to "
					+ endDate;
		}

		SmsPrimaryService.getInstance(applicationConfig).sendMessage(user.getMobileNumber(), absentMessage);
		String fcmToken = user.getFcmtoken();
		FcmMessageService.getInstance(applicationConfig).sendMessage(fcmToken, absentMessage);

		if (absent == null) {
			absent = new Absent();
		}
		absent.setStudent(student);
		absent.setFromDate(studentRequest.getAbsentFromDate());
		absent.setToDate(studentRequest.getAbsentToDate());
		absent.setPresentFlag(studentRequest.getPresentFlag());

		AbsentReason ar = absent.getAbsentReason();
		long absentid = studentRequest.getAbsentReasonId();
		AbsentReason arr = absentReasonDAO.findOne(absentid);
		if (studentRequest.getPresentFlag() == 'I') {
			ar = absentReasonDAO.getAbsentReasonByReason("Marked In By Teacher");
		} else if (studentRequest.getPresentFlag() == 'A') {
			ar = absentReasonDAO.getAbsentReasonByReason(arr.getReason());
		} else if (studentRequest.getPresentFlag() == 'O') {
			ar = absentReasonDAO.getAbsentReasonByReason("Marked Out By Teacher");
		}

		if (ar == null) {
			ar = new AbsentReason();
		}

		absent.setAbsentReason(ar);
		absentRepository.save(absent);
	}

	private void validateAbsentRequest(StudentRequest studentRequest) {
		if (studentRequest.getPresentFlag() == 'A' && studentRequest.getAbsentReasonId() < 1) {
			throw new RuntimeException("Invalid Reason selected");
		}
		// List<Student> student =
		// studentRepository.getTodaysPresentStudents(studentRequest.getStudentId(),
		// 'A', studentRequest.getAbsentFromDate(), studentRequest.getAbsentToDate());

	}

	@Override
	public List<Student> getAllStudentDetails(String userId) {

		List<Student> students = studentRepository.getAllStudentDetailsAdmin(userId);
		return students;

	}

	@Override
	public StudentRouteModel getStudentRouteDetail(long studentId) {
		LOG.debug("inside getStudentRouteDetail");
		StudentRouteModel routeDetail = null;
		Student student = studentRepository.findOne(studentId);
		WayPoint wayPoint = student.getWayPoint();
		if (wayPoint != null && wayPoint.getRoute() != null) {
			routeDetail = new StudentRouteModel(studentId, wayPoint.getRoute().getId(), wayPoint.getId(),
					student.getSchool().getId());
			if (student.getSchool() != null) {
				routeDetail.setSchoolId(student.getSchool().getId());
			}
		} else {
			routeDetail = new StudentRouteModel(studentId, -1L, -1L,
					student.getSchool() != null ? student.getSchool().getId() : -1L);
		}

		return routeDetail;
	}

	public Student getStudent(long studentId) {
		return studentRepository.findOne(studentId);
	}

	// @Override
	// public void updateStudentRouteDetail(long studentId, long routeId, long
	// wayPointId, long schoolID) {
	// LOG.debug("inside updateStudentRouteDetail");
	// Student student = studentRepository.findOne(studentId);
	// StudentRouteXREF routeXREF = student.getStudentRouteXREF();
	// Route route = routeRepository.findOne(routeId);
	// WayPoint wayPoint = studentRepository.findWayPoint(wayPointId);
	// School school = schoolRepository.getSchoolDetailById(schoolID);
	// if (routeXREF != null) {
	// routeXREF.setRoute(route);
	//// routeXREF.setWayPoint(wayPoint);
	// routeXREF.setStudent(student);
	// student.setStudentRouteXREF(routeXREF);
	// student.setSchool(school);
	// studentRepository.save(student);
	// } else {
	// routeXREF = new StudentRouteXREF();
	// routeXREF.setRoute(route);
	//// routeXREF.setWayPoint(wayPoint);
	// routeXREF.setStudent(student);
	// student.setStudentRouteXREF(routeXREF);
	// student.setSchool(school);
	// studentRepository.save(student);
	// }
	// }

	@Override
	public List<StudentVM> getAllStudentDetailsAdmin(String userUuid) {
		LOG.debug("inside getAllStudentDetailsAdmin");

		List<StudentVM> studentVMList = new ArrayList<StudentVM>();

		List<Student> students = studentRepository.getAllStudentDetailsAdmin(userUuid);
		for (Student student : students) {

			StudentVM svm = new StudentVM();
			svm.setFirstName(student.getFirstName());
			svm.setLastName(student.getLastName());
			svm.setRegId(student.getRegId());
			svm.setSchoolId(student.getSchool().getId());
			svm.setSection(student.getSection());
			svm.setStudentClass(student.getStudentClass());
			svm.setStudentId(student.getId());
			svm.setStudentUUID(student.getUuid().toString());
			svm.setSchoolName(student.getSchool() != null ? student.getSchool().getName() : "Not Available");
			svm.setIdentifier(student.getIdentifier());
			svm.setIsApproved(student.getIsApproved());

			if (student.getWayPoint() != null) {
				svm.setRouteName(student.getWayPoint().getRoute().getRouteName());
				svm.setRouteId(student.getWayPoint().getRoute().getId());
				svm.setWayPointId(student.getWayPoint().getId());
				svm.setPickTimeSummer(student.getWayPoint().getTimePick());
				svm.setWinterPickup(student.getWayPoint().getTimePickWinter());
				svm.setDrop(student.getWayPoint().getTimeDrop());
				svm.setLattitude(student.getWayPoint().getLattitude());
				svm.setLongitude(student.getWayPoint().getLongitude());
			}

			if (student.getUser() != null) {
				svm.setEmail_address(student.getUser().getEmailAddress());
				svm.setMobile_number(student.getUser().getMobileNumber());
				svm.setUserId(student.getUser().getId());
				svm.setParentName(student.getUser().getFirstName() + " " + student.getUser().getLastName());
				if (student.getUser().getAddresses() != null && student.getUser().getAddresses().size() > 0) {
					Address address = (Address) student.getUser().getAddresses().get(0);
					svm.setAddress_line1(address.getAddressLine1());
					svm.setAddress_line2(address.getAddressLine2());
					svm.setCity(address.getCity());
					svm.setState(address.getState());
					svm.setCountry(address.getCountry());
					svm.setPinCode(address.getPinCode());
				}
			}

			studentVMList.add(svm);
		}
		return studentVMList;
	}

	@Override
	public List<ShortStudentVM> getStudentUnassignedWithParent(String userUuid) {
		LOG.debug("inside getStudentsInSchool");

		List<ShortStudentVM> studentVMList = new ArrayList<ShortStudentVM>();

		List<Student> students = studentRepository.getStudentUnassignedWithParent(userUuid);
		for (Student student : students) {

			ShortStudentVM svm = new ShortStudentVM();
			svm.setFirstName(student.getFirstName());
			svm.setLastName(student.getLastName());
			svm.setStudentId(student.getId());
			svm.setStudentUUID(student.getUuid().toString());

			studentVMList.add(svm);
		}
		return studentVMList;
	}

	@Override
	public void approveStudent(String studentUUID, String action) {
		Student student = studentRepository.findByUuid(studentUUID);
		if (student != null) {
			if ("approve".equals(action)) {
				student.setIsApproved('Y');
			} else if ("disapprove".equals(action)) {
				student.setIsApproved('N');
			}
		}
		studentRepository.save(student);
	}

	public Map<String, List<StudentVM>> getStudentReport(String userId, Date selectDate) {
		List<Student> presentStudent = getTodaysPresentStudents(userId);
		List<StudentVM> presentStudentVMList = new ArrayList<StudentVM>();

		for (Student student : presentStudent) {
			StudentVM svm = new StudentVM();
			svm.setFirstName(student.getFirstName());
			svm.setLastName(student.getLastName());
			svm.setRegId(student.getRegId());
			svm.setSchoolId(student.getSchool().getId());
			svm.setSection(student.getSection());
			svm.setStudentClass(student.getStudentClass());
			svm.setStudentId(student.getId());
			svm.setUserId(student.getUser().getId());
			svm.setStudentUUID(student.getUuid().toString());
			svm.setRouteName(student.getWayPoint().getRoute().getRouteName());

			if (student.getAbsent().size() > 0
			// && DateUtils.isSameDay(student.getAbsent().get(0).getDate(), (new
			// java.util.Date()))
			) {

				svm.setPresentFlag(student.getAbsent().get(0).getPresentFlag());
				svm.setAbsentMessage(student.getAbsent().get(0).getAbsentReason().getReason());
			} else {
				svm.setPresentFlag('N');
			}
			presentStudentVMList.add(svm);
		}

		List<Student> absentStudent = getTodaysAbsentStudents(userId, selectDate);
		List<StudentVM> absentStudentVMList = new ArrayList<StudentVM>();

		for (Student student : absentStudent) {
			StudentVM svm = new StudentVM();
			svm.setFirstName(student.getFirstName());
			svm.setLastName(student.getLastName());
			svm.setRegId(student.getRegId());
			svm.setSchoolId(student.getSchool().getId());
			svm.setSection(student.getSection());
			svm.setStudentClass(student.getStudentClass());
			svm.setStudentId(student.getId());
			svm.setUserId(student.getUser().getId());
			svm.setRouteName(student.getWayPoint().getRoute().getRouteName());
			svm.setStudentUUID(student.getUuid().toString());

			// We need reason for all absent records for select date
			// if (student.getAbsent().size() > 0 &&
			// DateUtils.isSameDay(student.getAbsent().get(0).getDate(), (new
			// java.util.Date()))) {
			if (student.getAbsent().size() > 0) {
				svm.setPresentFlag(student.getAbsent().get(0).getPresentFlag());
				svm.setAbsentMessage(student.getAbsent().get(0).getAbsentReason().getReason());
			} else {
				svm.setPresentFlag('N');
			}
			absentStudentVMList.add(svm);
		}

		Map<String, List<StudentVM>> studentList = new HashMap<String, List<StudentVM>>();
		studentList.put("presentStudent", presentStudentVMList);
		studentList.put("absentStudent", absentStudentVMList);
		return studentList;
	}

	private Boolean wasMarkedInMorning(List<Absent> absents) {
		if (absents == null)
			return false;

		Date now = new Date();
		for (Absent a : absents) {
			if (DateUtils.isSameDay(a.getFromDate(), now) && a.getPresentFlag() == 'I') {
				return true;
			}
		}

		return false;
	}

	public List<StudentVM> getTeacherStudentList(String userId, long routeId, Boolean isMorning) {
		List<Student> allRouteStudentList = getUsersAllRouteStudents(userId, routeId);
		List<StudentVM> studentList = new ArrayList<StudentVM>();
		for (Student student : allRouteStudentList) {
			if (!isMorning && !wasMarkedInMorning(student.getAbsent()))
				continue;
			StudentVM svm = new StudentVM();
			svm.setFirstName(student.getFirstName());
			svm.setLastName(student.getLastName());
			svm.setRegId(student.getRegId());
			svm.setSchoolId(student.getSchool().getId());
			svm.setSection(student.getSection());
			svm.setStudentClass(student.getStudentClass());
			svm.setStudentId(student.getId());
			svm.setUserId(student.getUser().getId());
			svm.setStudentUUID(student.getUuid().toString());

			if (student.getAbsent().size() > 0) {
				Date now = new Date();
				for (Absent a : student.getAbsent()) {
					if ((now.after(a.getFromDate()) && now.before(a.getToDate()))
							|| DateUtils.isSameDay(a.getFromDate(), now)) {
						svm.setAbsentFromDate(a.getFromDate());
						svm.setAbsentToDate(a.getToDate());
						svm.setAbsentid(a.getId());
						svm.setAbsentMessage(a.getMessage());
						svm.setPresentFlag(a.getPresentFlag());
						svm.setAbsentReason(a.getAbsentReason().getReason());
						break;
					}
				}
			}

			if (student.getAbsent().size() == 0 || svm.getAbsentid() == 0) {
				svm.setPresentFlag('N');
			}
			studentList.add(svm);
		}
		return studentList;
	}

	public List<StudentVM> transformToStudentVM(List<Student> studentDetailList) {
		List<StudentVM> studentList = new ArrayList<StudentVM>();
		for (Student student : studentDetailList) {
			StudentVM svm = new StudentVM();
			svm.setFirstName(student.getFirstName());
			svm.setLastName(student.getLastName());
			svm.setRegId(student.getRegId());
			svm.setSchoolId(student.getSchool().getId());
			svm.setSection(student.getSection());
			svm.setStudentClass(student.getStudentClass());
			svm.setStudentId(student.getId());
			svm.setUserId(student.getUser().getId());
			svm.setStudentUUID(student.getUuid().toString());
			studentList.add(svm);
		}
		return studentList;
	}

	@Override
	public List<ShortSchoolVM> getUnassignedSchool(String userUuid) {
		LOG.debug("inside getUnassignedSchool");

		List<ShortSchoolVM> schoolVMList = new ArrayList<ShortSchoolVM>();

		List<School> schools = schoolRepository.getUnassignedSchool();
		for (School school : schools) {

			ShortSchoolVM svm = new ShortSchoolVM();
			svm.setName(school.getName());
			svm.setDisplayAddress(school.getDisplayAddress());
			svm.setId(school.getId());

			schoolVMList.add(svm);

		}
		return schoolVMList;
	}

	@Override
	public List<StudentVM> getStudentRouteList(String routeId) {

		long route = Long.parseLong(routeId);
		System.out.println("routeId-----" + route);
		List<Student> studentDetailList = studentRepository.getUsersAllRouteStudents(route);

		List<StudentVM> studentList = new ArrayList<StudentVM>();
		for (Student student : studentDetailList) {
			StudentVM svm = new StudentVM();
			svm.setFirstName(student.getFirstName());
			svm.setLastName(student.getLastName());
			svm.setRegId(student.getRegId());
			svm.setSchoolId(student.getSchool().getId());
			svm.setSection(student.getSection());
			svm.setStudentClass(student.getStudentClass());
			svm.setStudentId(student.getId());
			if (student.getUser() != null) {
				svm.setUserId(student.getUser().getId());
			}
			svm.setStudentUUID(student.getUuid().toString());
			studentList.add(svm);
		}
		return studentList;

	}

	@Override
	public void addUpdateAssigment(StudentAssignment req, String userId) {

		try {
			DateFormat inputFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'z", Locale.ENGLISH);
			Date dropT = inputFormat.parse(req.getDropTime());

			Date pickT = inputFormat.parse(req.getPickTime());

			Route route = routeRepository.findRouteByPK(req.getRouteId());

			WayPoint wp = new WayPoint();
			wp.setLattitude(req.getLattitude());
			wp.setLongitude(req.getLongitude());
			wp.setTimeDrop(dropT);
			wp.setTimePick(pickT);

			wp.setName(req.getRouteName());
			wp.setDescription(req.getRouteName());
			wp.setRoute(route);

			wp = wayPointRepository.save(wp);
			for (Long studentId : req.getStudentId()) {
				Student student = studentRepository.findOne(studentId);
				if (student == null) {
					throw new RuntimeException("Student " + req.getStudentId() + " not found.");
				}
				student.setWayPoint(wp);
				studentRepository.save(student);
			}

		} catch (Exception e) {
			LOG.error("Error in method addUpdateAssigment " + e.getMessage());
		}
	}

	@Override
	public List<StudentVM> getStudentsByWayPoint(long waypointId) {
		List<Student> students = studentRepository.getStudentsByWayPoint(waypointId);
		List<StudentVM> svm = new ArrayList<StudentVM>();
		for (Student stud : students) {
			StudentVM svmm = new StudentVM();
			svmm.setFirstName(stud.getFirstName());
			svmm.setLastName(stud.getLastName());
			svm.add(svmm);
		}

		return svm;
	}
	public void sendPickUpRequest(PickUpRequestModel pick,String UserId){
		List<Student> st=studentRepository.getStudentDetails(UserId);
		for(Student student : st){
			
			sendPickUpRequest(pick ,student.getId());
		}
	}

	public void sendPickUpRequest(PickUpRequestModel pick ,long StudentId) {

		PickOrDropRequest pickrequest = new PickOrDropRequest();
		Student student = studentRepository.findOne(StudentId);
		pickrequest.setStudentId(student.getId());
		pickrequest.setSchoolId(student.getSchool().getId());
		pickrequest.setPickDropStatus('U');
		pickrequest.setPickOrDrop(pick.getPickOrDrop());
		pickrequest.setPickUpDate(pick.getPickUpTime());
		pickrequest.setPickUpReason(pick.getMessage());
		pickUpRepository.save(pickrequest);
		//---------- Send notification to School Admin ----------
		Long schoolId = student.getSchool().getId();
		User schoolAdmin = null;
		if(schoolId != null) schoolAdmin = schoolRepository.findSchoolAdminBySchool(schoolId);
		String message = "Parent of Student " + student.getFirstName() + " " + student.getLastName() + 
				" (" + student.getStudentClass() + ", " + student.getRegId() + ") requesting for " + 
				(pick.getPickOrDrop()=='p' ? "pickup" : "drop" )+ " at " + pick.getPickUpTime();
		FcmMessageService.getInstance(applicationConfig).sendMessage(schoolAdmin.getFcmtoken(), message);
	}

	public void aprovePickRequest(PickUpRequestModel pick) {
		PickOrDropRequest pickupRq = new PickOrDropRequest();
		pickupRq = pickUpRepository.getStudent(pick.getUuid());
		String message = null;
		if (pick.getRequestAction() == 'A') {
			pickupRq.setPickDropStatus('A');
			message = "Your " + (pick.getPickOrDrop()=='p' ? "pickup" : "drop" ) + " request has been approved";
		} else if (pick.getRequestAction() == 'R') {
			pickupRq.setPickDropStatus('R');
			message = "Sorry your " + (pick.getPickOrDrop()=='p' ? "pickup" : "drop" ) + " request has been rejected";
		}
		pickUpRepository.save(pickupRq);
		Student student = studentRepository.findOne(pickupRq.getStudentId());
		FcmMessageService.getInstance(applicationConfig).sendMessage(student.getUser().getFcmtoken(), message);
	}

	public void updatePickRequest(PickUpRequestModel request, String userId) {

		UserSchool us = userschoolRepository.getSchoolByUser(userId);
		List<PickOrDropRequest> pickupRq = pickUpRepository.getPickUpAlerts(us.getSchool().getId());
		for (PickOrDropRequest pickrq : pickupRq) {
			pickrq.setPickDropStatus('S');
			pickUpRepository.save(pickrq);
		}

	}

	public List<PickUpRequestModel> getpickAlerts(String userId) {
		List<PickUpRequestModel> pickmodel = new ArrayList<PickUpRequestModel>();
		List<Student> student = studentRepository.getStudentByUserUUID(userId);

		for (Student st : student) {
			List<PickOrDropRequest> pc = pickUpRepository.getStudentAbsentRecord(st.getId());

			for (PickOrDropRequest pick : pc) {
				PickUpRequestModel pickmdl = new PickUpRequestModel();
				pickmdl.setFirstName(st.getFirstName());
				pickmdl.setPickUpTime(pick.getPickUpDate());
				if (pick.getPickDropStatus() == 'U') {
					pickmdl.setStatus("Request Sent");
				} else if (pick.getPickDropStatus() == 'A') {
					pickmdl.setStatus("Approved");
				} else if (pick.getPickDropStatus() == 'S') {
					pickmdl.setStatus("Request Seen");
				} else if (pick.getPickDropStatus() == 'R') {
					pickmdl.setStatus("Request Rejected");
				}
				pickmdl.setMessage(pick.getPickUpReason());
				pickmodel.add(pickmdl);

			}

		}
		return pickmodel;
	}
	public List<StudentVM> getStudentListByClass(String userId ,String className){
		List<StudentVM> student= new ArrayList<StudentVM>();
		List<Student> studentOb=studentRepository.getStudentListByClass(userId, className);
		for (Student st :studentOb) {
			StudentVM studentvm=new StudentVM();
			studentvm.setFirstName(st.getFirstName());
			studentvm.setLastName(st.getLastName());
			studentvm.setStudentId(st.getId());
			studentvm.setParentName(st.getUser().getFirstName());
			
			studentvm.setUserId(st.getUser().getId());
			student.add(studentvm);
		}
		System.out.println(student);
		return student;
				
	}
}
