package strategy;

import Player.NPC;
import ch.aplu.jcardgame.Card;
import gameRound.GameRound;
import util.RandomUtil;
import ch.aplu.jcardgame.Hand;

public class RandomStrategy implements INPCStrategy{

    @Override
    public Card playCard(NPC npc) {
        Hand cards = npc.getHand();
        return RandomUtil.randomCard(cards);

    }
}
