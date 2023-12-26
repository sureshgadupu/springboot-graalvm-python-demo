import polyglot
import numpy as np
from typing import List
import java

Employee = java.type("dev.fullstackcode.graalvm.python.model.Employee")
Project = java.type("dev.fullstackcode.graalvm.python.model.Project")

class WelcomeServiceImpl:
    def hello(self, txt):
        return  "Hello "+ txt
    def recommendEmployees(self,project: Project, employees: List[Employee]) -> List[Employee]:
            project_vector = np.array(list(map(lambda skill: 1, project.skills())))

            employee_vectors = {
                employee: self.to_vector(employee.getSkills(), project.skills())
                for employee in employees
            }

            distances = {
                employee: np.linalg.norm(project_vector - skill_vector)
                for employee, skill_vector in employee_vectors.items()
            }

            ordered = dict(sorted(distances.items(), key=lambda distance: distance[1]))

            return list(ordered.keys())


    def to_vector(self,employee_skills, searched_skills):
        return np.array(list(map(lambda skill: int(skill in employee_skills), searched_skills)))


# We export the PyHello class to Java as our explicit interface with the Java side
polyglot.export_value("WelcomeServiceImpl", WelcomeServiceImpl)