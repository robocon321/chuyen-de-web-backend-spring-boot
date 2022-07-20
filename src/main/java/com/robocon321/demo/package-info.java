@AnyMetaDef(name = "view_obj", metaType = "string", idType = "int", metaValues = {
		@MetaValue(value = "post", targetEntity = Post.class),
		@MetaValue(value = "taxomony", targetEntity = Taxomony.class) })

@AnyMetaDef(name = "taxomony_obj", metaType = "string", idType = "int", metaValues = {
		@MetaValue(value = "post", targetEntity = Post.class),
		@MetaValue(value = "attribute", targetEntity = Attribute.class),
		@MetaValue(value = "product", targetEntity =  Product.class)})

package com.robocon321.demo;

import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.entity.post.product.Attribute;
import com.robocon321.demo.entity.post.product.Product;
