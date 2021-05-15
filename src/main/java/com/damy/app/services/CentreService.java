package com.damy.app.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.damy.app.shared.dto.UserDto;

@Service
public interface CentreService {

	UserDto getCentreByUserId(String id);

	List<UserDto> getCentres();

}
