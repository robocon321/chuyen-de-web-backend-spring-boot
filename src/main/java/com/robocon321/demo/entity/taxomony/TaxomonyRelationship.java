package com.robocon321.demo.entity.taxomony;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Any;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taxomony_relationship")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxomonyRelationship {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@Any(metaDef = "taxomony_obj", metaColumn = @Column(name = "type", nullable = false))
	@JoinColumn(name = "object_id", nullable = false)
	private TaxomonyObj taxomonyObj;
	
	@ManyToOne
	@JoinColumn(name = "taxomony_id", nullable = false)
	@JsonIgnore
	private Taxomony taxomony;
}
