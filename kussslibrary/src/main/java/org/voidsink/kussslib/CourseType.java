package org.voidsink.kussslib;


/**
 * Enumeration contains all possible course types in KUSSS.
 * Detailed explanation:
 * - VL = Vorlesung (normal lecture)
 * - VO = Vorlesung (normal lecture)
 * - KV = Kombinationsveranstaltung (combined lecture)
 * - UE = Uebung (exercise)
 * - KS
 * - SE = Seminar (seminar)
 * - PR = Praktikum (practical training)
 * - UNDEFINED = no defined type
 * 
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
public enum CourseType {
	VL, VO, KV, UE, KS, SE, PR, UNDEFINED;
	
    /**
     * Receives a string and parses the correct course type from it.
     * @param text The string which describes the course type
     * @return The course type parsed from the string
     */
    public static CourseType parseCourseType(String text) {
        text = text.trim().toLowerCase();
        if (text.equals("VL")) {
            return VL;
        } else if (text.equals("VO")) {
            return VO;
        } else if (text.equals("KV")) {
            return KV;
        } else if (text.equals("UE")) {
            return UE;
        } else if (text.equals("KS")) {
            return KS;
        } else if (text.equals("SE")) {
            return SE;
        } else if (text.equals("PR")) {
            return PR;
        } else {
            return UNDEFINED;
        }
    }
}
