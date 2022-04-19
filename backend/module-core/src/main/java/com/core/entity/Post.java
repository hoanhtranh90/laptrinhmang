package com.core.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Post extends Auditable<String> implements Serializable {

private static final long serialVersionUID = 2679118992937555761L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @Column(name = "is_delete")
    private Long isDelete = 0L;



}
