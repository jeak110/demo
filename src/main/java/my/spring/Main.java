package my.spring;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Toolkit.getDefaultToolkit().beep();
        ObjectFactory.getInstance().createObject(IRobot.class).clean();
    }
}

