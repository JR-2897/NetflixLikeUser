package com.netflix.like.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.netflix.like.user.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {

}
