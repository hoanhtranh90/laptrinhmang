package com.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "user")
@Setter
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User extends Auditable<String> implements Serializable {

    private static final long serialVersionUID = 6174016284429444079L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "phone_number"/* , unique = true */, length = 25)
    private String phoneNumber;

    @Column(name = "email"/* , unique = true */, length = 40)
    private String email;

    @Column(name = "username", unique = true, nullable = false, updatable = false, length = 25)
    private String userName;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "status", nullable = false)
    private Long status;
    
    @Column(name = "is_delete", columnDefinition = "bigint default 0")
    private Long isDelete;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "address", length = 255, columnDefinition = "nvarchar(255)")
    private String address;




    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Where(clause = "is_delete = 0")
    private List<UserRole> userRoleList;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    //one to many following
    @JsonIgnore
    @OneToMany(mappedBy = "follower", fetch = FetchType.EAGER)
    private List<Follow> followingList;

    @Transient
    private String checkDomain;

    @Transient
    private String checkPassword;

    public User(String phoneNumber, String email, String userName) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userName = userName;
    }

    public User(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public User(Long userId, String userName, String fullName) {
        this.userId = userId;
        this.userName = userName;
        this.fullName = fullName;
    }
    
    public User(Long userId, String username, Long status,
            Date createdDate) {
        super();
        this.userId = userId;
        this.userName = username;
        this.status = status;
        this.createdDate = createdDate;
    }

}
