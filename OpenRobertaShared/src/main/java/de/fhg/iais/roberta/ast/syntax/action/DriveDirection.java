package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * Direction in which the robot will drive.
 */
public enum DriveDirection {
    FOREWARD( "OFF" ), BACKWARD( "ON" );

    private final String[] values;

    private DriveDirection(String... values) {
        this.values = values;
    }

    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * Get direction from {@link DriveDirection} from string parameter. It is possible for one direction to have multiple string mappings.
     * Throws exception if the direction does not exists.
     * 
     * @param name of the direction
     * @return name of the direction from the enum {@link DriveDirection}
     */
    public static DriveDirection get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid direction: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( DriveDirection sp : DriveDirection.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.values ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid direction: " + s);
    }
}