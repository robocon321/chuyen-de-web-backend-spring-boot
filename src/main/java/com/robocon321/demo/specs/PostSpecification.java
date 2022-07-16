package com.robocon321.demo.specs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.review.Comment;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.entity.user.User;

public class PostSpecification {
	public static Specification<Post> filter(FilterCriteria criteria) throws RuntimeException {
		return (root, query, builder) -> {
			if (criteria == null) {
				return null;
			}

			String[] splitField = criteria.getField().split("\\.");

			if (splitField.length >= 2) {
				String field = splitField[splitField.length - 1];

				Join postJoin = null;

				for (int i = 0; i < splitField.length - 1; i++) {
					postJoin = root.join(splitField[i]);
				}

				switch (criteria.getOperator()) {
				case GREATER:
					if (criteria.getField().equals("modifiedTime")) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date d = df.parse(criteria.getValue().toString());
							return builder.greaterThanOrEqualTo(root.<Date>get(criteria.getField()), d);
						} catch (ParseException e) {
							e.printStackTrace();
						}

					} else {
						return builder.greaterThanOrEqualTo(root.<String>get(criteria.getField()),
								criteria.getValue().toString());
					}
				case LESS:
					if (criteria.getField().equals("modifiedTime")) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date d = df.parse(criteria.getValue().toString());
							return builder.lessThanOrEqualTo(root.<Date>get(criteria.getField()), d);
						} catch (ParseException e) {
							e.printStackTrace();
						}

					} else {
						return builder.lessThanOrEqualTo(root.<String>get(criteria.getField()),
								criteria.getValue().toString());
					}
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
					if (criteria.getField().equals("modifiedTime")) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date d = df.parse(criteria.getValue().toString());
							return builder.greaterThanOrEqualTo(root.<Date>get(criteria.getField()), d);
						} catch (ParseException e) {
							e.printStackTrace();
						}

					} else {
						return builder.greaterThanOrEqualTo(root.<String>get(criteria.getField()),
								criteria.getValue().toString());
					}
				case LESS:
					if (criteria.getField().equals("modifiedTime")) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date d = df.parse(criteria.getValue().toString());
							return builder.lessThanOrEqualTo(root.<Date>get(criteria.getField()), d);
						} catch (ParseException e) {
							e.printStackTrace();
						}

					} else {
						return builder.lessThanOrEqualTo(root.<String>get(criteria.getField()),
								criteria.getValue().toString());
					}
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
