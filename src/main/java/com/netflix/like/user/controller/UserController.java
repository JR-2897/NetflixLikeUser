package com.netflix.like.user.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.netflix.like.user.model.User;
import com.netflix.like.user.model.User.UserStatus;
import com.netflix.like.user.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepo;

	RestTemplate restTemplate = new RestTemplate();

	String urlUser = "http://localhost:8081/";

	@PostMapping("/users")
	public ResponseEntity<List<User>> getAllUser(@RequestBody int idUser) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		Object obj = restTemplate.exchange(urlUser + "user/status/" + idUser, HttpMethod.GET, request, Object.class)
				.getBody();
		if (Objects.isNull(obj)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (obj.equals("ADMIN")) {
			return ResponseEntity.ok(userRepo.findAll());
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/user/{id}")
	public ResponseEntity<User> getUserById(@RequestBody int idUser, @PathVariable int id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		Object obj = restTemplate.exchange(urlUser + "user/status/" + idUser, HttpMethod.GET, request, Object.class)
				.getBody();
		if (Objects.isNull(obj)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (obj.equals("ACTIVE")) {
			Optional<User> resultRequest = userRepo.findById(id);
			if (resultRequest != null) {
				return ResponseEntity.ok(resultRequest.get());
			}
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/user/status/{id}")
	public String getUserStatusById(@PathVariable int id) {
		Optional<User> resultRequest = userRepo.findById(id);
		if (resultRequest != null) {
			User user = resultRequest.get();
			return user.getStatus().name().toString();
		}
		return null;
	}

	@PostMapping("/add/user")
	public ResponseEntity<Object> addUserToDatabase(@RequestBody User userToAdd) {
		User userAdded = userRepo.save(userToAdd);
		if (Objects.isNull(userAdded)) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userAdded.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/update/user/{id}")
	public ResponseEntity<User> updateUserToDatabase(@RequestBody int idUser, @PathVariable int id,
			@RequestBody User updateUser) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		Object obj = restTemplate.exchange(urlUser + "user/status/" + idUser, HttpMethod.GET, request, Object.class)
				.getBody();
		if (Objects.isNull(obj)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (obj.equals("ACTIVE")) {
			User userUpdate = userRepo.save(updateUser);
			if (!Objects.isNull(userUpdate)) {
				return ResponseEntity.ok(userUpdate);
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/delete/user/{id}")
	public ResponseEntity<User> deleteUserToDatabase(@RequestBody int idUser,@PathVariable int id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		Object obj = restTemplate.exchange(urlUser + "user/status/" + idUser, HttpMethod.GET, request, Object.class)
				.getBody();
		if (Objects.isNull(obj)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (obj.equals("ACTIVE")) {
			Optional<User> resultRequest = userRepo.findById(id);
			User userToDelete = null;
			if (resultRequest != null) {
				userToDelete = resultRequest.get();
			}
			userToDelete.setStatus(UserStatus.REMOVED);
			User user = userRepo.save(userToDelete);
			if (!Objects.isNull(user)) {
				return ResponseEntity.ok(user);
			}
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

}
