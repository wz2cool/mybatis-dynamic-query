package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import com.github.wz2cool.dynamic.model.Student;
import org.junit.Test;

public class ProviderTableHelperTest {

    @Test
    public void t1() {
        ProviderTable providerTable = ProviderTableHelper.getProviderTable(Student.class);
        System.out.println(providerTable);

        ProviderColumn note = providerTable.getProviderColumn("note");
        System.out.println(note.getDbColumn());
        System.out.println(note.getDbColumnTable());
        System.out.println(note);
    }

}