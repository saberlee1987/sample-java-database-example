package com.saber.sample_java_database_example.main;

import com.saber.sample_java_database_example.constants.OperationEnum;
import com.saber.sample_java_database_example.entities.StudentEntity;
import com.saber.sample_java_database_example.repositories.StudentRepository;
import com.saber.sample_java_database_example.repositories.impl.StudentRepositoryImpl;
import com.saber.sample_java_database_example.services.StudentService;
import com.saber.sample_java_database_example.services.impl.StudentServiceImpl;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;

import static com.saber.sample_java_database_example.handlers.StudentHandler.*;

public class MainApp {
    public static void main(String[] args) {
        StudentRepository studentRepository = new StudentRepositoryImpl();
        StudentService studentService = new StudentServiceImpl(studentRepository);
        Scanner scanner = new Scanner(System.in);
        start(studentService, scanner);
    }

    private static void start(StudentService studentService, Scanner scanner) {
        showMenu();
        String choice = scanner.next();
        OperationEnum operationEnum = OperationEnum.getInstance(choice);
        switch (operationEnum) {
            case REGISTRATION_STUDENT:
                registerNewStudent(scanner, studentService);
                break;
            case INQUIRY_CAPACITY_STUDENT:
                inquiryCapacity(scanner, studentService);
                break;
            case EDIT_STUDENT:
                editStudentInformation(scanner, studentService);
                break;
            case CANCEL_REGISTRATION_STUDENT:
                cancelRegistrationStudent(scanner, studentService);
                break;
            case SHOW_INFORMATION_STUDENT:
                showStudentInformation(scanner, studentService);
                break;
            case EXIT: {
                System.out.println("good bye");
                System.exit(0);
                break;
            }
            default: {
                System.out.println("your operation invalid.");
                System.out.println("Please try Again!!");
                break;
            }
        }
        System.out.println();
        start(studentService, scanner);
    }
}