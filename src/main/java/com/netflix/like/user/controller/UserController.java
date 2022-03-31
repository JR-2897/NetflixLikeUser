package com.netflix.like.user.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.netflix.like.user.model.User;
import com.netflix.like.user.model.User.UserStatus;
import com.netflix.like.user.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepo;
	
	@GetMapping("/users")
	public List<User> getAllUser() {
		return userRepo.findAll();
	}
	
	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable int id) {
		Optional<User> resultRequest = userRepo.findById(id);
		if(resultRequest != null)
		{
			return resultRequest.get();
		}
		
		return null;
	}
	
	@PostMapping("/add/user")
	public ResponseEntity<Object> addUserToDatabase(@RequestBody User userToAdd) {
		User userAdded = userRepo.save(userToAdd);
		if(Objects.isNull(userAdded)) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(userAdded.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/update/user/{id}")
	public User updateUserToDatabase(@PathVariable int id, @RequestBody User updateUser) {
		User userToUpdate = userRepo.save(updateUser);
		if(!Objects.isNull(userToUpdate))
		{
			return userToUpdate;
		}
		return null;
	}
	
	@PutMapping("/delete/user/{id}")
	public User deleteUserToDatabase(@PathVariable int id) {
		Optional<User> resultRequest = userRepo.findById(id);
		User userToDelete = null;
		if(resultRequest != null)
		{
			userToDelete = resultRequest.get();
		}
		userToDelete.setStatus(UserStatus.REMOVED);
		User user = userRepo.save(userToDelete);
		if(!Objects.isNull(user))
		{
			return user;
		}
		return null;
	}

}
