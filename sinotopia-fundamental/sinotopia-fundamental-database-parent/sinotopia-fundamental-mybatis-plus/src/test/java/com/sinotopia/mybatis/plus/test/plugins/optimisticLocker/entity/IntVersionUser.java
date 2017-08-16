package com.sinotopia.mybatis.plus.test.plugins.optimisticLocker.entity;

import java.io.Serializable;

import com.sinotopia.mybatis.plus.annotations.TableField;
import com.sinotopia.mybatis.plus.annotations.TableName;

@TableName("version_user")
public class IntVersionUser extends IntVersionFather implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private Integer age;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
