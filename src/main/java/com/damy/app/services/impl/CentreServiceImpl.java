package com.damy.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.damy.app.entities.UserEntity;
import com.damy.app.repositories.UserRepository;
import com.damy.app.services.CentreService;
import com.damy.app.shared.dto.UserDto;

@Service
public class CentreServiceImpl implements CentreService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDto getCentreByUserId(String id) {
		
		UserEntity userEntity = userRepository.findByUserId(id);
		
		if(userEntity == null|| !userEntity.getRole().toLowerCase().equals("centre")) throw new UsernameNotFoundException("Centre not Found"); 
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userEntity, userDto);
		
		return userDto;
	}

	@Override
	public List<UserDto> getCentres() {


		List<UserDto> usersDto = new ArrayList<>();
		Iterable<UserEntity> users = new ArrayList<>();
		 users  =  userRepository.findAll();
		
		
		for(UserEntity userEntity: users) {
			if(userEntity.getRole().toLowerCase().equals("centre")) {
				ModelMapper modelMapper = new ModelMapper();	
				UserDto user = modelMapper.map(userEntity, UserDto.class);
				usersDto.add(user);
			}

		}
		
		return usersDto;
	}

}
