package org.voidsink.kussslib.impl;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Parser provides methods for date parsing as well as parser patterns and
 * predefined error messages which can be set for a custom ParseException.
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
class Parser {
	
	
	/* *************************************************************************************
	 * Date formats
	 * *************************************************************************************/
	
	/**
	 * Format of simple date which consists of day (dd) . month (MM) . year (yyyy)
	 */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	/**
	 * Format for simple date with time which consists of day (dd) . month (MM) . year (yyyy)
	 * as well as hour (HH) : minute (mm)
	 */
    public static final SimpleDateFormat dateFormatWithTime = new SimpleDateFormat("dd.MM.yyyyHH:mm");
	
    
	/* *************************************************************************************
	 * Predefined error messages for curstom ParseException
	 * *************************************************************************************/
    
    /**
     * Predefined error message which can be used if date parsing fails.
     * %s in the error message can be replaced by more detailed information which date parsing failed.
     */
    public static final String FAILED_PARSING_DATE = "failed while parsing %s-date";
    
    /**
     * Predefined error message which can be used if parsing of a start date fails.
     */
	public static final String FAILED_PARSING_START_DATE = String.format(FAILED_PARSING_DATE,"start");
	
	/**
	 * Predefined error message which can be used if parsing of an end date fails
	 */
	public static final String FAILED_PARSING_END_DATE = String.format(FAILED_PARSING_DATE,"end");
	
	/**
	 * Predefined error message which can be used if date with time parsing fails.
	 * %s in the error message can be replaced by more detailed information which date + time parsing failed.
	 */
    public static final String FAILED_PARSING_DATE_WITH_TIME = "failed while parsing %s-date with time";
	
    /**
     * Predefined error message which can be used if parsing of a start date + time fails.
     */
    public static final String FAILED_PARSING_START_DATE_WITH_TIME = String.format(FAILED_PARSING_DATE_WITH_TIME,"start");
	
    /**
     * Predefined error message which can be used if parsing of an end date + time fails.
     */
    public static final String FAILED_PARSING_END_DATE_WITH_TIME = String.format(FAILED_PARSING_DATE_WITH_TIME,"end");
	
    
    /**
     * Predefined error message which can be used if parsing of a numeric value failed.
     * %s in the error message can be replaced by more detailed information which numeric value parsing failed.
     */
	public static final String FAILED_PARSING_NUMERIC_VALUE = "failed while parsing %s numeric value";
	
	
	/* *************************************************************************************
	 * Parser Patterns
	 * ************************************************************************************ */
	
    public static final String PATTERN_LVA_NR_WITH_DOT = "\\d{3}\\.\\w{3}";
    public static final String PATTERN_LVA_NR = "\\d{3}\\w{3}";
    public static final String PATTERN_TERM = "\\d{4}[swSW]";
    public static final String PATTERN_LVA_NR_COMMA_TERM = "\\("
            + PATTERN_LVA_NR + "," + PATTERN_TERM + "\\)";
    public static final String PATTERN_LVA_NR_SLASH_TERM = "\\("
            + PATTERN_LVA_NR + "\\/" + PATTERN_TERM + "\\)";
	
    
    /**
     * Receives a string and tries to parse a simple date (dd.MM.yyyy) from it.
     * If parsing fails, a ParseException with the passed error message is thrown.
     * @param text The string from which to parse the simple date
     * @param errorMsg The error message which should be set if parsing fails
     * @return The date parsed from the string
     * @throws ParseException thrown with specified errorMsg if parsing fails
     */
	public static Date parseDate(String text, String errorMsg) throws ParseException {
        if (!TextUtils.isEmpty(text)) {
            try {
				return dateFormat.parse(text);
			} catch (ParseException e) {
				if(errorMsg.trim().isEmpty()) errorMsg = e.getMessage();
				else errorMsg = errorMsg+": "+e.getMessage();
				throw new ParseException(errorMsg,e.getErrorOffset());
			}
        }
        return null;
    }
	
	
	/**
	 * Receives a string and tries to parse a date with time (dd.MM.yyyyHH:mm) from it.
	 * If parsing fails, a ParseException with the passed error message is thrown.
	 * @param text The string from which to parse the date with time.
	 * @param errorMsg The error message which should be set if parsing fails
	 * @return The date with time parsed from the string
	 * @throws ParseException thrown with specified errorMsg if parsing fails
	 */
	public static Date parseDateWithTime(String text, String errorMsg) throws ParseException {
        if (!TextUtils.isEmpty(text)) {
            try {
				return dateFormatWithTime.parse(text);
			} catch (ParseException e) {
				if(errorMsg.trim().isEmpty()) errorMsg = e.getMessage();
				else errorMsg = errorMsg+": "+e.getMessage();
				throw new ParseException(errorMsg,e.getErrorOffset());
			}
        }
        return null;
    }
}

