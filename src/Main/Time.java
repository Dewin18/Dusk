package Main;

public class Time
{
    public static double deltaTime;
    public static double scale = 1;

    private static long currentTime;
    private static long lastLoopTime = System.nanoTime();
    private static long updateLength;

    private static final int FPS = 60;
    private static final long TARGET_TIME = 1000000000 / FPS;

    // Freeze frame
    private static int framesPassed = 0;
    private static int framesToFreeze = 0;
    private static final float freezeScale = 0.05f;

    static void updateDeltaTime()
    {
        currentTime = System.nanoTime();
        updateLength = currentTime - lastLoopTime;
        lastLoopTime = currentTime;
        deltaTime = (updateLength / (double) TARGET_TIME) * scale;

        if (framesPassed < framesToFreeze) framesPassed++;
        else if (scale == freezeScale) scale = 1;
    }

    static long calculateWaitTime()
    {
        return (lastLoopTime - System.nanoTime() + TARGET_TIME) / 1000000;
    }

    public static void freeze(int frames)
    {
        framesPassed = 0;
        framesToFreeze = frames;
        scale = freezeScale;
    }

    public static long getCurrentTime()
    {
        return currentTime / 100000;
    }
}
