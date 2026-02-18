package raisetech.Student.Management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import raisetech.Student.Management.controller.converter.StudentConverter;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.service.StudentService;

// Controllerはリクエストの加工処理、入力チェックとかする場所

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<Course> courses = service.searchCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, courses));
    return "studentList";
  }

  @GetMapping("/courseList")
  public List<Course> getCourseList() {
    return service.searchCourseList();
  }
}
