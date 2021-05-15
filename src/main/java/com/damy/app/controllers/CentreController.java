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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.damy.app.responses.UserResponse;
import com.damy.app.services.CentreService;
import com.damy.app.services.UserService;
import com.damy.app.shared.dto.UserDto;

@CrossOrigin(origins="*")
@RestController
@RequestMapping(path="/centres",produces = MediaType.APPLICATION_JSON_VALUE)
public class CentreController {

	@Autowired
	CentreService centreService;
	
	@GetMapping(path="/{id}")
	public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
		
		UserDto userDto = centreService.getCentreByUserId(id);
		
		UserResponse userResponse = new UserResponse();
		
		BeanUtils.copyProperties(userDto, userResponse);
		
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}
	
	
	@GetMapping()
	public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(value="page", defaultValue = "1") int page,@RequestParam(value="limit", defaultValue = "4")  int limit ,@RequestParam(value="search", defaultValue = "") String search,@RequestParam(value="status", defaultValue = "1") int status) {
		
		List<UserResponse> usersResponse = new ArrayList<>();
		
		List<UserDto> users = centreService.getCentres();
		
		for(UserDto userDto: users) {
			
			ModelMapper modelMapper = new ModelMapper();
			UserResponse userResponse =  modelMapper.map(userDto, UserResponse.class);
			
			usersResponse.add(userResponse);
		}
		
		return new ResponseEntity<List<UserResponse>>(usersResponse, HttpStatus.OK);
	}
}
