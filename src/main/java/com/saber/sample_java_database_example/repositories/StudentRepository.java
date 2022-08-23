package com.saber.sample_java_database_example.repositories;

import com.saber.sample_java_database_example.entities.StudentEntity;

public interface StudentRepository {
    StudentEntity registerNewStudent(StudentEntity entity);

    Integer capacityStudents(Integer year);
    Integer totalCapacityWithYear(Integer year);

    StudentEntity findByNationalCode(String nationalCode);

    StudentEntity findByStudentNumber(String studentNumber);

    StudentEntity findByNationalCodeAndStudentNumber(String nationalCode, String studentNumber);

    boolean cancelRegistration(String nationalCode, String studentNumber);

    boolean updateStudentInformation(StudentEntity entity, String nationalCode, String studentNumber);
}