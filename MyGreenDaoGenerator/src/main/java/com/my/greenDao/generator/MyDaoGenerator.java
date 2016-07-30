package com.my.greenDao.generator;

import de.greenrobot.daogenerator.*;

public class MyDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.my.greenDao.bean");
        schema.setDefaultJavaPackageDao("com.my.greenDao.dao");
        addNotify(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java-greenDao");

    }

    private static void addNotify(Schema schema) {

        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Notify」（既类名）
        Entity Notify = schema.addEntity("Notify");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
        Notify.addIdProperty();
        Notify.addStringProperty("title");
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
        // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
        Notify.addStringProperty("description");
        Notify.addStringProperty("notifyContent");
        Notify.addBooleanProperty("isRead");
        Notify.addDateProperty("createDate");

    }
}
