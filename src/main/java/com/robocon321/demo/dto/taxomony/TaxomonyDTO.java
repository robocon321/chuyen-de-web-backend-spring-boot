package com.robocon321.demo.dto.taxomony;

import java.util.Date;
import java.util.List;

import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxomonyDTO {
	private Integer id;
	private String name;
	private String slug;
	private String type;
	private String description;
	private TaxomonyDTO parentTaxomony;
	private Integer status;
	private User modifiedUser;
	private Date modifiedTime;
	private List<TaxomonyMetaDTO> metas;
}
