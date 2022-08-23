package com.saber.sample_java_database_example.services;

import com.saber.sample_java_database_example.entities.StudentEntity;

public interface StudentService {

    StudentEntity registerNewStudent(StudentEntity entity);

    Integer capacityStudents(Integer year);

    StudentEntity findByNationalCode(String nationalCode);

    StudentEntity findByStudentNumber(String studentNumber);

    StudentEntity findByNationalCodeAndStudentNumber(String nationalCode, String studentNumber);

    boolean cancelRegistration(String nationalCode, String studentNumber);

    boolean updateStudentInformation(StudentEntity entity, String nationalCode, String studentNumber);
}
