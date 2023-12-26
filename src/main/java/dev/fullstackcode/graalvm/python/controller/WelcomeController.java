package dev.fullstackcode.graalvm.python.controller;

import dev.fullstackcode.graalvm.python.model.Employee;
import dev.fullstackcode.graalvm.python.model.Project;
import dev.fullstackcode.graalvm.python.service.WelcomeService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class WelcomeController {

    WelcomeService welcomeService;

    public WelcomeController(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return welcomeService.hello(name);
    }


    @PostMapping("/recommendation")
    public List<Employee> getRecommendation(@RequestBody Project project) {

        return welcomeService.recommendEmployees(project,getAllEmployees());
    }

    private Iterable<Employee> getAllEmployees() {
        List<Employee> emplIst = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Employee emp = new Employee();
            System.out.print("Emp Id " + i);
            emp.setId(Long.valueOf(i));
            emp.setName("test" + i);
            List skills = getRandomSkills(4);
            System.out.println(" skills " + skills);
            emp.setSkills(skills);
            emplIst.add(emp);
        }
        return emplIst;

    }

    public static <T> List<T> getRandomSkills(int newSize) {
        List skills = new ArrayList(List.of("C", "C++", "Java", "SQL", "React", "Python", "Data Science", "Vue", "Next JS", "Spring Boot", "AWS", "Azure", "Google Cloud"));     Collections.shuffle(skills);
        return skills.subList(0, newSize);
    }
}
