package com.damy.app.services;

import com.damy.app.shared.dto.UserDto;

public interface EmailService {

	 void sendEmail(UserDto user);
}
