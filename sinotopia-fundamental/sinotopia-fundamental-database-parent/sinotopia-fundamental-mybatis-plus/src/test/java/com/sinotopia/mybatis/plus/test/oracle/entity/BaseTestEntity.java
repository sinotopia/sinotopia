package com.sinotopia.mybatis.plus.test.oracle.entity;

import java.io.Serializable;

import com.sinotopia.mybatis.plus.annotations.KeySequence;
import com.sinotopia.mybatis.plus.annotations.TableField;
import com.sinotopia.mybatis.plus.annotations.TableId;

/**
 * 用户表
 */
@KeySequence("SEQ_TEST")
public class BaseTestEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID")
    private Long id;


    public BaseTestEntity() {

    }
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



}
