/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.repository;

import com.core.entity.AttachRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sadfsafbhsaid
 */
@Repository
public interface AttachRelationRepository extends JpaRepository<AttachRelation, Long>{
    
}
