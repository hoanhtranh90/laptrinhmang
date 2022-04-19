/**
 * Welcome developer friend. PC ospAdfilex-smartTeleSale-service UserSessionService.java 2:21:07 PM
 */
package com.business.services;

import com.core.entity.UserSession;


/**
 * 
 * @author DELL
 */
public interface UserSessionService {

  /**
   * 
   * Save or update
   * 
   * @param session
   * @return UserSession
   */
  UserSession saveOrUpdate(UserSession session);

  /**
   * Delete
   * @param userId
   */
  void delete(Long userId);
}
