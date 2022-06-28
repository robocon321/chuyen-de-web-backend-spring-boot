package com.robocon321.demo.service.common;

import java.util.List;
import java.util.Map;

import com.robocon321.demo.dto.common.FavoriteDTO;

public interface FavoriteService {
	public List<FavoriteDTO> getAll(String sort, Map<String, String> filter);
	public void delete(List<Integer> ids);
}
