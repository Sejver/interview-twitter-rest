package com.javalanguagezone.interviewtwitter.controller;

import com.javalanguagezone.interviewtwitter.domain.Tweet;
import com.javalanguagezone.interviewtwitter.domain.User;
import com.javalanguagezone.interviewtwitter.service.TweetService;
import com.javalanguagezone.interviewtwitter.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

  private static final String VIEWS_USER_CREATE = "createUser";

  private UserService userService;
  private TweetService tweetService;

  public UserController(UserService userService, TweetService tweetService) {
    this.userService = userService;
    this.tweetService = tweetService;
  }

  @GetMapping({"", "/", "/index", "/index.html"})
  public String index(Principal principal, Model model) {

   User user=userService.getUser(principal.getName());
   model.addAttribute("tweets",tweetService.tweetsFromUser(principal.getName()));
   model.addAttribute("user",user);
   model.addAttribute("tweet",new Tweet());
   return "index";
  }

  @GetMapping("/new")
  public String initCreationForm(Principal principal,Model model){

    model.addAttribute("user",new User());
    return VIEWS_USER_CREATE;
  }

  @PostMapping("/new")
  public String processCreationForm(@Valid @ModelAttribute(value = "user") User user, BindingResult result,Model model) {
    if (result.hasErrors()) {
      return VIEWS_USER_CREATE;
    } else {
      if(!userService.alreadyExists(user)){
       user.setId(user.getId());
       this.userService.save(user);
       }
       model.addAttribute("users",userService.getAllUsers());
       return "displayAllUsers";
    }
  }

  @GetMapping("/all")
  public String displayAllUsers(Model model){

    model.addAttribute("users",userService.getAllUsers());
    return "displayAllUsers";

  }

  @GetMapping("/overview")
  public String following(Principal principal,Model model) {
    model.addAttribute("tweet",new Tweet());
    model.addAttribute("user",userService.getUser(principal.getName()));
    model.addAttribute("following",userService.getUsersFollowing(principal).size());
    model.addAttribute("followers",userService.getUsersFollowers(principal).size());
    model.addAttribute("tweets",tweetService.tweetsFromUser(principal.getName()).size());

    return "overview";
  }
}
