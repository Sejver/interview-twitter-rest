package com.javalanguagezone.interviewtwitter.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class Tweet {
  protected static final int TWEET_MAX_LENGTH = 140;
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(length = TWEET_MAX_LENGTH)
  private String content;

  @ManyToOne(optional = false)
  private User author;

  public Tweet(String content, User author) {
    this.content = content;
    this.author = author;
  }

  public boolean isValid() {
    return author != null && content != null && !content.isEmpty() && content.length() <= TWEET_MAX_LENGTH;
  }
}

