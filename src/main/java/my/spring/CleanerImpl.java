package my.spring;

public class CleanerImpl implements Cleaner {
    @InjectRandomInt(min = 2, max = 5)
    private Integer repeat;

    @Override
    public void clean() {
        // clean logic
        for (int i = 0; i <= repeat; i++) {
            System.out.println("VVVVVVVVvvvvvvvvvvvvvvv");
        }
    }
}