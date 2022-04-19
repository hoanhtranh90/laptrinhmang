package com.business.authencation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 *
 * @author sangnk
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {

  @JsonProperty(value = "access_token")
  private String accessToken;
  
}
