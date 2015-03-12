package org.voidsink.kussslib;

/**
 * Enumeration contains all possible grades for courses/exams in KUSSS.
 * The possible grades are listed in the following:
 * - G1 = Sehr gut (very good)
 * - G2 = Gut (good)
 * - G3 = Befriedigend (satisfying)
 * - G4 = Genügend (sufficient)
 * - G5 = Nicht genügend (not sufficient)
 * - GET = Mit Erfolg teilgenommen (Participated with success)
 * - GB = Bestanden (passed)
 * - GAB = Mit Auszeichnung bestanden (passed with distinction)
 * 
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
public enum Grade {
    G1(1, true, true), G2(2, true, true), G3(3, true, true), G4(4, true, true), G5(5, false, true),
    GET(1, true, false), GB(1, true, false), GAB(1, true, false);

    // TODO add "nicht teilgenommen" NT(5, false, false)
    
    private final boolean isPositive;
    private final boolean isNumber;
    private int value;

    private Grade(int value, boolean isPositive, boolean isNumber) {
        this.value = value;
        this.isPositive = isPositive;
        this.isNumber = isNumber;
    }
    
    /**
     * Get the numeric value that corresponds to the grade.
     * @return Numeric grade value
     */
    public int getValue() {
        return value;
    }

    
    /**
     * Checks whether the grade is a numeric assessment (1 - 5) or a textual assessment.
     * @return true if the grade is a numeric assessment, else false
     */
    public boolean isNumber() {
        return isNumber;
    }

    
    /**
     * Check whether the grade is positive.
     * @return True if the grade is positive, else false
     */
    public boolean isPositive() {
        return isPositive;
    }
    
    
    /**
     * Receives a string and parses the correct grade from it.
     * @param text The string which describes the grade
     * @return The grade parsed from the string
     */
    public static Grade parseGrade(String text) {
        text = text.trim().toLowerCase();
        if (text.equals("sehr gut")) {
            return G1;
        } else if (text.equals("gut")) {
            return G2;
        } else if (text.equals("befriedigend")) {
            return G3;
        } else if (text.equals("genügend")) {
            return G4;
        } else if (text.equals("nicht genügend")) {
            return G5;
        } else if (text.equals("mit erfolg teilgenommen")) {
            return GET;
        } else if (text.equals("bestanden")) {
            return GB;
        } else if (text.equals("mit auszeichnung bestanden")) {
            return GAB;
        } else {
            return null;
        }
    }

    /**
     * Retrieves the grade corresponding to the passed numeric value.
     * @param ordinal Numeric value
     * @return Grade which corresponds to the passed value
     */
    public static Grade parseGradeType(int ordinal) {
        return Grade.values()[ordinal];
    }
}

