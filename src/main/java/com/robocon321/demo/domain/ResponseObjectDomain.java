package com.robocon321.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObjectDomain<T> {
	private T data = null;
	private String message = "";
	private boolean isSuccess = false;
}
