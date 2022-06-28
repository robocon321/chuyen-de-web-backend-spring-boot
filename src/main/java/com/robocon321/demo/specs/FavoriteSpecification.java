package com.robocon321.demo.specs;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.entity.common.Favorite;

public class FavoriteSpecification {
	public static Specification<Favorite> filter(FilterCriteria criteria) {
		return (root, query, builder) -> {
			if (criteria == null) {
				return null;
			}

			String[] splitField = criteria.getField().split("\\.");

			if (splitField.length >= 2) {
				String field = splitField[splitField.length - 1];

				Join postJoin = null;
				
				for(int i = 0; i < splitField.length - 1; i ++) {
					postJoin = root.join(splitField[i]);
				}

				switch (criteria.getOperator()) {
				case GREATER:
					return builder.greaterThanOrEqualTo(postJoin.get(field), criteria.getValue().toString());
				case LESS:
					return builder.lessThanOrEqualTo(postJoin.get(field), criteria.getValue().toString());
				case LIKE:
					return builder.like(postJoin.get(field), "%" + criteria.getValue() + "%");
				case EQUALS:
					return builder.equal(postJoin.get(field), criteria.getValue());
				case NOT_EQUALS:
					return builder.notEqual(postJoin.get(field), criteria.getValue());
				case STARTS_WITH:
					return builder.like(postJoin.get(field), criteria.getValue() + "%");
				case ENDS_WITH:
					return builder.like(postJoin.get(field), "%" + criteria.getValue());
				default:
					throw new RuntimeException("Operation not supported yet");

				}

			} else {

				switch (criteria.getOperator()) {
				case GREATER:
					return builder.greaterThanOrEqualTo(root.<String>get(criteria.getField()),
							criteria.getValue().toString());
				case LESS:
					return builder.lessThanOrEqualTo(root.<String>get(criteria.getField()),
							criteria.getValue().toString());
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
			}
		};
	}


}
