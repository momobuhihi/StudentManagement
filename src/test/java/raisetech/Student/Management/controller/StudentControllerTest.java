package raisetech.Student.Management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.Student.Management.controller.converter.CourseConverter;
import raisetech.Student.Management.controller.converter.StudentConverter;
import raisetech.Student.Management.controller.handler.TestException;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.service.StudentService;

@WebMvcTest
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  @MockitoBean
  private StudentConverter converter;

  @MockitoBean
  private CourseConverter courseConverter;

  private Validator validator;

  @BeforeEach
  void setUp() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    when(service.searchStudentList()).thenReturn(List.of());
    when(service.searchCourseList()).thenReturn(List.of());
    when(converter.convertStudentDetails(List.of(), List.of())).thenReturn(List.of());

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
    verify(service, times(1)).searchCourseList();
    verify(converter, times(1)).convertStudentDetails(List.of(), List.of());
  }

  @Test
  void コース情報一覧検索が実行できて空のリストが返ってくること() throws Exception {
    when(service.searchCourseList()).thenReturn(List.of());
    when(courseConverter.convertCourseDetails(List.of())).thenReturn(List.of());

    mockMvc.perform(get("/courseList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchCourseList();
    verify(courseConverter, times(1)).convertCourseDetails(List.of());
  }

  @Test
  void 指定したIDの受講生詳細が取得できること() throws Exception {

    Student student = new Student();
    student.setId(1);
    student.setStudentName("山田太郎");

    StudentDetail studentDetail =
        new StudentDetail(student, List.of());

    when(service.searchStudent(1)).thenReturn(studentDetail);

    mockMvc.perform(get("/student/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id").value(1))
        .andExpect(jsonPath("$.student.studentName").value("山田太郎"));

    verify(service, times(1)).searchStudent(1);
  }

  @Test
  void 単体検索時にidが0だと400エラーになること() throws Exception {

    mockMvc.perform(get("/student/0"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).searchStudent(anyInt());
  }

  @Test
  void メールアドレスが不正な形式だとエラーになること() {
    Student student = new Student();
    student.setMailaddress("test");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations)
        .anyMatch(v -> v.getPropertyPath().toString().equals("mailaddress"));
  }

  @Test
  void 電話番号に数字以外が含まれるとエラーになること() {
    Student student = new Student();
    student.setPhoneNumber("090-1234-abcd");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations)
        .anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber"));
  }

  @Test
  void 電話番号の形式が不正だとエラーになること() {
    Student student = new Student();
    student.setPhoneNumber("0901234");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations)
        .anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber"));
  }

  @Test
  void 受講生登録が成功すること() throws Exception {
    String json = """
          {"student": {
            "studentName": "山田太郎",
            "furigana": "ヤマダタロウ",
            "nickname": "タロウ",
            "mailaddress": "test@example.com",
            "phoneNumber": "09012345678",
            "region": "東京",
            "gender": "男性"
            },　
            "studentsCourse": [{
              "courseName": "Javaコース"
            }]
          }
        """;

    when(service.register(any())).thenReturn(new StudentDetail());

    mockMvc.perform(post("/registerStudent")
            .contentType("application/json")
            .content(json))
        .andExpect(status().isOk());

    verify(service, times(1)).register(any());
  }

  @Test
  void メールアドレスの入力が不正な場合は400エラーになること() throws Exception {
    String json = """
        {
          "student": {
            "studentName": "山田太郎",
            "furigana": "ヤマダタロウ",
            "nickname": "タロウ",
            "mailaddress": "test",
            "phoneNumber": "09012345678",
            "region": "東京",
            "gender": "男性"
          },
          "studentsCourse": [
            {
              "courseName": "Javaコース"
            }
          ]
        }
        """;

    mockMvc.perform(post("/registerStudent")
            .contentType("application/json")
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void TestExceptionが発生したら400エラーになること() throws Exception {
    String json = """
        {
          "student": {
            "studentName": "山田太郎",
            "furigana": "ヤマダタロウ",
            "nickname": "タロウ",
            "mailaddress": "test@example.com",
            "phoneNumber": "09012345678",
            "region": "東京",
            "gender": "男性"
          },
          "studentsCourse": [
            {
              "courseName": "Javaコース"
            }
          ]
        }
        """;

    when(service.register(any())).thenThrow(new TestException("エラーです"));

    mockMvc.perform(post("/registerStudent")
            .contentType("application/json")
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 更新時にメールアドレスが不正だと400エラーになること() throws Exception {

    String json = """
        {
          "student": {
            "id": 999,
            "studentName": "山田太郎",
            "furigana": "ヤマダタロウ",
            "nickname": "タロウ",
            "mailaddress": "test",
            "phoneNumber": "09012345678",
            "region": "東京",
            "gender": "男性"
          },
          "studentsCourse": [
            {
              "id": 1,
              "studentPk": 999,
              "courseName": "Javaコース"
            }
          ]
        }
        """;

    mockMvc.perform(put("/updateStudent")
            .contentType("application/json")
            .content(json))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).updateStudent(any());
  }

  @Test
  void 更新時にコース名が空だと400エラーになること() throws Exception {
    String json = """
        {
          "student": {
            "id": 999,
            "studentName": "山田太郎",
            "furigana": "ヤマダタロウ",
            "nickname": "タロウ",
            "mailaddress": "test@example.com",
            "phoneNumber": "09012345678",
            "region": "東京",
            "gender": "男性"
          },
          "studentsCourse": [
            {
              "id": 1,
              "studentPk": 999,
              "courseName": ""
            }
          ]
        }
        """;

    mockMvc.perform(put("/updateStudent")
            .contentType("application/json")
            .content(json))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).updateStudent(any());
  }

  @Test
  void 受講生削除が成功すること() throws Exception {
    mockMvc.perform(delete("/deleteStudent/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("削除処理が成功しました。"));

    verify(service, times(1)).deleteStudent(1);
  }

  @Test
  void 削除時にidが0だと400エラーになること() throws Exception {
    mockMvc.perform(delete("/deleteStudent/0"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).deleteStudent(anyInt());
  }

  @Test
  void 削除時にidが負の数だと400エラーになること() throws Exception {
    mockMvc.perform(delete("/deleteStudent/-1"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).deleteStudent(anyInt());
  }

  @Test
  void 受講生復元が成功すること() throws Exception {
    mockMvc.perform(patch("/restoreStudent/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("復元処理が成功しました。"));

    verify(service, times(1)).restoreStudent(1);
  }

  @Test
  void 復元時にidが0だと400エラーになること() throws Exception {
    mockMvc.perform(patch("/restoreStudent/0"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).restoreStudent(anyInt());
  }

  @Test
  void 復元時にidが負の数だと400エラーになること() throws Exception {
    mockMvc.perform(patch("/restoreStudent/-1"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).restoreStudent(anyInt());
  }
}