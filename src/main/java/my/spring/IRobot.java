package my.spring;

import javax.annotation.PostConstruct;

public class IRobot {
    @InjectByType
    private Speaker speaker;
    @InjectByType
    private Cleaner cleaner;

    @PostConstruct
    public void initIRobot() {
        System.out.println(cleaner.getName());
    }

    public void clean() {
        speaker.speak("Начал работу");
        cleaner.clean();
        speaker.speak("Закончил работу");
    }
}
