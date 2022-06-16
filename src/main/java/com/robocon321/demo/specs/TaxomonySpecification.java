package com.robocon321.demo.specs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.taxomony.Taxomony;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TaxomonySpecification {
	public static Specification<Taxomony> filter(FilterCriteria criteria) {
		return (root, query, builder) -> {
			if (criteria == null) {
				return null;
			}

			switch (criteria.getOperator()) {
			case GREATER:
				return builder.greaterThanOrEqualTo(root.<String>get(criteria.getField()),
						criteria.getValue().toString());
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
				return builder.like(root.<String>get(criteria.getField()), "%" + criteria.getValue());
			default:
				throw new RuntimeException("Operation not supported yet");

			}
		};
	}
}
