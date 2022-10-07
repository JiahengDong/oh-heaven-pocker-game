package Player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import gameRound.GameRound;
import oh_heaven.game.Oh_Heaven;

public class humanPlayer extends Player{
    private Card selected;

    public humanPlayer(int index) {
        super(index);
    }

    @Override
    public Card play() {
        selected = null;
        super.getHand().setTouchEnabled(true);
        while (selected == null) {
            Oh_Heaven.delay(100);
        };
        super.getHand().setTouchEnabled(false);
        return selected;
    }

    public void setupCardListener(){
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) { selected = card; humanPlayer.super.getHand().setTouchEnabled(false); }
        };
        super.getHand().addCardListener(cardListener);
    }

}
