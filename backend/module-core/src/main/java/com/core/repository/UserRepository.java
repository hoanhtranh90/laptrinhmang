/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.core.repository;

import com.core.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author DELL
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(
            value = "SELECT new User( u.userName,u.phoneNumber,u.email ) FROM User u WHERE LOWER(u.userName)=:userName and u.isDelete=:isDelete")
    List<User> checkAlreadyUsernameExsit(String userName, Long isDelete);

    @Query(
            value = "SELECT new User( u.userName,u.phoneNumber,u.email ) FROM User u WHERE LOWER(u.email)=:email and u.isDelete=:isDelete")
    List<User> checkAlreadyEmailExsit(String email,  Long isDelete);

    @Query(
            value = "SELECT new User( u.userName,u.phoneNumber,u.email ) FROM User u WHERE LOWER(u.phoneNumber)=:phoneNumber  and u.isDelete=:isDelete")
    List<User> checkAlreadyPhoneExsit(String phoneNumber,  Long isDelete);

    @Query(
            value = "SELECT u FROM User u WHERE LOWER(u.userName) =:userName  and u.isDelete=:isDelete")
    User findByUserName(String userName, Long isDelete);

    @Override
    Optional<User> findById(Long userId);

    User findByUserIdAndIsDelete(Long userId, Long isDelete);

    @Query(value = "SELECT u FROM User u WHERE u.userId=:userId and u.isDelete=:isDelete")
    User findByIdCustom(Long userId,  Long isDelete);

    @Query(value = "SELECT u  FROM User u, ViewUserRole r WHERE u.userId = r.userId  and u.isDelete=:isDelete "
            + "and (:keyword is null or (LOWER(u.userName) like :keyword or LOWER(u.fullName) like :keyword "
            + "or LOWER(u.phoneNumber) like :keyword "
            + "or LOWER(u.email) like :keyword) ) "
            //            + "AND (:createdDateFrom IS NULL OR  ( DATE_FORMAT(u.createdDate,'%Y-%m-%d') >= DATE_FORMAT(:createdDateFrom,'%Y-%m-%d'))) "
            //            + "AND (:createdDateTo IS NULL OR  ( DATE_FORMAT(u.createdDate,'%Y-%m-%d') <= DATE_FORMAT(:createdDateTo,'%Y-%m-%d'))) "
            + "and (:userName is null or (LOWER(u.userName) like :userName)) "
            + "and (:fullName is null or (LOWER(u.fullName) like :fullName)) "
            + "and (:roleCode is null or r.roleCode = :roleCode) "
            + "and (:phoneNumber is null or (LOWER(u.phoneNumber) like :fullName)) "
            + "and (:email is null or (LOWER(u.email) like :email)) ")
    Page<User> searchAllUser(Long isDelete, String keyword, String userName, String fullName, String phoneNumber, String email, String roleCode, Pageable pageable);

    @Query(
            value = "SELECT u FROM User u WHERE LOWER(u.email) =:email  and u.isDelete=:isDelete")
    User findByEmail(String email, Long isDelete);

    @Query("select u from User u where u.email = :email and u.isDelete=:isDelete")
    User existsByEmail(String email,  Long isDelete);
//    Page<User> searchAllUser(List<Integer> listStatus, String keyword, Date createdDateFrom,
//            Date createdDateTo, String userName, String fullName, String phoneNumber, String email);
}
