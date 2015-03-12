package org.voidsink.kussslib;

/**
 * Enumeration contains all possible assessment types in KUSSS.
 * 
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
public enum AssessmentType {
	INTERIM_COURSE_ASSESSMENT, FINAL_COURSE_ASSESSMENT,
	RECOGNIZED_COURSE_CERTIFICATE, RECOGNIZED_EXAM,
	RECOGNIZED_ASSESSMENT, FINAL_EXAM, ALL, NONE_AVAILABLE;

	/**
	 * Receives a string and parses the correct assessment type from it.
	 * @param text The string which describes the assessment type
	 * @return The assessment type parsed from the string
	 */
	public static AssessmentType parseAssessmentType(String text) {
		text = text.trim().toLowerCase();
		if (text.equals("vorläufige lehrveranstaltungsbeurteilungen")
				|| text.equals("interim course assessments")) {
			return INTERIM_COURSE_ASSESSMENT;
		} else if (text.equals("lehrveranstaltungsbeurteilungen")
				|| text.equals("course assessments")) {
			return FINAL_COURSE_ASSESSMENT;
		} else if (text.equals("sonstige beurteilungen")
				|| text.equals("recognized course certificates (ilas)")) {
			return RECOGNIZED_COURSE_CERTIFICATE;
		} else if (text.equals("anerkannte beurteilungen")
				|| text.equals("recognized assessments")) {
			return RECOGNIZED_ASSESSMENT;
		} else if (text.equals("prüfungen")
				|| text.equals("exams")) {
			return RECOGNIZED_EXAM;
		} else if (text.equals("anerkannte prüfungen")
				|| text.equals("recognized exams")) {
			return RECOGNIZED_EXAM;
		} else {
			return null;
		}		
	}
}

