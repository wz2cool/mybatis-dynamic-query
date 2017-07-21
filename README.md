MyBatis Dynamic Query
=====================================

[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/wz2cool/mybatis-dynamic-query.svg?branch=master)](https://travis-ci.org/wz2cool/mybatis-dynamic-query)
[![Coverage Status](https://coveralls.io/repos/github/wz2cool/mybatis-dynamic-query/badge.svg?branch=master)](https://coveralls.io/github/wz2cool/mybatis-dynamic-query?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/597283ce368b08005906060c/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/597283ce368b08005906060c)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.wz2cool/mybatis-dynamic-query/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wz2cool/mybatis-dynamic-query)

The MyBatis Dynamic Query framework makes it easier to generate "where" and "order" expression dynamically in mapper xml.
<b>mybatis-dynamic-query comes to solve four problem:</b>
- no need write lots of code in xml.
- filtering or sorting maintained by java code.
- hot update "where" and "order" expression. 
- save filter or sort descriptor and re-use them.

## Database support
- H2
- MySql
- SqlServer
- Postresql (BETA)
- Oracle (TODO)

## Maven
```xml
<dependency>
    <groupId>com.github.wz2cool</groupId>
    <artifactId>mybatis-dynamic-query</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
```

## Example
- create a table by sql.
```sql
CREATE TABLE product (
  product_id    INT PRIMARY KEY,
  category_id   INT NOT NULL,
  product_name  VARCHAR (50) NOT NULL,
  price         DECIMAL
);
```
- create a model map to this table.
```java
public class Product {
    @QueryColumn(name = "product_id") // custom column name
    private Integer productID;
    private String productName;
    private BigDecimal price;
    private Integer categoryID;

    // get, set method.
}
```
- create a dynamic select in mapper interface / xml.
```java
List<Product> getProductByDynamic(Map<String, Object> params);
```
```xml
<select id="getProductByDynamic" parameterType="java.util.Map"
         resultType="Product">
    SELECT * FROM product
    <if test="whereExpression != null and whereExpression != ''">WHERE ${whereExpression}</if>
    <if test="orderExpression != null and orderExpression != ''">ORDER BY ${orderExpression}</if>
</select>
```
- generate expression and param map (NOTE: expression string also put into map).
```java
@Test
public void simpleDemo() throws Exception {
    // create a filter descriptor.
    FilterDescriptor idFilter =
        new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.EQUAL, 2);
    // generater expression and params base on filter descriptor.
    Map<String, Object> queryParams =
        mybatisQueryProvider.getWhereQueryParamMap(
            Product.class, "whereExpression", idFilter);
    // pass query params.
    Product productView =
        northwindDao.getProductByDynamic(queryParams).stream().findFirst().orElse(null);
    
    assertEquals(Integer.valueOf(2), productView.getProductID());
}
```
