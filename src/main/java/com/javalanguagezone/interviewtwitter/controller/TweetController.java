package com.javalanguagezone.interviewtwitter.controller;

import com.javalanguagezone.interviewtwitter.controller.dto.ErrorMessage;
import com.javalanguagezone.interviewtwitter.service.TweetService;
import com.javalanguagezone.interviewtwitter.service.TweetService.InvalidTweetException;
import com.javalanguagezone.interviewtwitter.service.TweetService.UnknownUsernameException;
import com.javalanguagezone.interviewtwitter.service.dto.TweetDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.Collection;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@Controller
@RequestMapping("tweets")
@Slf4j
public class TweetController {

  private TweetService tweetService;

  public TweetController(TweetService tweetService) {
    this.tweetService = tweetService;
  }

  @PostMapping("/")
  @ResponseStatus(CREATED)
  public String tweet(@ModelAttribute("tweet") @Valid @RequestBody String tweet, Principal principal, BindingResult result) {
    if(result.hasErrors()){
      return "index";
    }
       tweetService.createTweet(tweet, principal);

    return "index";
  }

  @GetMapping
  public String followingUsersTweets(Principal principal, Model model) {
    model.addAttribute("tweets", tweetService.followingUsersTweets(principal));
    return "tweets";
  }

  @GetMapping(value = "{username}")
  public Collection<TweetDTO> tweetsFromUser(@PathVariable String username) {
    return tweetService.tweetsFromUser(username);
  }

  @ExceptionHandler
  @ResponseStatus(BAD_REQUEST)
  public ErrorMessage handleUnknownUsernameException(UnknownUsernameException e){
    log.warn("", e);
    return new ErrorMessage(String.format("Unknown user '%s'", e.getUsername()));
  }

  @ExceptionHandler
  @ResponseStatus(BAD_REQUEST)
  public ErrorMessage handleInvalidTweetException(InvalidTweetException e){
    log.warn("", e);
    return new ErrorMessage("We're unable to accept tweet");
  }
}
