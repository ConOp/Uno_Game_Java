package uno;
import java.util.List;

public class Hand
{

    //List of cards that a hand of cards will have.
    private List<Card> handDeck;

    public Hand(List <Card> hDeck) {handDeck= hDeck;}


    /**
     * Adds a drawed card in player's hand.
     * @param c
     */
    public void drawCard(Card c)
    {
        handDeck.add(c);
    }

    /**
     * Drops a card from hand.
     * @param c
     * @return The dropped card.
     */
    public Card DropCard(Card c){
        handDeck.remove(c);
        return c;
    }

    /**
     *
     * @return True if hand is out of cards, false if not.
     */
    public boolean outOfCards() {
        return handDeck.isEmpty();
    }

    /**
     * Calculates the player hand's score.
     * @return The hand score.
     */
    public int calcHandScore() {
        int total = 0;

        for (Card card: handDeck) {
            if(card.isNumber()) {
                total += Integer.parseInt(card.getValue());
            }
            else {
                if (card.getValue().equals("+2") || card.getValue().equals("skip") || card.getValue().equals("swap")) {
                    total += 20;
                } else {
                    total += 50;
                }
            }
        }
        return total;
    }

    /**
     * Drops all cards from player's hand (clears the hand).
     */
    public void ClearHand(){
        handDeck.clear();
    }

    /**
     *
     * @return The player's hand of cards.
     */
    public List<Card> getHandDeck() {
        return handDeck;
    }
}
