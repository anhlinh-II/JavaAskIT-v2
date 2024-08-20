package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
     private final UserRepository userRepository;

     private UserService(UserRepository userRepository) {
          this.userRepository = userRepository;
     }

     public User handleCreateUser(User user) {
          return this.userRepository.save(user);
     }

     public void handleDeleteUser(long userId) {
          this.userRepository.deleteById(userId);
     }

     public User fetchUserById(long userId) {
          Optional<User> optionalUser = this.userRepository.findById(userId);
          if (optionalUser.isPresent()) {
               return optionalUser.get();
          }
          return null;
     }

     public ResultPaginationDTO fetchAllUser(Pageable pageable) {
          Page<User> pageUser = this.userRepository.findAll(pageable);
          ResultPaginationDTO res = new ResultPaginationDTO();
          Meta mt = new Meta();

          mt.setPage(pageUser.getNumber());
          mt.setPageSize(pageUser.getSize());
          mt.setPage(pageUser.getTotalPages());
          mt.setTotal(pageUser.getTotalElements());

          res.setMeta(mt);
          res.setResult(pageUser.getContent());

          return res;
          
     }

     public User handleUpdateUser(User userUpdateRequest) {
          var id = userUpdateRequest.getId();
          var currentUser = this.fetchUserById(id);
          if (currentUser != null) {
               currentUser.setName(userUpdateRequest.getName());
               currentUser.setEmail(userUpdateRequest.getEmail());
               currentUser.setPassword(userUpdateRequest.getPassword());

               currentUser = this.userRepository.save(currentUser);
          }
          return currentUser;
     }

     public User handleGetUserByUsername(String username) {
          return this.userRepository.findByEmail(username);
     }
}
