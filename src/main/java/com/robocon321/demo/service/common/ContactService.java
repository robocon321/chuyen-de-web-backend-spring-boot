package com.robocon321.demo.service.common;

import java.util.List;
import java.util.Map;

import com.robocon321.demo.dto.common.ContactDTO;

public interface ContactService {
	public List<ContactDTO> getAll(Map<String, String> filter, String sort);
	public List<ContactDTO> insert(List<ContactDTO> contactDTOs);
	public List<ContactDTO> update(List<ContactDTO> contactDTOs);
}
