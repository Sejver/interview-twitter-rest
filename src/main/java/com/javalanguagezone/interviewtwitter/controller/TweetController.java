package com.javalanguagezone.interviewtwitter.controller;

import com.javalanguagezone.interviewtwitter.controller.dto.ErrorMessage;
import com.javalanguagezone.interviewtwitter.domain.Tweet;
import com.javalanguagezone.interviewtwitter.domain.User;
import com.javalanguagezone.interviewtwitter.service.TweetService;
import com.javalanguagezone.interviewtwitter.service.TweetService.InvalidTweetException;
import com.javalanguagezone.interviewtwitter.service.TweetService.UnknownUsernameException;
import com.javalanguagezone.interviewtwitter.service.UserService;
import com.javalanguagezone.interviewtwitter.service.dto.TweetDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@Controller
@RequestMapping("tweets")
@Slf4j
public class TweetController {

  private TweetService tweetService;
  private UserService userService;
  public TweetController(TweetService tweetService, UserService userService) {
    this.tweetService = tweetService;
    this.userService = userService;
  }


  @PostMapping("/tweet")
  @ResponseStatus(CREATED)
  public String tweet(@Valid @ModelAttribute("tweet")  Tweet tweet, Principal principal, BindingResult result,
                      ModelMap modelMap) {
    if(result.hasErrors()){

      return "index";
    }

    if (!tweet.getContent().equals(null) && !tweet.getContent().equals("") && !tweet.getContent().isEmpty()) {
      tweetService.createTweet(tweet.getContent(), principal);
    }
    User user=userService.getUser(principal.getName());
    modelMap.addAttribute("tweets",tweetService.tweetsFromUser(principal.getName()));
    modelMap.addAttribute("user",user);
    modelMap.addAttribute("tweet",new Tweet());
    return "index";
  }

  @GetMapping("/")
  public String goToIndex(Model model){
    model.addAttribute("tweet",new Tweet());
    return "overview";
  }

  @GetMapping
  public String followingUsersTweets(Principal principal, Model model) {
    model.addAttribute("tweet",new Tweet());
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
