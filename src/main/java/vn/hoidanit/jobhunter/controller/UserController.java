package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {
     private final UserService userService;

     public UserController(UserService userService) {
          this.userService = userService;
     }
     
     @PostMapping("/user")
     public User createNewUser(@RequestBody User user) {
          User addedUser = this.userService.handleCreateUser(user);
          return addedUser;
     }

     @DeleteMapping("/user/{id}")
     public String deleteNewUser(@PathVariable("id") long id) {
          this.userService.deleteUser(id);
          return "hello thuyvan";
     }
}
