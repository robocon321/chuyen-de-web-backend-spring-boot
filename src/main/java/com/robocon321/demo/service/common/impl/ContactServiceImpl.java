package com.robocon321.demo.service.common.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.common.ContactDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.common.Contact;
import com.robocon321.demo.repository.ContactRepository;
import com.robocon321.demo.service.common.ContactService;
import com.robocon321.demo.specs.ContactSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactRepository contactRepository;
	
	@Override
	public List<ContactDTO> getAll(Map<String, String> filter) {
		Specification<Contact> spec = null;	

		for(Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Contact> specType = ContactSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
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
					Specification<Contact> specTypeGreater = ContactSpecification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Contact> specTypeLess = ContactSpecification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[0]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Contact> specType = ContactSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}				
			}
		}

		List<ContactDTO> contactDTOs = new ArrayList<ContactDTO>();
		List<Contact> contacts = contactRepository.findAll(spec);
		for(Contact contact : contacts) {
			ContactDTO dto = new ContactDTO();
			BeanUtils.copyProperties(contact, dto);
			
			UserDTO userModDTO = new UserDTO();
			BeanUtils.copyProperties(contact.getModifiedUser(), userModDTO);
			dto.setModifiedUser(userModDTO);
			
			contactDTOs.add(dto);
		}
		
		return contactDTOs;
	}

}
