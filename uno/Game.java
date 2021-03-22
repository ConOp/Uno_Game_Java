package uno;

import java.util.*;

public class Game {
    List<Player> players;
    Deck drawDeck;
    Stack<Card> discardDeck;
    String order;
    int currentPlayer;
    int dealer;
    int winner;
    int playerCount;
    int winningScore;

    /**
     * Basic constructor of every game, user sets the players and the score.
     * @param pCount
     * @param winScore
     */
    public Game(int pCount,int winScore){
        playerCount=pCount;
        winningScore=winScore;
        players=new ArrayList<>();
    }

    /**
     * The basic method of the game, all game functions run in here.
     */
    public void StartNewRound(){
        dealer=GetDealer();//Get the dealer of the game.
        System.out.println("Dealer is " + dealer);
        currentPlayer=dealer;//Set first player as dealer so that the first card applies to the next player.
        order="Normal";//Turn order is set to Normal.
        discardDeck= new Stack<>();//Initialize discard deck.
        drawDeck = new Deck();//Deck to start game.
        drawDeck.shuffle();//Shuffle the deck first.
        InitializePlayersHand();//Every player gets 7 cards to start the game.
        Card firstCard = drawDeck.draw();
        while (firstCard.getValue().equals("choose color")||firstCard.getValue().equals("+4")){//If first card is wild or wild draw 4
            drawDeck.add(firstCard);//put card back in deck
            drawDeck.shuffle();//Shuffle again.
            firstCard=drawDeck.draw();
        }
        System.out.println("First card is drawn: " + firstCard.getColor()+" "+firstCard.getValue());
        ExecuteCardAbility(firstCard);
        discardDeck.add(firstCard);
        while(true){
            Card playedCard=players.get(currentPlayer).PlayMatchingCard(discardDeck.peek());
            if(playedCard==null){//If player does not have a playable card.
                System.out.println("Player: " + currentPlayer+" doesn't have a valid card and has to pick one from deck");
                CheckIfCardsNeeded(1);//Check if deck has cards.
                players.get(currentPlayer).drawCard(drawDeck.draw(1));//Player picks one up from the deck.
                playedCard=players.get(currentPlayer).PlayMatchingCard(discardDeck.peek());
                if(playedCard!=null) {//Player checks again if he has a valid card after picking one from the deck.
                    discardDeck.push(playedCard);//Player throws the card from hand and it gets added in the discard deck.
                    System.out.println("Player: " + currentPlayer+" throws card: "+playedCard.getColor()+" "+playedCard.getValue());
                    if(players.get(currentPlayer).HasUno()){
                        System.out.println("Player: "+currentPlayer+" declares UNO!");
                    }
                    ExecuteCardAbility(playedCard);//If it's a special card execute it's ability.
                }
                else{
                    System.out.println("Player: " + currentPlayer+" still doesn't have a valid card to play");
                }
            }
            else{
                discardDeck.push(playedCard);//Player throws the card from hand and it gets added in the discard deck.
                System.out.println("Player: " + currentPlayer+" throws card: "+playedCard.getColor()+" "+playedCard.getValue());
                if(players.get(currentPlayer).HasUno()){
                    System.out.println("Player: "+currentPlayer+" declares UNO!");
                }
                if(players.get(currentPlayer).CheckIfWon()){
                    winner=currentPlayer;
                    System.out.println("Player: " + winner+" won the round! Counting score...");
                    break;//If player has no more cards, wins!
                }
                ExecuteCardAbility(playedCard);//If it's a special card execute it's ability.
            }
            currentPlayer=GetNextPlayer();


        }
        //When code enters here, someone won the round and we need to count the score the round-winner got.
        for(int i=0;i<players.size();i++){
            if(i!=winner){
                players.get(winner).AddScore(players.get(i).GetHandScore());
            }
        }

    }

