package com.mygdx.game.shared.position.ship;

import com.mygdx.game.shared.position.ship.filter.FireFilter;
import com.mygdx.game.shared.position.ship.filter.ImperialFilter;

/**
 * Created by platypus on 02.06.16.
 */
public final class ShipFactory{

    private ShipFactory(){
        //not called
    }

    /**
     * Creates a new Ship with a given sequence of Filters.
     *
     * @param sequence The Sequence of Filters.
     * @return The Ship after applying the filters.
     */
    public static Ship createShip(String sequence){
        assert sequence != null;

        return extendShip(new BaseShip(), sequence);
    }

    /**
     * Extends a ship with a sequence of Filters.
     *
     * @param ship     The ship that shall be extended.
     * @param sequence The Sequence of chars identifiing the filters.
     * @return The Ship after applying the filters.
     */
    @SuppressWarnings("Duplicates")
    public static Ship extendShip(Ship ship, String sequence){
        assert ship != null;
        assert sequence != null;

        Ship result = ship;
        if(!sequence.equals(ship.toString())){
            if(sequence.startsWith(ship.toString())){
                String restSequence = sequence.substring(ship.toString().length());
                for(final char id : restSequence.toCharArray()){
                    result = extendShip(result, id);
                }
            }else{
                return createShip(sequence);
            }
        }
        return result;
    }

    /**
     * Extends a ship with one filter.
     *
     * @param ship The Ship that shall be extended.
     * @param id   The Filter extension.
     * @return The Ship after applying the filter.
     */
    private static Ship extendShip(Ship ship, char id){
        assert ship != null;
        assert id != '\0';
        Ship result = ship;

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
