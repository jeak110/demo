package my.spring;

public class IRobot {
    private Speaker speaker = new ConsoleSpeaker();
    private Cleaner cleaner = new CleanerImpl();

    public void clean() {
        speaker.speak("Начал работу");
        cleaner.clean();
        speaker.speak("Закончил работу");
    }
}
