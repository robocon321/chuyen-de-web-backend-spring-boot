package com.robocon321.demo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomSpecification implements Specification {
	private FilterCriteria criteria;

	@Override
	public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
		if(criteria == null) {
			return null;
		}
		
		switch (criteria.getOperator()) {
		case GREATER:
			return builder.greaterThanOrEqualTo(root.<String>get(criteria.getField()), criteria.getValue().toString());
		case LESS:
			return builder.lessThanOrEqualTo(root.<String>get(criteria.getField()), criteria.getValue().toString());
		case LIKE:
			return builder.like(root.<String>get(criteria.getField()), "%" + criteria.getValue() + "%");
		case EQUALS:
			return builder.equal(root.get(criteria.getField()), criteria.getValue());
		case NOT_EQUALS:
			return builder.notEqual(root.get(criteria.getField()), criteria.getValue());
		case STARTS_WITH:
			return builder.like(root.<String>get(criteria.getField()), criteria.getValue() + "%");
		case ENDS_WITH:
			return builder.like(root.<String>get(criteria.getField()), "%"+criteria.getValue());
		default:
			throw new RuntimeException("Operation not supported yet");

		}
	}

	private Object castToRequiredType(Class fieldType, String value) {
		if (fieldType.isAssignableFrom(Double.class)) {
			return Double.valueOf(value);
		} else if (fieldType.isAssignableFrom(Integer.class)) {
			return Integer.valueOf(value);
		} else if (Enum.class.isAssignableFrom(fieldType)) {
			return Enum.valueOf(fieldType, value);
		}
		return null;
	}

	private Object castToRequiredType(Class fieldType, List<String> value) {
		List<Object> lists = new ArrayList<>();
		for (String s : value) {
			lists.add(castToRequiredType(fieldType, s));
		}
		return lists;
	}

}
