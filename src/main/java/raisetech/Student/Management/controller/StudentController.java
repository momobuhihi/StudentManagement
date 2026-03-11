package raisetech.Student.Management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.Student.Management.controller.converter.CourseConverter;
import raisetech.Student.Management.controller.converter.StudentConverter;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.service.StudentService;

// Controllerはリクエストの加工処理、入力チェックとかする場所

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;
  private CourseConverter courseconverter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter,
      CourseConverter courseconverter) {
    this.service = service;
    this.converter = converter;
    this.courseconverter = courseconverter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<Course> courses = service.searchCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, courses));
    return "studentList";
  }

  @GetMapping("/courseList")
  public String getCourseList(Model model) {
    List<Course> enrollments = service.searchCourseList();

    model.addAttribute("courseList", courseconverter.convertCourseDetails(enrollments));
    return "courseList";
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.register(studentDetail);
    return "redirect:/studentList";
  }

  @GetMapping("/editStudent/{id}")
  public String edit(@PathVariable int id, Model model) {
    StudentDetail detail = service.searchStudent(id);
    model.addAttribute("studentDetail", detail);
    return "studentEdit";
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return "redirect:/studentList";
  }
}
