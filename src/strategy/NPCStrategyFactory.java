package strategy;
import Player.*;


public class NPCStrategyFactory {

    private static NPCStrategyFactory instance;

    public static NPCStrategyFactory getInstance() {
        if (instance == null)
            instance = new NPCStrategyFactory();
        return instance;
    }

    public INPCStrategy getStrategy(NPC npc) {

        if (npc.getType().equals("legal")) {
            return new LegalStrategy();
        } else if (npc.getType().equals("smart")) {
            return new SmartStrategy();
        }else if(npc.getType().equals("random")){
            return new RandomStrategy();
        } // do we need to have an illgalStrategy in else statement?

        // omit this one
        return new LegalStrategy();

    }
}
