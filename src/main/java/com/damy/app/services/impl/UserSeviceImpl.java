package com.damy.app.services.impl;



import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.damy.app.entities.UserEntity;
import com.damy.app.repositories.UserRepository;
import com.damy.app.services.EmailService;
import com.damy.app.services.UserService;
import com.damy.app.shared.Utils;
import com.damy.app.shared.dto.UserDto;





@Service
public class UserSeviceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	Utils util;
	
	@Autowired
	EmailService confMail;
	
	@Override
	public UserDto createUser(UserDto user) {
		
		UserEntity checkUser = userRepository.findByEmail(user.getEmail());
		
		if(checkUser != null) throw new RuntimeException("User Alrady Exists !");
		
		
        ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		
		
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		userEntity.setUserId(util.generateStringId(32));
		
		UserEntity newUser = userRepository.save(userEntity);
		
		UserDto userDto =  modelMapper.map(newUser, UserDto.class);
		confMail.sendEmail(userDto);
		return userDto;
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email); 
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}


	@Override
	public UserDto getUser(String email) {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email); 
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userEntity, userDto);
		
		return userDto;
	}


	@Override
	public UserDto getUserByUserId(String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null) throw new UsernameNotFoundException(userId); 
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userEntity, userDto);
		
		return userDto;
	}


	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null) throw new UsernameNotFoundException(userId); 
		if(userDto.getFirstName()!=null)
		userEntity.setFirstName(userDto.getFirstName());
		
		if(userDto.getLastName()!=null)
		userEntity.setLastName(userDto.getLastName());
		
		if(userDto.getEmail()!=null)
		userEntity.setEmail(userDto.getEmail());
		
		if(userDto.getPassword()!=null)
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		
		if(userDto.getAdress()!=null)
		userEntity.setAdress(userDto.getAdress());
		
		if(userDto.getTelephone()!=null)
		userEntity.setTelephone(userDto.getTelephone());
		
		if(userDto.getCity()!=null)
		userEntity.setCity(userDto.getCity());
		
		if(userDto.getBloodType()!=null)
		userEntity.setBloodType(userDto.getBloodType());
		
		if(userDto.getContact()!=null)
		userEntity.setContact(userDto.getContact());
		
		UserEntity userUpdated = userRepository.save(userEntity);
		
		UserDto user = new UserDto();
		
		BeanUtils.copyProperties(userUpdated, user);
		
		return user;
	}


	@Override
	public void deleteUser(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null) throw new UsernameNotFoundException(userId); 
		
		userRepository.delete(userEntity);
		
	}


	@Override
	public List<UserDto> getUsers(int page, int limit, String search, int status) {
		
		if(page > 0) page = page - 1;
		
		List<UserDto> usersDto = new ArrayList<>();
		
		Pageable pageableRequest = PageRequest.of(page, limit);
		
		Page<UserEntity> userPage;
		
		if(search.isEmpty()) {
			userPage = userRepository.findAllUsers(pageableRequest);
		}
		else {
			
			userPage = userRepository.findAllUserByCriteria(pageableRequest, search, status);
		}
		
		
		List<UserEntity> users = userPage.getContent();
		
		for(UserEntity userEntity: users) {
			
			ModelMapper modelMapper = new ModelMapper();	
			UserDto user = modelMapper.map(userEntity, UserDto.class);
			
			usersDto.add(user);
		}
		
		return usersDto;
	}


	@Override
	public List<UserDto> getAllUsers() {
	Iterable<UserEntity> usersIterable =	userRepository.findAll();

	List<UserEntity> users= new ArrayList<>();
	usersIterable.forEach(users::add);
	ModelMapper modelMapper = new ModelMapper();
	List<UserDto> usersDto = new ArrayList<>();
	for(UserEntity userEntity: users) {
			
		UserDto user = modelMapper.map(userEntity, UserDto.class);
		
		usersDto.add(user);
	}
	return usersDto;
	}

}
