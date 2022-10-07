package oh_heaven.game;

// Oh_Heaven.java

import Player.Player;
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import gameRound.GameRound;
import util.RandomUtil;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.util.stream.Collectors;
import util.PropertiesLoader;
import Player.*;
import Player.NPC;
@SuppressWarnings("serial")
public class Oh_Heaven extends CardGame {

  
  final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};

  private GameRound gr;

  private final String version = "1.0";
  public final int nbPlayers = 4;
  public int nbStartCards = 13;
  public int nbRounds = 3;
  public int madeBidBonus = 10;
  private final int handWidth = 400;
  private final int trickWidth = 40;

  private final Location[] handLocations = {
			  new Location(350, 625),
			  new Location(75, 350),
			  new Location(350, 75),
			  new Location(625, 350)
	  };
  private final Location[] scoreLocations = {
			  new Location(575, 675),
			  new Location(25, 575),
			  new Location(575, 25),
			  // new Location(650, 575)
			  new Location(575, 575)
	  };
  private Actor[] scoreActors = {null, null, null, null };
  private final Location trickLocation = new Location(350, 350);
  private final Location textLocation = new Location(350, 450);
  public int thinkingTime = 2000;

  private Location hideLocation = new Location(-500, - 500);
  private Location trumpsActorLocation = new Location(50, 50);
  public boolean enforceRules=false;

  public void setStatus(String string) { setStatusText(string); }

Font bigFont = new Font("Serif", Font.BOLD, 36);

private void initScore() {
	 for (int i = 0; i < nbPlayers; i++) {
		 // scores[i] = 0;
		 String text = "[" + String.valueOf(gr.getScores()[i]) + "]" + String.valueOf(gr.getTricks()[i]) + "/" + String.valueOf(gr.getBids()[i]);
		 scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
		 addActor(scoreActors[i], scoreLocations[i]);
	 }
  }

private void updateScore(int player) {
	removeActor(scoreActors[player]);
	String text = "[" + String.valueOf(gr.getScores()[player]) + "]" + String.valueOf(gr.getTricks()[player]) + "/" + String.valueOf(gr.getBids()[player]);
	scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
	addActor(scoreActors[player], scoreLocations[player]);
}

private void initRound() {
		//do initilize hand, dealing out and sort
		gr.setHandCards(nbPlayers, nbStartCards, players);
		 // Set up human player for interaction
		for (Player player : players) {
			if (player instanceof humanPlayer) {
				((humanPlayer) player).setupCardListener();
			}
		}
		 // graphics
	    RowLayout[] layouts = new RowLayout[nbPlayers];
	    for (int i = 0; i < nbPlayers; i++) {
	      layouts[i] = new RowLayout(handLocations[i], handWidth);
	      layouts[i].setRotationAngle(90 * i);
	      // layouts[i].setStepDelay(10);
	      gr.getHands()[i].setView(this, layouts[i]);
	      gr.getHands()[i].setTargetArea(new TargetArea(trickLocation));
	      gr.getHands()[i].draw();
	    }

	    for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide the cards in a hand (make them face down)
	      gr.getHands()[i].setVerso(true);			// You do not need to use or change this code.
	    // End graphics
 }

 public void playWithSelectedCard(Hand trick, Card selected){
	 // Lead with selected card
	 trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
	 trick.draw();
	 selected.setVerso(false);
 }

private void playRound() {
	// Select and display trump suit, can use gr.getTrumpSuit() to access trumps
		gr.setTrumpSuit();
		final Actor trumpsActor = new Actor("sprites/"+trumpImage[gr.getTrumpSuit().ordinal()]);
	    addActor(trumpsActor, trumpsActorLocation);
	// End trump suit
	int nextPlayer = RandomUtil.random.nextInt(nbPlayers); // randomly select player to lead for this round
	//make bids from first player
	gr.initBids(nextPlayer,nbPlayers,nbStartCards);
    // initScore();
    for (int i = 0; i < nbPlayers; i++) updateScore(i);

	for (int i = 0; i < nbStartCards; i++) {
		gr.setLead(null);
		gr.notifyPlayers(players);

		gr.startLead(nextPlayer, players);
		gr.notifyPlayers(players);

		for (int j = 1; j < nbPlayers; j++) {
			if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
			gr.startAfterLead(nextPlayer, players);
			//System.out.println(nextPlayer);
			gr.notifyPlayers(players);
		}

		delay(600);
		gr.getTrick().setView(this, new RowLayout(hideLocation, 0));
		gr.getTrick().draw();
		nextPlayer = gr.getWinner();
		setStatusText("Player " + nextPlayer + " wins trick.");
		gr.getTricks()[nextPlayer]++;
		updateScore(nextPlayer);

		gr.notifyPlayers(players);
	}
	removeActor(trumpsActor);
}

private ArrayList<Player> players = new ArrayList<>();

//create players and initialize nums
private void initGameWithProperties(Properties properties){
	RandomUtil.seed = properties.getProperty("seed") == null ? RandomUtil.seed :
			Integer.parseInt(properties.getProperty("seed"));
	this.nbStartCards = properties.getProperty("nbStartCards") == null ? nbStartCards :
			Integer.parseInt(properties.getProperty("nbStartCards"));
	this.nbRounds = properties.getProperty("rounds") == null ? nbRounds :
			Integer.parseInt(properties.getProperty("rounds"));
	this.enforceRules = properties.getProperty("enforceRules") == null ? enforceRules :
			Boolean.parseBoolean(properties.getProperty("enforceRules"));
	for(int i = 0; i <nbPlayers; i++){
		String playerType = properties.getProperty(String.format("players.%d", i));
		Player newPlayer;
		if(playerType.equals("human")){
			newPlayer = new humanPlayer(i);
		}else{
			newPlayer = new NPC(i);
		}

		if(newPlayer instanceof NPC){
			((NPC) newPlayer).setType(playerType);
		}
		players.add(newPlayer);
	}

}

	public Oh_Heaven(Properties properties)
  {
	super(700, 700, 30);
    setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    setStatusText("Initializing...");
    initGameWithProperties(properties);
    gr = new GameRound(this);
    gr.initScores(nbPlayers);
    initScore();
    for (int i=0; i <nbRounds; i++) {
      gr.initTricks(nbPlayers);
      initRound();
      playRound();
      gr.updateScores(nbPlayers, madeBidBonus);
      gr.notifyPlayers(players);
      gr.updateGameRound();
    };

    for (int i=0; i <nbPlayers; i++) updateScore(i);
    int maxScore = 0;
    for (int i = 0; i <nbPlayers; i++) if (gr.getScores()[i] > maxScore) maxScore = gr.getScores()[i];
    Set <Integer> winners = new HashSet<Integer>();
    for (int i = 0; i <nbPlayers; i++) if (gr.getScores()[i] == maxScore) winners.add(i);
    String winText;
    if (winners.size() == 1) {
    	winText = "Game over. Winner is player: " +
    			winners.iterator().next();
    }
    else {
    	winText = "Game Over. Drawn winners are players: " +
    			String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toSet()));
    }
    addActor(new Actor("sprites/gameover.gif"), textLocation);
    setStatusText(winText);
    refresh();
  }

  public static void main(String[] args)
  {
	// System.out.println("Working Directory = " + System.getProperty("user.dir"));
	final Properties properties;
	if (args == null || args.length == 0) {
	  properties = PropertiesLoader.loadPropertiesFile(null);
	} else {
		properties = PropertiesLoader.loadPropertiesFile(args[0]);
	}
    new Oh_Heaven(properties);
  }
  

}
