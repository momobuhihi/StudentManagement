package raisetech.Student.Management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.Student.Management.data.CourseList;
import raisetech.Student.Management.data.Student;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の在籍検索を行います。
   *
   * @return 受講生在籍一覧
   */
  @Select("SELECT * FROM students WHERE is_deleted = FALSE")
  List<Student> search();

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return　受講生のコース情報（全件）
   */
  @Select("SELECT * FROM students_courses")
  List<CourseList> searchCourses();

  /**
   * 受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return　受講生
   */
  @Select("SELECT * FROM students WHERE id = #{id} AND is_deleted = FALSE")
  Student searchStudent(@Param("id") int id);

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentPk 受講生ID
   * @return　受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_pk = #{studentPk}")
  List<CourseList> searchStudentCourses(@Param("studentPk") int studentPk);

  @Select("SELECT id FROM courses WHERE course_name = #{courseName}")
  Integer findCourseIdByName(String courseName);

  /**
   * 受講生を新規登録します。 　IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  @Insert("""
      INSERT INTO students (student_name, furigana, nickname, phone_number, mailaddress, region, age, gender, remark, is_deleted)
      VALUES (#{studentName}, #{furigana}, #{nickname}, #{phoneNumber}, #{mailaddress}, #{region}, #{age}, #{gender}, #{remark}, false)
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  void insertStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。　IDに関しては自動採番を行う。
   *
   * @param course 受講生コース情報
   */
  @Insert("""
      INSERT INTO students_courses (course_id, student_pk, course_name, start_date, end_date)
      VALUES (#{courseId}, #{studentPk}, #{courseName}, #{startDate}, #{endDate})
      """)
  void insertCourse(CourseList course);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
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

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param course 受講生コース情報
   */
  @Update("UPDATE students_courses SET course_name = #{courseName} WHERE student_pk = #{studentPk}")
  void updateCourse(CourseList course);

  /**
   * 受講生を論理的削除します。
   *
   * @param id 受講生ID
   */
  @Update("UPDATE students SET is_deleted = TRUE WHERE id = #{id}")
  void deleteStudent(int id);
}
