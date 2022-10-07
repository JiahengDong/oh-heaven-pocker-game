package gameRound;

import Player.Player;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import oh_heaven.game.BrokeRuleException;
import oh_heaven.game.Oh_Heaven;
import util.CardUtil.*;
import util.RandomUtil;
import util.CardUtil;
import Player.*;
import java.util.ArrayList;


public class GameRound{
    private Hand[] hands;
    private int[] scores;
    private int[] tricks;
    private int[] bids;
    private Hand trick;
    private int winner;
    private Card winningCard;
    private Suit trumps;
    private Suit lead;
    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    private Oh_Heaven game;
    private Card selected = null;
    public GameRound(Oh_Heaven game){
        this.game = game;
        this.scores = new int[game.nbPlayers];
        this.tricks = new int[game.nbPlayers];
        this.bids = new int[game.nbPlayers];
    }

    private void dealingOut(Hand[] hands, int nbPlayers, int nbCardsPerPlayer) {
        Hand pack = deck.toHand(false);
        // pack.setView(Oh_Heaven.this, new RowLayout(hideLocation, 0));
        for (int i = 0; i < nbCardsPerPlayer; i++) {
            for (int j=0; j < nbPlayers; j++) {
                if (pack.isEmpty()) return;
                Card dealt = RandomUtil.randomCard(pack);
                // System.out.println("Cards = " + dealt);
                dealt.removeFromHand(false);
                hands[j].insert(dealt, false);
                // dealt.transfer(hands[j], true);
            }
        }
    }

    public void setHandCards(int nbPlayers, int nbStartCards, ArrayList<Player> players){
        hands = new Hand[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            hands[i] = new Hand(deck);
            players.get(i).setHand(hands[i]);
        }
        dealingOut(hands, nbPlayers, nbStartCards);
        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
        }
    }

    public void initBids(int nextPlayer, int nbPlayers, int nbStartCards) {
        int total = 0;
        for (int i = nextPlayer; i < nextPlayer + nbPlayers; i++) {
            int iP = i % nbPlayers;
            bids[iP] = nbStartCards / 4 + RandomUtil.random.nextInt(2);
            //set each player's bid
            total += bids[iP];
        }
        if (total == nbStartCards) {  // Force last bid so not every bid possible
            int iP = (nextPlayer + nbPlayers) % nbPlayers;
            if (bids[iP] == 0) {
                bids[iP] = 1;
                //set each player's bid

            } else {
                bids[iP] += RandomUtil.random.nextBoolean() ? -1 : 1;
                //set each player's bid

            }
        }
        // for (int i = 0; i < nbPlayers; i++) {
        // 	 bids[i] = nbStartCards / 4 + 1;
        //  }
    }

    public void initTrick(){
        this.trick = new Hand(deck);
    }

    public void initScores(int nbPlayers) {
        for (int i = 0; i < nbPlayers; i++) {
            scores[i] = 0;
        }
    }

    public void initTricks(int nbPlayers){
        for (int i = 0; i < nbPlayers; i++) {
            tricks[i] = 0;
        }
    }

    public void updateScores(int nbPlayers, int madeBidBonus) {
        for (int i = 0; i < nbPlayers; i++) {
            scores[i] += tricks[i];
            if (tricks[i] == bids[i]) scores[i] += madeBidBonus;
        }
    }

    public void startLead(int nextPlayer, ArrayList<Player> players){
        //initialize the card on board
        trick = new Hand(deck);
        game.delay(game.thinkingTime);
        selected = players.get(nextPlayer).play();
        game.playWithSelectedCard(trick, selected);
        // No restrictions on the card being lead
        lead = (Suit) selected.getSuit();
        selected.transfer(trick, true); // transfer to trick (includes graphic effect)
        winner = nextPlayer;
        winningCard = selected;
        // End Lead
    }

    public void startAfterLead(int nextPlayer, ArrayList<Player> players){
        game.delay(game.thinkingTime);
        selected = players.get(nextPlayer).play();
        // Follow with selected card
        game.playWithSelectedCard(trick,selected);
        // Check: Following card must follow suit if possible
        checkValidation(nextPlayer);
        // End Check
        selected.transfer(trick, true); // transfer to trick (includes graphic effect)
        System.out.println("winning: " + winningCard);
        System.out.println(" played: " + selected);
        // System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + (13 - winningCard.getRankId()));
        // System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " + (13 -    selected.getRankId()));
        if ( // beat current winner with higher card
                (selected.getSuit() == winningCard.getSuit() && CardUtil.rankGreater(selected, winningCard)) ||
                        // trumped when non-trump was winning
                        (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
            System.out.println("NEW WINNER");
            winner = nextPlayer;
            winningCard = selected;
        }
        // End Follow
    }

    public void checkValidation(int nextPlayer){
        if (selected.getSuit() != lead && hands[nextPlayer].getNumberOfCardsWithSuit(lead) > 0) {
            // Rule violation
            String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
            System.out.println(violation);
            if (game.enforceRules)
                try {
                    throw(new BrokeRuleException(violation));
                } catch (BrokeRuleException e) {
                    e.printStackTrace();
                    System.out.println("A cheating player spoiled the game!");
                    System.exit(0);
                }
        }
    }

    public void setTrumpSuit(){
        this.trumps = RandomUtil.randomEnum(Suit.class);
    }
    public Suit getTrumpSuit(){
        return this.trumps;
    }

    public Hand[] getHands() {
        return hands;
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
        return lead;
    }

    public void setLead(Suit lead) {
        this.lead = lead;
    }

    public Card getSelected() {
        return selected;
    }

    public void setSelected(Card selected) {
        this.selected = selected;
    }

    public void updateGameRound(){
        this.trick = null;
        this.winner = 0;
        this.winningCard = null;
        this.lead = null;
    }

    public void notifyPlayers(ArrayList<Player> players){
        for(Player player: players){
            if(player instanceof NPC){
                ((NPC) player).setTrick(this.trick);
                ((NPC) player).setLead(this.lead);
                ((NPC) player).setBids(this.bids);
                ((NPC) player).setScores(this.scores);
                ((NPC) player).setTricks(this.tricks);
                ((NPC) player).setTrumps(this.trumps);
                ((NPC) player).setWinner(this.winner);
                ((NPC) player).setWinningCard(this.winningCard);
                ((NPC) player).setPrevSelected(this.selected);
            }
        }
    }
}
