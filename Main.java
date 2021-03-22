import uno.Game;

public class Main {
    public static void main(String[] args) {
        Game UnoGame = new Game(4,500);//Game of 4 players with winning score of 500.
        while(!UnoGame.CheckIfPlayerWonGame()){
            UnoGame.StartNewRound();
        }
    }

}
