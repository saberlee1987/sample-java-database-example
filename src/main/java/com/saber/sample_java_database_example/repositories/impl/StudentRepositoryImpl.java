package com.saber.sample_java_database_example.repositories.impl;

import com.saber.sample_java_database_example.entities.StudentEntity;
import com.saber.sample_java_database_example.repositories.StudentRepository;

import java.sql.*;
import java.util.ResourceBundle;

public class StudentRepositoryImpl implements StudentRepository {

    private String jdbcUrl;
    private String username;
    private String password;

    public StudentRepositoryImpl() {
        try {
            ResourceBundle application = ResourceBundle.getBundle("application");
            String driverClass = (String) application.getObject("database.driver");
            Class.forName(driverClass);
            jdbcUrl = (String) application.getObject("database.url");
            username = (String) application.getObject("database.username");
            password = (String) application.getObject("database.password");
        } catch (Exception ex) {
            System.out.println("Error ===> " + ex.getMessage());
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    @Override
    public StudentEntity registerNewStudent(StudentEntity entity) {
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement("INSERT INTO students (firstName, lastName, score, studentNumber, address, nationalCode,isDeleted,year) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setDouble(3, entity.getScore());
            preparedStatement.setString(4, entity.getStudentNumber());
            preparedStatement.setString(5, entity.getAddress());
            preparedStatement.setString(6, entity.getNationalCode());
            preparedStatement.setBoolean(7, entity.isDeleted());
            preparedStatement.setInt(8, entity.getYear());

            int executeUpdate = preparedStatement.executeUpdate();
            System.out.println("registerNewStudent add to table ====> " + executeUpdate);
            if (executeUpdate > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    entity.setId(resultSet.getInt(1));
                }
            }
            return entity;
        } catch (Exception ex) {
            System.out.println("Error registerNewStudent ===> " + ex.getMessage());
            return null;
        }
    }

    @Override
    public Integer capacityStudents(Integer year) {
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement("select count(*)  as cnt FROM students s where s.isDeleted=false and s.year=?;")
        ) {
            preparedStatement.setInt(1, year);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int cnt = resultSet.getInt("cnt");
                System.out.println("capacityStudents ===> " + cnt);
                return cnt;
            } else
                return -1;
        } catch (Exception ex) {
            System.out.println("Error capacityStudents ===> " + ex.getMessage());
            return -1;
        }
    }

    @Override
    public Integer totalCapacityWithYear(Integer year) {
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement("select c.cnt  FROM capacity c where  c.year=?;")
        ) {
            preparedStatement.setInt(1, year);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getInt("cnt");
            else
                return -1;
        } catch (Exception ex) {
            System.out.println("Error totalCapacityWithYear ===> " + ex.getMessage());
            return -1;
        }
    }

    @Override
    public StudentEntity findByNationalCode(String nationalCode) {
        return findByColumn(nationalCode, "nationalCode");
    }


    @Override
    public StudentEntity findByStudentNumber(String studentNumber) {
        return findByColumn(studentNumber, "studentNumber");
    }

    private StudentEntity findByColumn(String columnValue, String columnName) {
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement("SELECT * FROM students s where s.%s=?".format(columnName), ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE)
        ) {
            preparedStatement.setString(1, columnValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.last();
            if (resultSet.getRow() < 1) {
                return null;
            }
            resultSet.beforeFirst();
            StudentEntity entity = new StudentEntity();
            while (resultSet.next()) {
                entity = getStudentFromResultSet(resultSet);
            }
            return entity;
        } catch (Exception ex) {
            System.out.println(String.format("Error findByColumn %s: %s ===> %s", columnName, columnValue, ex.getMessage()));
            return null;
        }
    }

    private StudentEntity getStudentFromResultSet(ResultSet resultSet) throws SQLException {
        StudentEntity entity = new StudentEntity();
        entity.setId(resultSet.getInt("id"));
        entity.setFirstName(resultSet.getString("firstName"));
        entity.setLastName(resultSet.getString("lastName"));
        entity.setAddress(resultSet.getString("address"));
        entity.setDeleted(resultSet.getBoolean("isDeleted"));
        entity.setNationalCode(resultSet.getString("nationalCode"));
        entity.setScore(resultSet.getDouble("score"));
        entity.setStudentNumber(resultSet.getString("studentNumber"));
        entity.setYear(resultSet.getInt("year"));
        return entity;
    }


    @Override
    public StudentEntity findByNationalCodeAndStudentNumber(String nationalCode, String studentNumber) {
        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement("SELECT * FROM students s where s.studentNumber=? and s.nationalCode=?;", ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE)
        ) {
            preparedStatement.setString(1, studentNumber);
            preparedStatement.setString(2, nationalCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.last();
            if (resultSet.getRow() < 1) {
                return null;
            }
            resultSet.beforeFirst();
            StudentEntity entity = new StudentEntity();
            if (resultSet.next()) {
                entity = getStudentFromResultSet(resultSet);
            }
            return entity;
        } catch (Exception ex) {
            System.out.println(String.format("Error findByNationalCodeAndStudentNumber  studentNumber : %s , nationalCode: %s ===> %s", studentNumber, nationalCode, ex.getMessage()));
            return null;
        }
    }

    @Override
    public boolean cancelRegistration(String nationalCode, String studentNumber) {
        StudentEntity studentEntity = findByNationalCodeAndStudentNumber(nationalCode, studentNumber);
        if (studentEntity == null)
            return false;
        studentEntity.setDeleted(true);
        return updateStudentInformation(studentEntity, nationalCode, studentNumber);
    }

    @Override
    public boolean updateStudentInformation(StudentEntity entity, String nationalCode, String StudentNumber) {

        try (
                Connection connection = openConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement("UPDATE students s set s.firstName=? , s.lastName=? , s.nationalCode=? , s.studentNumber=? , s.score=? , s.address=?, s.isDeleted=? , s.year=? where s.nationalCode=? and s.studentNumber=?")
        ) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getNationalCode());
            preparedStatement.setString(4, entity.getStudentNumber());
            preparedStatement.setDouble(5, entity.getScore());
            preparedStatement.setString(6, entity.getAddress());
            preparedStatement.setBoolean(7, entity.isDeleted());
            preparedStatement.setInt(8, entity.getYear());
            preparedStatement.setString(9, entity.getNationalCode());
            preparedStatement.setString(10, entity.getStudentNumber());

            int executeUpdate = preparedStatement.executeUpdate();
            System.out.println("updateStudentInformation add to table ====> " + executeUpdate);
            return true;
        } catch (Exception ex) {
            System.out.println("Error updateStudentInformation ===> " + ex.getMessage());
            return false;
        }
    }
}
