package com.robocon321.demo.service.taxomony;

import java.util.List;

import com.robocon321.demo.entity.taxomony.Taxomony;

public interface TaxomonyService {
	public List<Taxomony> getAll();
	public List<Taxomony> getAllByType(String[] types);
}
