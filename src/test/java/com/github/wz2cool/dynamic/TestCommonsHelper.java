package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.model.HelloWorld;
import com.github.wz2cool.dynamic.model.Student;

/**
 * @author 王进
 **/
public class TestCommonsHelper {

    public static void main(String[] args) {
        System.out.println(CommonsHelper.getPropertyInfo(Student::getName).getPropertyName());
        System.out.println(CommonsHelper.getPropertyInfo(Student::getAge).getPropertyName());
        System.out.println(CommonsHelper.getPropertyInfo(Student::getNote).getPropertyName());
        System.out.println(CommonsHelper.getPropertyInfo(HelloWorld::getDateProperty).getPropertyName());
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            CommonsHelper.getPropertyInfo(Student::getName);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
