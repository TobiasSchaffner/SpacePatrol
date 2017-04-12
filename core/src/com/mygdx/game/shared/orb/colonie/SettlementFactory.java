package com.mygdx.game.shared.orb.colonie;

import com.mygdx.game.shared.orb.colonie.filter.FireFilter;
import com.mygdx.game.shared.orb.colonie.filter.ImperialFilter;

/**
 * Created by platypus on 02.06.16.
 */
public class SettlementFactory{

    private Settlement createSettlement(String sequence){
        assert sequence != null;

        return extendSettlement(new BaseSettlement(), sequence);
    }

    @SuppressWarnings("Duplicates")
    public Settlement extendSettlement(Settlement settlement, String sequence){
        assert settlement != null;
        assert sequence != null;

        Settlement result = settlement;
        if(!sequence.equals(settlement.toString())){
            if(sequence.startsWith(settlement.toString())){
                String restSequence = sequence.substring(settlement.toString().length());
                for(final char id : restSequence.toCharArray()){
                    result = extendSettlement(result, id);
                }
            }else{
                return createSettlement(sequence);
            }
        }
        return result;
    }

    public Settlement extendSettlement(Settlement settlement, char id){
        assert settlement != null;
        assert id != '\0';
        Settlement result = settlement;

        // List of classes that can be created by the Factory
        if(id == 'F'){
            result = new FireFilter(result);
        }else if(id == 'I'){
            result = new ImperialFilter(result);
        }else{
            throw new AssertionError("Class " + id + " does not exist!");
        }
        return result;
    }
}
