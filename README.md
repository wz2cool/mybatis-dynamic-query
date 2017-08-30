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
- Postresql
- Oracle (TODO)

## Maven
```xml
<dependency>
    <groupId>com.github.wz2cool</groupId>
    <artifactId>mybatis-dynamic-query</artifactId>
    <version>2.0.0</version>
</dependency> 
```

## Dynamic Query example
- create two tables by sql.
```sql
DELETE FROM category;
INSERT INTO category (category_id, category_name, description) VALUES
  (1, 'Beverages', 'test'),
  (2, 'Condiments', 'test'),
  (3, 'Oil', 'test');

DELETE FROM product;
INSERT INTO product (product_id, category_id, product_name, price) VALUES
  (1, 1, 'Northwind Traders Chai', 18.0000),
  (2, 2, 'Northwind Traders Syrup', 7.5000),
  (3, 2, 'Northwind Traders Cajun Seasoning', 16.5000),
  (4, 3, 'Northwind Traders Olive Oil', 16.5000);
```
- create a model map to this table.
```java
public class ProductView {
    @Column(name = "product_id", table = "product")
    private Long productID;
    @Column(name = "product_name", table = "product")
    private String productName;
    @Column(name = "price", table = "product")
    private BigDecimal price;

    @Column(name = "category_id", table = "category")
    private Long categoryID;
    @Column(name = "category_name", table = "category")
    private String categoryName;
    @Column(name = "description", table = "category")
    private String description;

    // get, set method.
}
```
- create a dynamic select in mapper interface / xml.
```java
List<ProductView> getProductViewsByDynamic(Map<String, Object> params);
```
```xml
<select id="getProductViewsByDynamic" parameterType="java.util.Map"
        resultType="com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView">
    SELECT
    <choose>
        <when test="columnsExpression != null and columnsExpression !=''">
            ${columnsExpression}
        </when>
        <otherwise>
            *
        </otherwise>
    </choose>
    FROM product LEFT JOIN category ON product.category_id = category.category_id
    <if test="whereExpression != null and whereExpression != ''">WHERE ${whereExpression}</if>
    <if test="orderExpression != null and orderExpression != ''">ORDER BY ${orderExpression}</if>
</select>
```
- generate expression and param map (NOTE: expression string also put into map).
```java
@Test
public void testMultiTablesFilter() throws Exception {
        FilterDescriptor priceFilter1 =
                new FilterDescriptor(ProductView.class, ProductView::getPrice,
                        FilterOperator.GREATER_THAN_OR_EQUAL, 6);
        FilterDescriptor priceFilter2 =
                new FilterDescriptor(ProductView.class, ProductView::getPrice,
                        FilterOperator.LESS_THAN, 10);
        FilterDescriptor categoryNameFilter =
                new FilterDescriptor(ProductView.class, ProductView::getCategoryName,
                        FilterOperator.START_WITH, "Co");

        DynamicQuery<ProductView> dynamicQuery = new DynamicQuery<>(ProductView.class);
        dynamicQuery.addFilter(priceFilter1);
        dynamicQuery.addFilter(priceFilter2);
        dynamicQuery.addFilter(categoryNameFilter);

        Map<String, Object> params = MybatisQueryProvider.getQueryParamMap(dynamicQuery,
                "whereExpression",
                "sortExpression",
                "columnsExpression");

        List<ProductView> result = northwindDao.getProductViewsByDynamic(params);
        assertEquals(true, result.size() > 0);
}
```
output result
```bash
==>  Preparing: SELECT product.product_id AS product_id, product.price AS price, category.description AS description, category.category_name AS category_name, product.product_name AS product_name, category.category_id AS category_id 
FROM product LEFT JOIN category ON product.category_id = category.category_id WHERE (product.price >= ? AND product.price < ? AND category.category_name LIKE ?) 
==> Parameters: 6(Integer), 10(Integer), Co%(String)
<==    Columns: PRODUCT_ID, PRICE, DESCRIPTION, CATEGORY_NAME, PRODUCT_NAME, CATEGORY_ID
<==        Row: 2, 7.5000, test, Condiments, Northwind Traders Syrup, 2
<==      Total: 1
```

## Dynamic Query Mapper
DynamicQueryMapper is based on tk.mybatis.mapper.
### spring boot configuration
1. add dependency
```xml
<!-- base -->
<dependency>
    <groupId>com.github.wz2cool</groupId>
    <artifactId>mybatis-dynamic-query</artifactId>
    <version>2.0.0</version>
</dependency>
<!-- register mapper -->
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring-boot-starter</artifactId>
    <version>1.1.3</version>
</dependency>
<!-- mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.4</version>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.0</version>
</dependency>
<!-- spring boot web already has jackson-->
<!--  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.0</version>
</dependency>-->
<!-- spring boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.2.0</version>
</dependency>
```
2. register DynamicQueryMapper in `application.properties` file.
```bash
mapper.mappers[0]=com.github.wz2cool.dynamic.mybatis.mapper.DynamicQueryMapper
```
3. scan mappers.
```java
@SpringBootApplication
@MapperScan(basePackages = "com.github.wz2cool.mdqtest.mapper")
@EnableSwagger2
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
### create mapper 
```java
public interface ProductDao extends DynamicQueryMapper<Product> {
}
```
![](https://raw.githubusercontent.com/wz2cool/markdownPhotos/master/res/20170815143538.png?_=7365737)

## Docs
[中文文档1.x](https://wz2cool.gitbooks.io/mybatis-dynamic-query-zh-cn/content/)
|
[中文文档2.x](https://wz2cool.gitbooks.io/mybatis-dynamic-query-2-0-zh-cn/content/)