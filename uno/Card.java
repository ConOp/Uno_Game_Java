package uno;

public class Card {
    private Color color;
    private String value;

    public Card(Color color, String value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (color != card.color) return false;
        return value.equals(card.value);
    }

    @Override
    public int hashCode() {
        int result = color.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    /**
     * Defines if the card can be played or not depending on the rules of the game.
     * @param otherCard
     * @return true if can be played, false if not.
     */
    public boolean matches(Card otherCard) {
        if (color == Color.BLACK) {
            return true;
        }
        return color == otherCard.color || value.equals(otherCard.value);
    }

    /**
     * Checks if card is a number.
     * @return True if card is a number, false if the card is not a number.
     */
    public boolean isNumber(){
        try{//If card value is integer.
            Integer.parseInt(value);
            if(value.equals("+2")||value.equals("+4")){
                return false;
            }
            return true;
        }
        catch (NumberFormatException e) {//If card value is string.
            return false;
        }
    }



}

