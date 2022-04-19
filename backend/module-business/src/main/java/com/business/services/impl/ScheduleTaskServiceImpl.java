/**
 * Welcome developer friend. PC ospAdfilex-smartTeleSale-service ScheduleTaskServiceImpl.java
 * 4:17:28 PM
 */
package com.business.services.impl;

import com.business.services.ScheduleTaskService;
import com.core.config.ApplicationConfig.MessageSourceVi;
import com.core.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author DELL
 */
@Component
@EnableAsync
@Slf4j
@Transactional
public class ScheduleTaskServiceImpl implements ScheduleTaskService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSourceVi messageSourceVi;


}
