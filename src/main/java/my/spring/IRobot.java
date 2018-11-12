package my.spring;

public class IRobot {
    @InjectByType
    private Speaker speaker;
    @InjectByType
    private Cleaner cleaner;

    public void clean() {
        speaker.speak("Начал работу");
        cleaner.clean();
        speaker.speak("Закончил работу");
    }
}
