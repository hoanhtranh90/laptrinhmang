package com.core.entity.view;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "v_user_role")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ViewUserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "role_code")
    private String roleCode;
}
