package raisetech.Student.Management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import raisetech.Student.Management.data.Course;
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
  List<Student> search();

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return　受講生のコース情報（全件）
   */
  List<Course> searchCourses();

  /**
   * 在籍受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return　受講生
   */
  Student searchStudent(@Param("id") int id);

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentPk 受講生ID
   * @return　受講生IDに紐づく受講生コース情報
   */
  List<Course> searchStudentCourses(@Param("studentPk") int studentPk);

  /**
   * コース名からコースIDを取得します。
   *
   * @param courseName コース名
   * @return　コースID
   */
  Integer findCourseIdByName(String courseName);

  /**
   * 受講生を新規登録します。 　IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  void insertStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。　IDに関しては自動採番を行う。
   *
   * @param course 受講生コース情報
   */
  void insertCourse(Course course);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param course 受講生コース情報
   */
  void updateCourse(Course course);

  /**
   * 受講生を論理的削除します。
   *
   * @param id 受講生ID
   */
  void deleteStudent(int id);

  /**
   * 削除した受講生を復元します。
   *
   * @param id 受講生ID
   */
  void restoreStudent(int id);
}
