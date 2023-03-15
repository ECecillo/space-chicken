package fr.univlyon1.m1if.m1if13.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not Found")
public class UserNotFoundException extends RuntimeException { }
