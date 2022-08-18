package com.saber.sample_java_database_example.services.impl;

import com.saber.sample_java_database_example.entities.StudentEntity;
import com.saber.sample_java_database_example.exceptions.ResourceDuplicationException;
import com.saber.sample_java_database_example.exceptions.ResourceNotFoundException;
import com.saber.sample_java_database_example.repositories.StudentRepository;
import com.saber.sample_java_database_example.services.StudentService;

public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private Integer totalCapacityWithYear(int year) {
        return this.studentRepository.totalCapacityWithYear(year);
    }

    @Override
    public boolean registerNewStudent(StudentEntity entity) {

        System.out.println("Request for register new Student ===> " + entity);

        String nationalCode = entity.getNationalCode();
        String studentNumber = entity.getStudentNumber();
        if (nationalCode == null || nationalCode.trim().equals(""))
            return false;

        if (studentNumber == null || studentNumber.trim().equals(""))
            return false;

        if (this.studentRepository.findByNationalCodeAndStudentNumber(nationalCode, studentNumber) != null) {
            throw new ResourceDuplicationException(String.format("student with nationalCode %s And studentNumber %s already exist", nationalCode, studentNumber));
        }

        boolean registerNewStudent = this.studentRepository.registerNewStudent(entity);

        System.out.println("Response for register new Student ===> " + registerNewStudent);

        return registerNewStudent;
    }

    @Override
    public Integer capacityStudents(Integer year) {
        Integer capacity = totalCapacityWithYear(year);
        Integer capacityStudents = this.studentRepository.capacityStudents(year);
        return capacity - capacityStudents;
    }

    @Override
    public StudentEntity findByNationalCode(String nationalCode) {
        StudentEntity studentEntity = this.studentRepository.findByNationalCode(nationalCode);
        if (studentEntity == null) {
            throw new ResourceNotFoundException(String.format("student with nationalCode %s does not exist", nationalCode));
        }
        return studentEntity;
    }

    @Override
    public StudentEntity findByStudentNumber(String studentNumber) {
        StudentEntity studentEntity = this.studentRepository.findByStudentNumber(studentNumber);
        if (studentEntity == null) {
            throw new ResourceNotFoundException(String.format("student with studentNumber %s does not exist", studentNumber));
        }
        return studentEntity;
    }

    @Override
    public StudentEntity findByNationalCodeAndStudentNumber(String nationalCode, String studentNumber) {
        StudentEntity studentEntity = this.studentRepository.findByNationalCodeAndStudentNumber(nationalCode, studentNumber);
        if (studentEntity == null) {
            throw new ResourceNotFoundException(String.format("student with studentNumber %s and nationalCode : %s does not exist", studentNumber, nationalCode));
        }
        return studentEntity;
    }

    @Override
    public boolean cancelRegistration(String nationalCode, String studentNumber) {
        System.out.println(String.format("Request for cancelRegistration with nationalCode %s and studentNumber %s ", nationalCode, studentNumber));
        if (this.studentRepository.findByNationalCodeAndStudentNumber(nationalCode, studentNumber) == null) {
            throw new ResourceNotFoundException(String.format("student with nationalCode %s And studentNumber  %s does not exist", nationalCode, studentNumber));
        }
        boolean cancelRegistration = this.studentRepository.cancelRegistration(nationalCode, studentNumber);

        System.out.println(String.format("Request for cancelRegistration with nationalCode %s and studentNumber %s ===> %s", nationalCode, studentNumber, cancelRegistration));
        return cancelRegistration;
    }

    @Override
    public boolean updateStudentInformation(StudentEntity entity,
                                            String nationalCode,
                                            String studentNumber) {
        System.out.println(String.format("Request for updateStudentInformation with nationalCode %s and studentNumber %s with body %s",
                nationalCode, studentNumber, entity));
        StudentEntity studentEntity = this.studentRepository.findByNationalCodeAndStudentNumber(nationalCode, studentNumber);
        if (studentEntity == null) {
            throw new ResourceNotFoundException(String.format("student with nationalCode %s And studentNumber  %s does not exist", nationalCode, studentNumber));
        }

        studentEntity.setYear(entity.getYear());
        studentEntity.setDeleted(entity.isDeleted());
        studentEntity.setStudentNumber(entity.getStudentNumber());
        studentEntity.setScore(entity.getScore());
        studentEntity.setNationalCode(entity.getNationalCode());
        studentEntity.setAddress(entity.getAddress());
        studentEntity.setLastName(entity.getLastName());
        studentEntity.setFirstName(entity.getFirstName());
        studentEntity.setId(entity.getId());

        return this.studentRepository.updateStudentInformation(studentEntity, nationalCode, studentNumber);
    }
}