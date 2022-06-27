package com.robocon321.demo.service.common.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.common.ContactDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.common.Contact;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.ContactRepository;
import com.robocon321.demo.service.common.ContactService;
import com.robocon321.demo.specs.ContactSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactRepository contactRepository;
	
	@Override
	public List<ContactDTO> getAll(Map<String, String> filter, String sort) {
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

		String arrSort[] = sort.split("__");
		String sortName;
		String sortType;
		if(arrSort.length == 2) {
			sortName = arrSort[0];
			sortType = arrSort[1];						
		} else {
			sortName = "id";
			sortType = "ASC";
		}

		List<ContactDTO> contactDTOs = new ArrayList<ContactDTO>();
		List<Contact> contacts = contactRepository.findAll(spec, sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending());
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

	@Override
	public List<ContactDTO> insert(List<ContactDTO> contactDTOs) throws RuntimeException {
		List<Contact> contacts = new ArrayList<Contact>();
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
			throw new RuntimeException("Your session invalid");
		} else {
			UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = new User();
			BeanUtils.copyProperties(userDTO, user);
			
			for(ContactDTO contactDTO: contactDTOs) {
				Contact contact = new Contact();	
				BeanUtils.copyProperties(contactDTO, contact);
				contact.setStatus(1);
				contact.setModifiedUser(user);
				contact.setModifiedTime(new Date());
				contacts.add(contact);
			}
			
			contacts = contactRepository.saveAll(contacts);
			
			List<ContactDTO> result = new ArrayList<>();
			
			for(Contact contact : contacts) {
				ContactDTO contactDTO = new ContactDTO();
				BeanUtils.copyProperties(contact, contactDTO);
				result.add(contactDTO);
			}
			
			return result;
		}
	}

	@Override
	public List<ContactDTO> update(List<ContactDTO> contactDTOs) throws RuntimeException {
		List<Contact> contacts = new ArrayList<Contact>();
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
			throw new RuntimeException("Your session invalid");
		} else {
			UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = new User();
			BeanUtils.copyProperties(userDTO, user);
			
			for(ContactDTO contactDTO: contactDTOs) {
				if(contactDTO.getId() == null) {
					throw new RuntimeException("Your contact invalid");
				} else {
					Contact contact = new Contact();
					BeanUtils.copyProperties(contactDTO, contact);
					contact.setStatus(1);
					contact.setModifiedUser(user);
					contact.setModifiedTime(new Date());
					contacts.add(contact);
				}
			}
			
			contacts = contactRepository.saveAll(contacts);			
			
			List<ContactDTO> result = new ArrayList<>();
			for(Contact contact : contacts) {
				ContactDTO contactDTO = new ContactDTO();
				BeanUtils.copyProperties(contact, contactDTO);
				result.add(contactDTO);
			}
			
			return result;
		}
	}

}
