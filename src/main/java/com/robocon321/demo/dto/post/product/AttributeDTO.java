package com.robocon321.demo.dto.post.product;

import java.util.Date;

import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeDTO {
	private Integer id;
	private String name;
	private ProductDTO product;
	private User modifiedUser;
	private Date modifiedTime;

}
