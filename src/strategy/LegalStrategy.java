package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import gameRound.GameRound;
import oh_heaven.game.Oh_Heaven;
import util.RandomUtil;
import util.RandomUtil.*;
import util.CardUtil.*;
import Player.*;
import java.util.ArrayList;

public class LegalStrategy implements INPCStrategy{

    @Override
    public Card playCard(NPC npc) {
        Suit lead = npc.getLead();
        Hand cards = npc.getHand();

        // if the first one in this round to take the lead
        if (lead == null) {
            return RandomUtil.randomCard(cards);
        }

        ArrayList<Card> cardsWithLeadSuit = cards.getCardsWithSuit(lead);

        if (cardsWithLeadSuit.size() > 0) {
            return cardsWithLeadSuit.get(0); // have to follow the lead
        }else {
            return RandomUtil.randomCard(cards);
        }
    }
}
