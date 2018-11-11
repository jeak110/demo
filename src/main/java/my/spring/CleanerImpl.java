package my.spring;

public class CleanerImpl implements Cleaner {
    private Integer repeat = 2;

    @Override
    public void clean() {
        // clean logic
        for (int i = 0; i < repeat; i++) {
            System.out.println("VVVVVVVVvvvvvvvvvvvvvvv");
        }
    }
}
