package Entity;

public class InvulnerableTime implements Runnable
{
    private long startTime;
    private long time;
    private Player player;
    
    public InvulnerableTime(long startTime, long time, Player player)
    {
        this.startTime = startTime;
        this.time = time;
        this.player = player;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            long now = System.currentTimeMillis() / 1000;
            long elapsed = now - startTime;
            
            if(elapsed == (time /2))
            {
                player.setInvulnerable(false);
            }
            
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
