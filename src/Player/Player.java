package Player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import gameRound.GameRound;
import oh_heaven.game.Oh_Heaven;
import util.CardUtil;
import util.CardUtil.*;
public abstract class Player {
    private Hand hand;
    private int index;

    public Player(int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    public abstract Card play();

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

}
