package com.comcast.video.dawg.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
public class UserException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserException(Throwable cause) {
		super(cause);
	}

	public UserException(String message) {
		super(message);
	}

	public UserException(String message, Throwable cause) {
		super(message, cause);
	}
}
