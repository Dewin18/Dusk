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
    
    public static void setChoice(int choice)
    {
        currentChoice = choice;
    }
    
    public static int getDifficultyChoice(String mode)
    {
        switch(mode)
        {
            case "ON"     : return 0;
            case "OFF"    : return 1;
            case "EASY"   : return 0;
            case "MEDIUM" : return 1;
            case "HARD"   : return 2;
            default       : return 0;
        }
    }
}
