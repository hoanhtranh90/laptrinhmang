/**
 * Welcome developer friend. PC ospAdfilex-smartTeleSale-service UserSessionServiceImpl.java 2:21:15
 * PM
 */
package com.business.services.impl;

import com.business.services.UserSessionService;
import com.core.entity.UserSession;
import com.core.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author sangnk
 */
@Service
@Transactional
public class UserSessionServiceImpl implements UserSessionService {

  @Autowired
  private UserSessionRepository userSessionRepository;

  @Override
  public UserSession saveOrUpdate(UserSession session) {
    return userSessionRepository.save(session);
  }

  @Override
  public void delete(Long userId) {
    userSessionRepository.deleteByUserUserId(userId);
  }

}
