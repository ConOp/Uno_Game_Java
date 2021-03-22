package uno;

import java.util.List;

public class Player {
    private Hand hand;
    private int score;
    public Player(Hand hand) {
        this.hand = hand;
        score=0;
    }
    /**
     * Method to find a valid card to play depending on the one which is on top of discard deck.
     * @param otherCard
     * @return The card to be played by the player.
     */
    public Card PlayMatchingCard(Card otherCard){
        for(int i=0; i<hand.getHandDeck().size();i++){
            if(hand.getHandDeck().get(i).matches(otherCard)){
                return  hand.DropCard(hand.getHandDeck().get(i));
            }
        }
        return null;
    }
    /**
     * Player draws the cards which are present in the list and adds them to hand.
     * @param cards
     */
    public void drawCard(List<Card> cards) {
        for (Card c:cards) {
            hand.drawCard(c);
        }
    }
    /**
     * Player decides which color to set if the card is "choose color" or "+4"
     * @return The color to which the BLACK card color will be changed into depending on the number of cards the player has of this color.
     */
    public Color PickColor(){
        int[] colors = new int[4];
        for (Card c :hand.getHandDeck()) {
            if(c.getColor().equals(Color.BLUE)){
                colors[0]++;
            }
            else if( c.getColor().equals(Color.GREEN)){
                colors[1]++;
            }
            else if (c.getColor().equals(Color.RED)){
                colors[2]++;
            }
            else if(c.getColor().equals(Color.YELLOW)){
                colors[3]++;
            }
        }
        int maxValue=colors[0];
        int index =0;
        for(int i = 0; i < colors.length; i++)
        {
            if(maxValue < colors[i])
            {
                maxValue = colors[i];
                index = i;
            }
        }
        if(index==0) {
            return Color.BLUE;
        }
        else if (index==2) {
            return  Color.GREEN;
        }
        else if (index==3) {
            return Color.RED;
        }
        else {
            return Color.YELLOW;
        }

    }

    /**
     *
     * @return The score of the cards in player hands.
     */
    public int GetHandScore() {
        return hand.calcHandScore();
    }

    /**
     * Empties player hand.
     */
    public void ClearHand(){
        hand.ClearHand();
    }

    /**
     * Checks if the player has no cards.
     * @return true if player has no more cards remaining | false if there are cards in the hand of the player.
     */
    public boolean CheckIfWon(){
        return hand.outOfCards();
    }

    /**
     * Adds the specified score to player's total score.
     * @param sc
     */
    public void AddScore(int sc){
        score+=sc;
    }

    /**
     *
     * @return The player total score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Checks if player has only one card remaining.
     * @return true if only one card remains else false.
     */
    public boolean HasUno(){
        if(hand.getHandDeck().size()==1){
            return true;
        }
        else{
            return false;
        }
    }
}
