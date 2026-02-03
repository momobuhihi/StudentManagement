package raisetech.Student.Management;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	private String name = "Yoshida Saori";
	private String age = "30";

	private Map<String, String> student = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/student")
	public Map<String, String> getStudent() {
		student.put("name", name);
		student.put("age", age);
		return student;
	}


	@PostMapping("/student")
	public String postStudent(
			@RequestParam String name,
			@RequestParam String age
	) {
		this.name = name;
		this.age = age;
		return "登録";
	}

}
