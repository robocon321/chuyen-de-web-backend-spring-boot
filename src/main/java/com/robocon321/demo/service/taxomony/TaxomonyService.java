package com.robocon321.demo.service.taxomony;

import java.util.List;
import java.util.Map;

import com.robocon321.demo.dto.taxomony.TaxomonyDTO;

public interface TaxomonyService {
	public List<TaxomonyDTO> getAll();
	public List<TaxomonyDTO> getAll(Map<String, String> filter); 
}
