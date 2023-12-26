package dev.fullstackcode.graalvm.python.service;



import dev.fullstackcode.graalvm.python.model.Employee;
import dev.fullstackcode.graalvm.python.model.Project;

import java.util.List;

public interface WelcomeService {

    public  String hello(String name);

    public List<Employee> recommendEmployees(
            Project project,
            Iterable<Employee> employees
    );

}
