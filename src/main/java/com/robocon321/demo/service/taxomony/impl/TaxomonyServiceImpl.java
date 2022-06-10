package com.robocon321.demo.service.taxomony.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.repository.TaxomonyRepository;
import com.robocon321.demo.service.taxomony.TaxomonyService;

@Service
public class TaxomonyServiceImpl implements TaxomonyService {
	@Autowired
	private TaxomonyRepository taxomonyRepository;

	@Override
	public List<Taxomony> getAll() {
		return taxomonyRepository.findAll();
	}

	@Override
	public List<Taxomony> getAllByType(String[] types) {
		return taxomonyRepository.findByTypeIn(types);
	}

}
