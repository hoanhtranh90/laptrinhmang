package com.core.model;

import lombok.Data;

@Data
public
class TypeDTO<T> {
    private String name;
    private T type;

    public TypeDTO(String name, T type) {
        this.name = name;
        this.type = type;
    }
}
