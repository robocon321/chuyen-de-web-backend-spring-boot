package com.robocon321.demo.service.taxomony.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.CustomSpecification;
import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.taxomony.TaxomonyDTO;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.repository.TaxomonyRepository;
import com.robocon321.demo.service.taxomony.TaxomonyService;
import com.robocon321.demo.type.FilterOperate;

@Service
public class TaxomonyServiceImpl implements TaxomonyService {
	@Autowired
	private TaxomonyRepository taxomonyRepository;

	@Override
	public List<TaxomonyDTO> getAll() {
		List<TaxomonyDTO > taxomonyDTOs = new ArrayList<TaxomonyDTO>();
		List<Taxomony> taxomonies = taxomonyRepository.findAll();
		for(Taxomony taxomony : taxomonies) {
			TaxomonyDTO dto = new TaxomonyDTO();
			BeanUtils.copyProperties(taxomony, dto);
			taxomonyDTOs.add(dto);
		}
		return taxomonyDTOs;
	}

	@Override
	public List<TaxomonyDTO> getAll(Map<String, String> filter) {
		Specification<Taxomony> spec = null;	

		for(Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Taxomony> specType = new CustomSpecification(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.or(specType);
					}
				}
			} else if(keyEntry.startsWith("BT")) {
				String field = keyEntry.substring(8);
				String[] arrValue = valueEntry.split("%2C");
				if(arrValue.length == 2) {
					Specification<Taxomony> specTypeGreater = new CustomSpecification(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Taxomony> specTypeLess = new CustomSpecification(new FilterCriteria(field, FilterOperate.LESS, arrValue[0]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Taxomony> specType = new CustomSpecification(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}				
			}
		}

		List<TaxomonyDTO > taxomonyDTOs = new ArrayList<TaxomonyDTO>();
		List<Taxomony> taxomonies = taxomonyRepository.findAll(spec);
		for(Taxomony taxomony : taxomonies) {
			TaxomonyDTO dto = new TaxomonyDTO();
			BeanUtils.copyProperties(taxomony, dto);
			
			Taxomony parent = taxomony.getParent();
			if(parent != null) {
				TaxomonyDTO parentDTO = new TaxomonyDTO();
				BeanUtils.copyProperties(taxomony.getParent(), parentDTO);
				dto.setParent(parentDTO);				
			}
			
			List<Taxomony> childs = taxomony.getChilds();
			if(childs != null) {
				for(Taxomony child: childs) {
					TaxomonyDTO childDTO = new TaxomonyDTO();
					BeanUtils.copyProperties(child, childDTO);
					dto.getChilds().add(childDTO);
				}
			}
			
			taxomonyDTOs.add(dto);
		}
		
		return taxomonyDTOs;
	}

}
