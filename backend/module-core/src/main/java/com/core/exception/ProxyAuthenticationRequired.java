/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.exception;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author sangnk
 */
@ResponseStatus(value = HttpStatus.PROXY_AUTHENTICATION_REQUIRED)
public class ProxyAuthenticationRequired extends Exception implements Serializable {

    private static final long serialVersionUID = 1141685067515932795L;

    public ProxyAuthenticationRequired(final String message) {
        super(message);
    }
}
