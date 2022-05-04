package com.robocon321.demo.dto.taxomony;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxomonyMetaDTO {
	private Integer id;
	private String key;
	private String value;
	private TaxomonyDTO taxomony;
}
