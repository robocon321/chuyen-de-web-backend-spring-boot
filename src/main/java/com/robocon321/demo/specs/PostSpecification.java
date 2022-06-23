package com.robocon321.demo.specs;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.taxomony.Taxomony;

public class PostSpecification {
	public static Specification<Post> filter(FilterCriteria criteria) {
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

	public static Specification<Post> filterByTaxomonyId(Integer id) {
		return (root, query, builder) -> {
			if (id == null)
				return null;
			Join<Post, Taxomony> taxomonyJoin = root.join("taxomonies");
			return builder.equal(taxomonyJoin, id);
		};

	}

}
