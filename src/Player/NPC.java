package Player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import gameRound.GameRound;
import oh_heaven.game.Oh_Heaven;
import strategy.INPCStrategy;
import strategy.NPCStrategyFactory;
import util.CardUtil.*;

public class NPC extends Player{
    //public enum NPCType {SMART, LEGAL, RANDOM};
    private String type;
    private INPCStrategy strategy;
    private int[] scores;
    private int[] tricks;
    private int[] bids;
    private Hand trick;
    private int winner;
    private Card winningCard;
    private Suit trumps;
    private Suit lead;
    private Card prevSelected;

    public NPC(int index) {
        super(index);
    }

    @Override
    public Card play() {
        strategy = NPCStrategyFactory.getInstance().getStrategy(this);
        return strategy.playCard(this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public void setTricks(int[] tricks) {
        this.tricks = tricks;
    }

    public void setBids(int[] bids) {
        this.bids = bids;
    }

    public void setTrick(Hand trick) {
        this.trick = trick;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public void setWinningCard(Card winningCard) {
        this.winningCard = winningCard;
    }

    public void setTrumps(Suit trumps) {
        this.trumps = trumps;
    }

    public void setLead(Suit lead) {
        this.lead = lead;
    }

    public void setPrevSelected(Card prevSelected) {
        this.prevSelected = prevSelected;
    }

    public int[] getScores() {
        return scores;
    }

    public int[] getTricks() {
        return tricks;
    }

    public int[] getBids() {
        return bids;
    }

    public Hand getTrick() {
        return trick;
    }

    public int getWinner() {
        return winner;
    }

    public Card getWinningCard() {
        return winningCard;
    }

    public Suit getTrumps() {
        return trumps;
    }

    public Suit getLead() {
        return this.lead;
    }

    public Card getPrevSelected() {
        return prevSelected;
    }

}
