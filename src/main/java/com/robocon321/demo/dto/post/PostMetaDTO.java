package com.robocon321.demo.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMetaDTO {
	private Integer id;
	private String key;
	private String value;
	private PostDTO post;

}
