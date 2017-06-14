package Main;

public class Time {
    public static double deltaTime;
    public static double scale = 1;

    private static long currentTime;
    private static long lastLoopTime = System.nanoTime();
    private static long updateLength;

    private static final int FPS = 60;
    private static final long TARGET_TIME = 1000000000 / FPS;

    static void updateDeltaTime() {
        currentTime = System.nanoTime();
        updateLength = currentTime - lastLoopTime;
        lastLoopTime = currentTime;
        deltaTime = (updateLength / (double)TARGET_TIME) * scale;
    }

    static long calculateWaitTime() {
        return (lastLoopTime - System.nanoTime() + TARGET_TIME) / 1000000;
    }
}
