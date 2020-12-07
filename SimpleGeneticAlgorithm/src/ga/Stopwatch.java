package ga;

public class Stopwatch {

    private long start;
    private long stop;


    public Stopwatch() {
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop(){
        stop = System.currentTimeMillis();
    }


    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }

    public long getTimeElapsed() {
        return stop - start;
    }
}
