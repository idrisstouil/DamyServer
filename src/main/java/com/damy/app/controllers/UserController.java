package com.damy.app.controllers;

import java.util.ArrayList;
import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.damy.app.exceptions.UserException;
import com.damy.app.requests.UserRequest;
import com.damy.app.responses.ErrorMessages;
import com.damy.app.responses.UserResponse;
import com.damy.app.services.UserService;
import com.damy.app.shared.dto.UserDto;

@CrossOrigin(origins="*")
@RestController
@RequestMapping(path="/users",produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping(path="/{id}")
	public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
		
		UserDto userDto = userService.getUserByUserId(id);
		
		UserResponse userResponse = new UserResponse();
		
		BeanUtils.copyProperties(userDto, userResponse);
		
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}
	
	
	@GetMapping()
	public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(value="page", defaultValue = "1") int page,@RequestParam(value="limit", defaultValue = "4")  int limit ,@RequestParam(value="search", defaultValue = "") String search,@RequestParam(value="status", defaultValue = "1") int status) {
		
		List<UserResponse> usersResponse = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page, limit, search, status);
		
		for(UserDto userDto: users) {
			
			ModelMapper modelMapper = new ModelMapper();
			UserResponse userResponse =  modelMapper.map(userDto, UserResponse.class);
			
			usersResponse.add(userResponse);
		}
		
		return new ResponseEntity<List<UserResponse>>(usersResponse, HttpStatus.OK);
	}
	
	@GetMapping(path="/all")
	public ResponseEntity<List<UserResponse>> getAllUsersAll() {
		
		List<UserResponse> usersResponse = new ArrayList<>();
		
		List<UserDto> users = userService.getAllUsers();
		
		for(UserDto userDto: users) {
			
			ModelMapper modelMapper = new ModelMapper();
			UserResponse userResponse =  modelMapper.map(userDto, UserResponse.class);
			
			usersResponse.add(userResponse);
		}
		
		return new ResponseEntity<List<UserResponse>>(usersResponse, HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) throws Exception {
		
		if(userRequest.getFirstName().isEmpty()) throw new UserException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userRequest, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userRequest, UserDto.class);
		
		UserDto createUser = userService.createUser(userDto);
		
		UserResponse userResponse =  modelMapper.map(createUser, UserResponse.class);
		
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.CREATED);
		
		
	}
	
	@PutMapping(path="/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userRequest, userDto);
		
		UserDto updateUser = userService.updateUser(id, userDto);
		
		UserResponse userResponse = new UserResponse();
		
		BeanUtils.copyProperties(updateUser, userResponse);
		
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.ACCEPTED);
	}
	
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable String id) {
		
		userService.deleteUser(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
