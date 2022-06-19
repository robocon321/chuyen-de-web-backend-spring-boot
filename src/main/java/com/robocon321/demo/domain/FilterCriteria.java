package com.robocon321.demo.domain;

import com.robocon321.demo.type.FilterOperate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterCriteria {
	  private String field;
	  private FilterOperate operator;
	  private Object value;
}
