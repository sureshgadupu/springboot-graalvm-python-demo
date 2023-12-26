package dev.fullstackcode.graalvm.python.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    Long id;


    String name;


    List<String> skills;
}