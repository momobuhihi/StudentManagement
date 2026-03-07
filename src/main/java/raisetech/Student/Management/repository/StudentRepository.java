package raisetech.Student.Management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;

/**
 * 受講生情報を扱うレポジトリ
 * <p>
 * 全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */
@Mapper
public interface StudentRepository {


  /**
   * 全件検索します。
   *
   * @return 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<Course> searchCourses();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(@Param("id") int id);

  @Insert("""
      INSERT INTO students
        (student_name, furigana, nickname, phone_number, mailaddress, region, age, gender, remark, is_deleted)
      VALUES
        (#{studentName}, #{furigana}, #{nickname}, #{phoneNumber}, #{mailaddress}, #{region}, #{age}, #{gender}, #{remark}, false)
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  void insertStudent(Student student);

  @Insert("""
      INSERT INTO students_courses
        (course_id, student_pk, course_name, start_date, end_date)
      VALUES
        (#{courseId}, #{studentPk}, #{courseName}, #{startDate}, #{endDate})
      """)
  void insertCourse(Course course);

  @Update("""
        UPDATE students
        SET student_name = #{studentName},
            furigana = #{furigana},
            nickname = #{nickname},
            phone_number = #{phoneNumber},
            mailaddress = #{mailaddress},
            region = #{region},
            age = #{age},
            gender = #{gender},
            remark = #{remark}
        WHERE id = #{id}
      """)
  void updateStudent(Student student);
}
