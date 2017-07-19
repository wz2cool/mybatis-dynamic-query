MyBatis Dynamic Query
=====================================

[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/wz2cool/mybatis-dynamic-query.svg?branch=master)](https://travis-ci.org/wz2cool/mybatis-dynamic-query)
[![Coverage Status](https://coveralls.io/repos/github/wz2cool/mybatis-dynamic-query/badge.svg?branch=master)](https://coveralls.io/github/wz2cool/mybatis-dynamic-query?branch=master)

The MyBatis Dynamic Query framework makes it easier to generate "where" and "order" expression dynamically in mapper xml.
<b>mybatis-dynamic-query comes to solve four problem:</b>
- do not write lots of code in xml.
- filtering or sorting maintained by java code.
- hot update "where" and "order" expression. 
- save filter or sort descriptor and re-use them.