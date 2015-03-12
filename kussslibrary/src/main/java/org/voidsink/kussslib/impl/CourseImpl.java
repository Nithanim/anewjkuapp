package org.voidsink.kussslib.impl;

import java.text.ParseException;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.voidsink.kussslib.Course;
import org.voidsink.kussslib.CourseType;
import org.voidsink.kussslib.Term;

class CourseImpl implements Course {

	private static final Pattern courseIdPattern = Pattern
			.compile(Parser.PATTERN_LVA_NR_WITH_DOT);

	private Term term;
	private String courseId;
	private String title;
	private int cid;
	private String lecturer;
	private double ects, sws;
	private CourseType courseType;
	private String classCode;

	CourseImpl(Term term, String courseId) {
		this.term = term;
		this.courseId = courseId;
	}

	CourseImpl(Term term, Element row) throws ParseException {
		this(term,"");

        Elements columns = row.getElementsByTag("td");

		if (columns.size() >= 11) {
			boolean active = columns.get(9)
					.getElementsByClass("assignment-active").size() == 1;
			String courseIdText = columns.get(6).text();
			if (active && courseIdPattern.matcher(courseIdText).matches()) {
				this.courseId = courseIdText.toUpperCase().replace(".", "");
				title = columns.get(5).text();
				courseType = CourseType.parseCourseType(columns.get(4).text()); // type (UE, ...)
				lecturer = columns.get(7).text(); // lecturer
				
				try {
					cid = Integer.parseInt(columns.get(2).text()); // curr. id
				}
				catch (NumberFormatException e) {
					throw new ParseException(String.format(
							Parser.FAILED_PARSING_NUMERIC_VALUE, "curriculum id"),
							2);
				}
				
				ects = Double.parseDouble(columns.get(8).text()
						.replace(",", ".")); // ECTS
				sws = (this.ects * 2/3D); //Sws //FIXME
				classCode = columns.get(3).text();
			}
		}
	}
	
	
	CourseImpl(Term term, String courseId, String title, int cid, String lecturer, double ects, double sws, CourseType courseType, String classCode) { 
		this(term, courseId);
		
		this.title = title;
		this.cid = cid;
		this.lecturer = lecturer;
		this.ects = ects;
		this.sws = sws;
		this.courseType = courseType;
		this.classCode = classCode;
	}

	public int getCid() {
		return this.cid;
	}

	
	public String getLecturer() {
		return this.lecturer;
	}


	public double getEcts() {
		return this.ects;
	}


	public Term getTerm() {
		return this.term;
	}


	public String getClassCode() {
		return this.classCode;
	}


	public CourseType getCourseType() {
		return this.courseType;
	}


	public String getTitle() {
		return this.title;
	}


	public String getCourseId() {
		return this.courseId;
	}


	public double getSws() {
		return this.sws;
	}
	
	
	public boolean isInitialized() {
		return !term.isEmpty() && !TextUtils.isEmpty(courseId);
	}
}
	
