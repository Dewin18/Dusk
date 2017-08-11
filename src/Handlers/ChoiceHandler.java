package Handlers;

public class ChoiceHandler
{
    private static int currentChoice = 0;
    
    public static void selectNextUp(int selectionLength)
    {
        currentChoice--;
        if (currentChoice == -1)
        {
            currentChoice = selectionLength - 1;
        }
    }
    
    public static void selectNextDown(int selectionLength)
    {
        currentChoice++;
        if (currentChoice == selectionLength)
        {
            currentChoice = 0;
        }
    }
    
    public static int getChoice()
    {
        return currentChoice;
    }
    
    public static int getChoiceAt(int[] choices, int index)
    {
        return choices[index];
    }
    
    public static void setChoice(int choice)
    {
        currentChoice = choice;
    }
    
    public static int getChoice(String mode)
    {
        if(mode.equals("ON")) return 0;
        else if(mode.equals("OFF")) return 1;
        else if(mode.equals("EASY")) return 0;
        else if(mode.equals("MEDIUM")) return 1;
        else if(mode.equals("HARD")) return 2;
        else return 0;
    }
}
