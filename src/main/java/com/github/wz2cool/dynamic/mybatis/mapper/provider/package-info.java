/**
 * 处理动态sql
 * <p>
 * <p>
 * {@link com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicInsertProvider#insert(org.apache.ibatis.builder.annotation.ProviderContext)}
 * <code>
 * insert into userinfo (optional_string, optional_integer,
 * optional_int, optional_long, optional_decimal,
 * test_enum, test_enum2)
 * values (#{optionalString,jdbcType=VARCHAR}, #{optionalInteger,jdbcType=INTEGER},
 * #{optionalInt,jdbcType=INTEGER}, #{optionalLong,jdbcType=BIGINT}, #{optionalDecimal,jdbcType=DECIMAL},
 * #{testEnum,jdbcType=TINYINT}, #{testEnum2,jdbcType=INTEGER})
 * </code>
 * <p>
 * <p>
 * {@link com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicInsertProvider#insertSelective(org.apache.ibatis.builder.annotation.ProviderContext)}
 * <code>
 * insert into userinfo
 * <trim prefix="(" suffix=")" suffixOverrides=",">
 * <if test="optionalString != null">optional_string,</if>
 * <if test="optionalInteger != null">
 * optional_integer,
 * </if>
 * <if test="optionalInt != null">
 * optional_int,
 * </if>
 * <if test="optionalLong != null">
 * optional_long,
 * </if>
 * <if test="optionalDecimal != null">
 * optional_decimal,
 * </if>
 * <if test="testEnum != null">
 * test_enum,
 * </if>
 * <if test="testEnum2 != null">
 * test_enum2,
 * </if>
 * </trim>
 * <trim prefix="values (" suffix=")" suffixOverrides=",">
 * <if test="optionalString != null">#{optionalString},</if>
 * <if test="optionalInteger != null">
 * #{optionalInteger,jdbcType=INTEGER},
 * </if>
 * <if test="optionalInt != null">
 * #{optionalInt,jdbcType=INTEGER},
 * </if>
 * <if test="optionalLong != null">
 * #{optionalLong,jdbcType=BIGINT},
 * </if>
 * <if test="optionalDecimal != null">
 * #{optionalDecimal,jdbcType=DECIMAL},
 * </if>
 * <if test="testEnum != null">
 * #{testEnum,jdbcType=TINYINT},
 * </if>
 * <if test="testEnum2 != null">
 * #{testEnum2,jdbcType=INTEGER},
 * </if>
 * </trim>
 * </code>
 * <p>
 * <p>
 * {@link com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicUpdateProvider#updateByPrimaryKey(org.apache.ibatis.builder.annotation.ProviderContext)}
 * <code>
 * update userinfo
 * set optional_string = #{optionalString,jdbcType=VARCHAR},
 * optional_integer = #{optionalInteger,jdbcType=INTEGER},
 * optional_int = #{optionalInt,jdbcType=INTEGER},
 * optional_long = #{optionalLong,jdbcType=BIGINT},
 * optional_decimal = #{optionalDecimal,jdbcType=DECIMAL},
 * test_enum = #{testEnum,jdbcType=TINYINT},
 * test_enum2 = #{testEnum2,jdbcType=INTEGER}
 * where id = #{id,jdbcType=INTEGER}
 * </code>
 * <p>
 * <p>
 * {@link com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicUpdateProvider#updateByPrimaryKeySelective(org.apache.ibatis.builder.annotation.ProviderContext)}
 * <code>
 * update userinfo
 * <set>
 * <if test="optionalString != null">
 * optional_string = #{optionalString,jdbcType=VARCHAR},
 * </if>
 * <if test="optionalInteger != null">
 * optional_integer = #{optionalInteger,jdbcType=INTEGER},
 * </if>
 * <if test="optionalInt != null">
 * optional_int = #{optionalInt,jdbcType=INTEGER},
 * </if>
 * <if test="optionalLong != null">
 * optional_long = #{optionalLong,jdbcType=BIGINT},
 * </if>
 * <if test="optionalDecimal != null">
 * optional_decimal = #{optionalDecimal,jdbcType=DECIMAL},
 * </if>
 * <if test="testEnum != null">
 * test_enum = #{testEnum,jdbcType=TINYINT},
 * </if>
 * <if test="testEnum2 != null">
 * test_enum2 = #{testEnum2,jdbcType=INTEGER},
 * </if>
 * </set>
 * where id = #{id,jdbcType=INTEGER}
 * </code>
 *
 * @author wangjin
 */
@ParametersAreNonnullByDefault
package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import javax.annotation.ParametersAreNonnullByDefault;

