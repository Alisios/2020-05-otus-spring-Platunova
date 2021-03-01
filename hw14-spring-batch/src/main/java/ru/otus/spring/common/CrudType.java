package ru.otus.spring.common;

public enum CrudType {

    CREATE_SUBSCRIPTION("save_subscription"),
    DELETE_SUBSCRIPTION("delete_subscription"),
    DELETE_USER("delete_user");

    private final String value;

    CrudType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
