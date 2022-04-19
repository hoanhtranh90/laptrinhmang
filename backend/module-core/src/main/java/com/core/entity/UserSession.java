/**
 * Welcome developer friend. PC ospAdfilex-smartTeleSale-data UserSession.java 11:09:31 AM
 */
package com.core.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "user_session",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "session"})})
@Setter
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserSession extends Auditable<String> implements Serializable {

  private static final long serialVersionUID = -6998632842657040223L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "session_id")
  private Long sessionId;

  @Column(name = "session")
  private String session;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = true)
  private User user;

}
