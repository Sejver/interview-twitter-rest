package com.javalanguagezone.interviewtwitter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@MappedSuperclass
public class Person {


  @Column(name="first_person")
  private String firstName;

  @Column(name="last_name")
  private String lastName;

}
