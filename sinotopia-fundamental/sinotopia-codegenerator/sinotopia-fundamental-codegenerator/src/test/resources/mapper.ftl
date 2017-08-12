<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${namespace}">

    <sql id="requestAllFields">
${fieldClause}
    </sql>

    <sql id="whereClause">
        <where>
${whereClause}
            <include refid="extendedWhereClause" />
        </where>
    </sql>

    <insert id="add" useGeneratedKeys="true" parameterType="${parameterType}" keyProperty="id">
        INSERT INTO ${tableName}
        <trim prefix="(" suffix=")" prefixOverrides=",">
${insertParams}
        </trim>
        VALUES
        <trim prefix="(" suffix=")" prefixOverrides=",">
${insertValues}
        </trim>
    </insert>

    <update id="update" parameterType="${parameterType}">
        UPDATE ${tableName}
        <trim prefix="SET" prefixOverrides=",">
${updateClause}
            <include refid="extendedUpdateSql" />
        </trim>
        WHERE id = ${r'#'}{id}
    </update>
    
    <select id="query" parameterType="${parameterType}" resultType="${parameterType}">
        SELECT <include refid="requestAllFields"/> FROM ${tableName}
        <include refid="whereClause" />
        <include refid="extendedOrderByClause" />
        <if test="null!=requestOffset">
            LIMIT ${r'#'}{requestOffset}, ${r'#'}{requestCount}
        </if>
    </select>
    
    <select id="get" parameterType="${parameterType}" resultType="${parameterType}">
        SELECT <include refid="requestAllFields"/> FROM ${tableName}
        <include refid="whereClause" />
        LIMIT 1
    </select>
    
    <select id="getById" parameterType="long" resultType="${parameterType}">
        SELECT <include refid="requestAllFields"/> FROM ${tableName} WHERE id = ${r'#'}{id}
    </select>
    
    <select id="count" parameterType="${parameterType}" resultType="int">
        SELECT COUNT(1) FROM ${tableName} <include refid="whereClause" />
    </select>

    <!-- 扩展的更新等语句（自定义）-->
    <sql id="extendedUpdateSql">
        <if test="null!=extendedParameter">

        </if>
    </sql>

    <!-- 扩展的条件过滤语句（自定义）-->
    <sql id="extendedWhereClause">
        <if test="null!=extendedParameter">
            
        </if>
    </sql>

    <!-- 扩展的排序等语句（自定义）-->
    <sql id="extendedOrderByClause">
        <if test="null!=extendedParameter">
            
        </if>
    </sql>
    
</mapper>