package com.saber.sample_java_database_example.entities;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import lombok.Data;

import java.io.Serializable;
@Data
public class StudentEntity implements Serializable {
    private Integer id;
    private String firstName;
    private String lastName;
    private Double score;
    private String studentNumber;
    private String address;
    private String nationalCode;
    private Integer year;
    private boolean isDeleted;

    @Override
    public String toString(){

        return new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
                .create()
                .toJson(this,StudentEntity.class);
    }
}