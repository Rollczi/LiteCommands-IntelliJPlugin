package dev.rollczi.litecommands.intellijplugin.command;

import java.util.Map;


public class CommandService {


    @Deprecated
    public void test() throws NoSuchMethodException {
        Deprecated deprecated = CommandService.class.getMethod("test").getAnnotation(Deprecated.class);

        boolean isForRemoval = deprecated.forRemoval();
        String since = deprecated.since();
    }

}
