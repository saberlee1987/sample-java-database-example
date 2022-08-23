package com.saber.sample_java_database_example.handlers;

import com.saber.sample_java_database_example.entities.StudentEntity;
import com.saber.sample_java_database_example.services.StudentService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class StudentHandler {

    public static void showStudentInformation(Scanner scanner, StudentService studentService) {
        try {
            StudentEntity studentEntity = inquiryStudentInformation(scanner, studentService);
            System.out.println("Your student is " + studentEntity);
        } catch (Exception ex) {
            System.out.println("Error ====> " + ex.getMessage());
        }
    }

    public static void cancelRegistrationStudent(Scanner scanner, StudentService studentService) {

        try {
            String nationalCode = getNationalCode(scanner);
            String studentNumber = getStudentNumber(scanner);
            boolean cancelRegistration = studentService.cancelRegistration(nationalCode, studentNumber);
            if (cancelRegistration) {
                System.out.println("cancel Registration  Student successfully");
            } else {
                System.out.println("Sorry can not cancel Registration Student");
            }
        } catch (Exception ex) {
            System.out.println("Error ====> " + ex.getMessage());
        }
    }

    public static void editStudentInformation(Scanner scanner, StudentService studentService) {
        try {

            StudentEntity studentEntity = inquiryStudentInformation(scanner, studentService);
            System.out.println("Your student is " + studentEntity);
            System.out.println("Please enter new Information");
            getStudentInformationForUpdate(scanner, studentEntity);

            String nationalCode = studentEntity.getNationalCode();
            String studentNumber = studentEntity.getStudentNumber();
            boolean updateStudentInformation = studentService.updateStudentInformation(studentEntity, nationalCode, studentNumber);
            if (updateStudentInformation) {
                System.out.println("Update  Student successfully");
            } else {
                System.out.println("Sorry can not Update Student");
            }
        } catch (Exception ex) {
            System.out.println("Error ====> " + ex.getMessage());
        }
    }

    private static StudentEntity inquiryStudentInformation(Scanner scanner, StudentService studentService) {
        String nationalCode = getNationalCode(scanner);
        String studentNumber = getStudentNumber(scanner);
        return studentService.findByNationalCodeAndStudentNumber(nationalCode, studentNumber);
    }

    private static String getNationalCode(Scanner scanner) {
        System.out.print("Enter the nationalCode : ");
        return scanner.next();
    }

    private static String getStudentNumber(Scanner scanner) {
        System.out.print("Enter the studentNumber : ");
        return scanner.next();
    }

    public static void inquiryCapacity(Scanner scanner, StudentService studentService) {
        System.out.println("capacity ===> " + studentService.capacityStudents(year()));
    }

    public static void showMenu() {
        System.out.println("Please select from menu: ");
        System.out.println("1-Register new Student ");
        System.out.println("2-Inquiry capacity ");
        System.out.println("3-Edit student information ");
        System.out.println("4-cancel registration ");
        System.out.println("5-Inquiry student information");
        System.out.println("0-Exit");
        System.out.print("enter your operation : ");
    }

    private static StudentEntity getStudentInformation(Scanner scanner) {
        StudentEntity entity = new StudentEntity();
        System.out.print("Enter the firstName ");
        scanner.nextLine();
        entity.setFirstName(scanner.nextLine());
        System.out.print("Enter the lastName ");
        entity.setLastName(scanner.nextLine());
        System.out.print("Enter the score ");
        entity.setScore(scanner.nextDouble());
        System.out.print("Enter the studentNumber ");
        entity.setStudentNumber(scanner.next());
        System.out.print("Enter the nationalCode ");
        entity.setNationalCode(scanner.next());
        scanner.nextLine();
        System.out.print("Enter the address ");
        entity.setAddress(scanner.nextLine());
        entity.setYear(year());
        return entity;
    }

    private static void getStudentInformationForUpdate(Scanner scanner, StudentEntity entity) {
        System.out.print("Enter the firstName ");
        scanner.nextLine();
        String firstName = scanner.nextLine();
        if (firstName!=null && !firstName.equals(""))
            entity.setFirstName(firstName);
        System.out.print("Enter the lastName ");
        String lastName =scanner.nextLine();
        if (lastName!=null && !lastName.equals(""))
            entity.setLastName(lastName);
        System.out.print("Enter the score ");
        String score = scanner.next();
        if (score != null && !score.equals("") && score.matches("\\d+.\\d+")) {
            entity.setScore(Double.parseDouble(score));
        }
        scanner.nextLine();
        System.out.print("Enter the address ");
        String address = scanner.nextLine();
        if (address != null && !address.equals(""))
            entity.setAddress(address);
    }

    private static Integer year() {
        return LocalDateTime.now().getYear();
    }

    public static void registerNewStudent(Scanner scanner, StudentService studentService) {
        try {
            StudentEntity entity = getStudentInformation(scanner);
            StudentEntity registerNewStudent = studentService.registerNewStudent(entity);
            if (registerNewStudent!=null) {
                System.out.println("Register new Student successfully");
            } else {
                System.out.println("Sorry can not register new Student");
            }
        } catch (Exception ex) {
            System.out.println("Error ===> " + ex.getMessage());
        }
    }

}
