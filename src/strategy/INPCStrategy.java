package strategy;

import ch.aplu.jcardgame.Card;
import gameRound.GameRound;
import Player.*;

public interface INPCStrategy {
    public Card playCard(NPC npc);
}
