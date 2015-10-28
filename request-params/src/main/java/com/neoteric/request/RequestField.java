package com.neoteric.request;

public class RequestField implements RequestObject {

    private final String fieldName;

    private RequestField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static RequestField of(String fieldName) {
        return new RequestField(fieldName);
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public RequestObjectType getType() {
        return RequestObjectType.FIELD;
    }

    @Override
    public String toString() {
        return "[" + fieldName + "]";
    }
}
