package org.voidsink.kussslib;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.voidsink.kussslib.impl.TextUtils;

/**
 * Class describes a term represented in KUSSS.
 * A term has to consist of the following elements:
 * - a term type (winter or summer)
 * - a term year
 * 
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
public final class Term implements Comparable<Term> {

	private static final Pattern termPattern = Pattern.compile("\\d{4}[WwSs]");

	/**
	 * Enumeration contains all possible term types in KUSSS, namely winter and summer.
	 * 
	 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
	 * @version 1.0
	 *
	 */
	public enum TermType {
		SUMMER("S"), WINTER("W");

		private final String value;

		private TermType(String value) {
			this.value = value;
		}

		/**
		 * Returns the string representation of the term type.
		 */
		@Override
		public String toString() {
			return value;
		}

		/**
		 * Receives a string and parses the correct term type from it.
		 * @param text The string which describes the term type
		 * @return The term type parsed from the string
		 * @throws ParseException thrown if no valid term type can be parsed from the string
		 */
		public static TermType parseTermType(String text) throws ParseException {
			text = text.trim().toLowerCase();

			switch (text) {
			case "w":
				return WINTER;
			case "s":
				return SUMMER;
			default:
				throw new ParseException("value is no valid char", 0);
			}
		}
	};

	private final int year;
	private final TermType type;
	private boolean loaded = false;

	/**
	 * Creates new term object with year and term type.
	 * @param year term year
	 * @param type term type
	 */
	public Term(int year, TermType type) {
		this.year = year;
		this.type = type;
	}


	@Override
	public int compareTo(Term o) {
		if (o == null) {
			return -1;
		}

		if (this.year < o.getYear()) {
			return -1;
		} else if (this.year > o.getYear()) {
			return 1;
		}

		return this.type.compareTo(o.getType());
	}

	public int getYear() {
		return year;
	}

	public TermType getType() {
		return type;
	}

	
	/**
	 * Returns the string representation of the term object.
	 */
	@Override
	public String toString() {
		return String.format("%d%s", year, type.toString());
	}


	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean mLoaded) {
		this.loaded = mLoaded;
	}

	/**
	 * Receives a string and parses the correct term object from it.
	 * @param termStr The string which describes the term (type and year)
	 * @return The term object parsed from the string
	 * @throws ParseException thrown if no valid term object can be parsed from the string
	 */
	public static Term parseTerm(String termStr) throws ParseException {
		if (TextUtils.isEmpty(termStr) || termStr.length() > 5) {
			throw new ParseException("String is no valid term", 0);
		}

		Matcher matcher = termPattern.matcher(termStr);
		if (!matcher.find()) {
			throw new ParseException("String is no valid term", 0);
		}

		int year = Integer.parseInt(matcher.group().substring(0, 4));
		TermType type = TermType.parseTermType(matcher.group().substring(4));

		return new Term(year, type);
	}

	/**
	 * Checks if a term object is empty. This is the case if
	 * - either the year is less than 0
	 * - or the term type is null
	 * @return true if the term object is empty, else false
	 */
	public boolean isEmpty() {
		return this.year < 0 || this.type == null;
	}

}
