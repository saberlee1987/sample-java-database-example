package com.saber.sample_java_database_example.constants;

public enum OperationEnum {
    REGISTRATION_STUDENT("1"),
    INQUIRY_CAPACITY_STUDENT("2"),
    EDIT_STUDENT("3"),
    CANCEL_REGISTRATION_STUDENT("4"),
    SHOW_INFORMATION_STUDENT("5"),
    EXIT("0"),
    UNKNOWN("-1"),
    ;
    String value;

    OperationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OperationEnum getInstance(String value) {
        for (OperationEnum operationEnum : OperationEnum.values()) {
            if (operationEnum.getValue().equals(value))
                return operationEnum;
        }
        return OperationEnum.UNKNOWN;
    }
}