    /**
     * Checks if the winner of the round has enough points to win the whole game.
     * @return True if winner found, false if not.
     */
    public boolean CheckIfPlayerWonGame(){
        if(!players.isEmpty()) {
            if (players.get(winner).getScore() >= winningScore) {
                System.out.println("Player "+winner+" won the game with score: "+players.get(winner).getScore()+"\n");
                System.out.println("Scoreboard");
                for(int i=0;i<playerCount;i++){
                    System.out.println("Player: "+i+" Score: "+players.get(i).getScore());
                }
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Initializes all players hands for the start of the game (each player gets 7 cards).
     */
    public void InitializePlayersHand(){
        for(int i=0;i<playerCount;i++){
            players.get(i).drawCard(drawDeck.draw(7));
        }
    }

    /**
     * Test round to determine who is gonna be the dealer of the round.
     * @return Index of the player who is the current dealer.
     */
    private int GetDealer(){
        Deck tempDeck = new Deck();//Temporary deck to declare the dealer.
        tempDeck.shuffle();
        if(players.isEmpty()) {//If it's the very first round of the game
            for (int i = 0; i < playerCount; i++) {
                players.add(new Player(new Hand(tempDeck.draw(1))));//Create players and give each player 1 card to determine the dealer.
            }
        }
        else{//If players already exist.
            for (int i = 0; i < playerCount; i++) {
                players.get(i).drawCard(tempDeck.draw(1));//Every player draws 1 card to determine the dealer.
            }
        }
        int maxScore=-1;
        for(int i=0;i<playerCount;i++){
            if(players.get(i).GetHandScore()>maxScore){//Player with bigger score is the dealer.
                maxScore=players.get(i).GetHandScore();
                dealer=i;
            }
            players.get(i).ClearHand();//Clear players hand.

        }
        return dealer;
    }

    /**
     * Gets the player who has to play next depending on the order ( Normal or Reversed).
     * @return Index of next player.
     */
    private int GetNextPlayer(){
        if(order.equals("Normal")){//If order is set to normal return the next player.
            return  (currentPlayer+1)%players.size();
        }
        else{//If order is reversed return the previous player.
            return (currentPlayer-1)<0? 3:(currentPlayer-1);
        }
    }

    /**
     * Checks if the played card has a special ability and executes it's ability depending on the rules of UNO.
     * @param card
     */
    private void ExecuteCardAbility(Card card){
        if(!card.isNumber()) {
            switch (card.getValue()) {
                case "+2":
                    CheckIfCardsNeeded(2);//Check if deck has cards.
                    players.get(GetNextPlayer()).drawCard(drawDeck.draw(2));//Next player draws 2 cards.
                    System.out.println("Player: " + GetNextPlayer() + " gets 2 cards and loses the turn!");
                    currentPlayer = GetNextPlayer();//Next player forfeit turn.

                    break;
                case "swap":
                    if (order.equals("Reversed")) {
                        System.out.println("Order is set to: Normal");
                        order = "Normal";
                    } else {
                        System.out.println("Order is set to: Reversed");
                        order = "Reversed";
                    }
                    break;
                case "skip":
                    currentPlayer = GetNextPlayer();//Skip the next player.

                    System.out.println("Skipping player: " + currentPlayer);
                    break;
                case "+4": {
                    CheckIfCardsNeeded(4);//Check if deck has cards.
                    players.get(GetNextPlayer()).drawCard(drawDeck.draw(4));//Next player draws 4 cards.
                    Color col = players.get(currentPlayer).PickColor();
                    card.setColor(col);
                    System.out.println("Player: " + currentPlayer + " decides to change color to: " + col);
                    System.out.println("Player: " + GetNextPlayer() + " gets 4 cards and loses the turn!");
                    currentPlayer = GetNextPlayer();//Next player forfeit turn.
                    break;
                }
                default: {
                    Color col = players.get(currentPlayer).PickColor();
                    card.setColor(col);
                    System.out.println("Player: " + currentPlayer + " decides to change color to: " + col);
                    break;
                }
            }
        }
    }

    /**
     * Checks if the draw deck lacks of cards, if yes then it gets all the cards except from the top one out of the discard deck,
     * it places them in the draw deck and then it shuffles the draw deck.
     * @param cardsDrawn
     */
    private void CheckIfCardsNeeded(int cardsDrawn){
        if(cardsDrawn>=drawDeck.cardsLeft()){//If there are less cards in the deck that the cards that should be drawn.
            Card topCard =discardDeck.pop();//Gets the top card to be placed again at the top of discard deck after the operation.
            while(discardDeck.size()>1){//Take all the cards from discard deck and place them in drawDeck.
                drawDeck.add(discardDeck.pop());
            }
            drawDeck.shuffle();//Shuffle deck to make it ready to play.
            discardDeck.push(topCard);//Put back on top the card that was on top before the oparation.
        }
    }
}
