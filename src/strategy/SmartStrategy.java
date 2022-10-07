package strategy;

import Player.NPC;
import ch.aplu.jcardgame.Card;
import util.CardUtil.*;
import ch.aplu.jcardgame.Hand;

public class SmartStrategy implements INPCStrategy{
    public Card leastcard(NPC npc){
        int x = -1;
        if(this.havesamesuit(npc)){
            for(int i =0;i < npc.getHand().getNumberOfCards();i++){
                if(x == -1){
                    if(npc.getHand().get(i).getSuit()==npc.getLead()){
                        x = i;
                    }
                }
                else if(npc.getHand().get(i).getSuit()==npc.getLead() && npc.getHand().get(i).getRankId() > npc.getHand().get(x).getRankId()){
                    x = i;
                }
            }
            return npc.getHand().get(x);
        }
        else{
            for(int i =0;i < npc.getHand().getNumberOfCards();i++){
                if(x == -1){
                    if(npc.getHand().get(i).getSuit() != npc.getTrumps()){
                        x = i;
                    }
                }
                else if(npc.getHand().get(i).getRankId() > npc.getHand().get(x).getRankId() && npc.getHand().get(i).getSuit() != npc.getTrumps()){
                    x = i;
                }
            }
            if(x == -1){
                for(int i =0;i < npc.getHand().getNumberOfCards();i++){
                    if(x == -1){
                        x = i;
                    }
                    else if(npc.getHand().get(i).getRankId() > npc.getHand().get(x).getRankId()){
                        x = i;
                    }
                }
            }
            return npc.getHand().get(x);
        }
    }

    public Card handcardwinleast(NPC npc){
        int x = -1;
        if(this.havesamesuit(npc)){
            if(npc.getWinningCard().getSuit() == npc.getTrumps() && npc.getLead() != npc.getTrumps()){
                return null;
            }
            else{
                for(int i =0;i < npc.getHand().getNumberOfCards();i++){
                    if(x == -1){
                        if(npc.getHand().get(i).getSuit() == npc.getLead() && npc.getHand().get(i).getRankId() < npc.getWinningCard().getRankId()){
                            x = i;
                        }
                    }
                    else{
                        if(npc.getHand().get(i).getSuit() == npc.getLead() && npc.getHand().get(i).getRankId() < npc.getWinningCard().getRankId() && npc.getHand().get(i).getRankId() > npc.getHand().get(x).getRankId()){
                            x = i;
                        }
                    }
                }
                if(x == -1) return null;
                return npc.getHand().get(x);
            }
        }
        else if(this.havetrump(npc)){
            for(int i =0;i < npc.getHand().getNumberOfCards();i++){
                if(x == -1){
                    if((npc.getHand().get(i).getSuit() == npc.getWinningCard().getSuit() && npc.getHand().get(i).getSuit() == npc.getTrumps() && npc.getHand().get(i).getRankId() < npc.getWinningCard().getRankId()) ||
                            (npc.getHand().get(i).getSuit() == npc.getTrumps() && npc.getWinningCard().getSuit() != npc.getTrumps()) ){
                        x = i;
                    }
                }
                else{
                    if((npc.getHand().get(i).getSuit() == npc.getHand().get(x).getSuit() && npc.getHand().get(i).getSuit() == npc.getWinningCard().getSuit() && npc.getHand().get(i).getRankId() < npc.getWinningCard().getRankId() && npc.getHand().get(i).getRankId() > npc.getHand().get(x).getRankId())
                            || (npc.getWinningCard().getSuit() != npc.getTrumps() && npc.getHand().get(i).getRankId() > npc.getHand().get(x).getRankId() && npc.getHand().get(i).getSuit() == npc.getHand().get(x).getSuit())){
                        x = i;
                    }
                }
            }
            if(x == -1) return null;
            return npc.getHand().get(x);
        }
        else{ return null;}
    }

    public boolean havesamesuit(NPC npc){
        for(int i =0;i < npc.getHand().getNumberOfCards();i++){
            if(npc.getHand().get(i).getSuit() == npc.getLead()){
                return true;
            }
        }
        return false;
    }
    public boolean havetrump(NPC npc){
        for(int i =0;i < npc.getHand().getNumberOfCards();i++){
            if(npc.getHand().get(i).getSuit() == npc.getTrumps()){
                return true;
            }
        }
        return false;
    }
    @Override
    public Card playCard(NPC npc) {
        Suit lead = npc.getLead();
        //Hand cards = npc.getHand();
        //play the least card when being lead
        if (lead == null) {
            return leastcard(npc);
        }

        else{
            if(handcardwinleast(npc) == null){
                return leastcard(npc);
            }else{ return handcardwinleast(npc);
            }
        }

    }
}
