package org.voidsink.anewjkuapp.kusss;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.voidsink.anewjkuapp.GradeListItem;
import org.voidsink.anewjkuapp.ImportGradeTask;
import org.voidsink.anewjkuapp.KusssContentContract;

public class ExamGrade implements GradeListItem {

	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd.MM.yyyy");

	private static final Pattern lvaNrTermPattern = Pattern
			.compile("(\\(.*?\\))");

	private static final Pattern lvaNrPattern = Pattern
			.compile(KusssHandler.PATTERN_LVA_NR);

	private static final Pattern termPattern = Pattern
			.compile(KusssHandler.PATTERN_TERM);

	private int skz;
	private Grade grade;
	private String term;
	private String lvaNr;
	private Date date;
	private final GradeType gradeType;
	private String title;
	private String code;
	private double ects;
	private double sws;

	private ExamGrade(GradeType type, Date date, String lvaNr, String term,
			Grade grade, int skz, String title, String code, double ects,
			double sws) {
		this.gradeType = type;
		this.date = date;
		this.lvaNr = lvaNr;
		this.term = term;
		this.grade = grade;
		this.skz = skz;
		this.title = title;
		this.code = code;
		this.ects = ects;
		this.sws = sws;
	}

	public ExamGrade(GradeType type, Element row) {
		this(type, null, "", "", null, 0, "", "", 0, 0);

		Elements columns = row.getElementsByTag("td");
		if (columns.size() >= 7) {
			try {
				String title = columns.get(1).text();
				Matcher lvaNrTermMatcher = lvaNrTermPattern.matcher(title); // (lvaNr,term)
				if (lvaNrTermMatcher.find()) {
					String lvaNrTerm = lvaNrTermMatcher.group();

					Matcher lvaNrMatcher = lvaNrPattern.matcher(lvaNrTerm); // lvaNr
					if (lvaNrMatcher.find()) {
						setLvaNr(lvaNrMatcher.group());
					}

					Matcher termMatcher = termPattern.matcher(lvaNrTerm); // term
					if (termMatcher.find()) {
						setTerm(termMatcher.group());
					}

					String tmp = title.substring(0, lvaNrTermMatcher.start());
					if (lvaNrTermMatcher.end() <= title.length()) {
						String addition = title
								.substring(lvaNrTermMatcher.end(),
										title.length())
								.replaceAll("(\\(.*?\\))", "").trim();
						if (addition.length() > 0) {
							tmp = tmp + " (" + addition + ")";
						}
					}
					title = tmp;
				}

				title = title + " " + columns.get(4).text(); // title + lvaType

				setTitle(title); // title

				setDate(dateFormat.parse(columns.get(0).text())); // date

				setGrade(Grade.parseGrade(columns.get(2).text())); // grade

				String[] ectsSws = columns.get(5).text().replace(",", ".")
						.split("/");
				if (ectsSws.length == 2) {
					setEcts(Double.parseDouble(ectsSws[0]));
					setSws(Double.parseDouble(ectsSws[1]));
				}

				setSKZ(Integer.parseInt(columns.get(6).text())); // grade

				setCode(columns.get(3).text());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private void setSws(double sws) {
		this.sws = sws;
	}

	private void setEcts(double ects) {
		this.ects = ects;
	}

	public ExamGrade(Cursor c) {
		this.lvaNr = c.getString(ImportGradeTask.COLUMN_GRADE_LVANR);
		this.term = c.getString(ImportGradeTask.COLUMN_GRADE_TERM);
		this.date = new Date(c.getLong(ImportGradeTask.COLUMN_GRADE_DATE));
		this.gradeType = GradeType.parseGradeType(c
				.getInt(ImportGradeTask.COLUMN_GRADE_TYPE));
		this.grade = Grade.parseGradeType(c
				.getInt(ImportGradeTask.COLUMN_GRADE_GRADE));
		this.skz = c.getInt(ImportGradeTask.COLUMN_GRADE_SKZ);
		this.title = c.getString(ImportGradeTask.COLUMN_GRADE_TITLE);
		this.code = c.getString(ImportGradeTask.COLUMN_GRADE_CODE);
		this.ects = c.getDouble(ImportGradeTask.COLUMN_GRADE_ECTS);
		this.sws = c.getDouble(ImportGradeTask.COLUMN_GRADE_SWS);
	}

	private void setCode(String code) {
		this.code = code;
	}

	private void setTitle(String title) {
		this.title = title.trim();
	}

	private void setSKZ(int skz) {
		this.skz = skz;
	}

	private void setGrade(Grade grade) {
		this.grade = grade;
	}

	private void setTerm(String term) {
		this.term = term;
	}

	private void setLvaNr(String lvaNr) {
		this.lvaNr = lvaNr;
	}

	private void setDate(Date date) {
		this.date = date;
	}

	public String getCode() {
		return this.code;
	}

	public Date getDate() {
		return this.date;
	}

	public String getLvaNr() {
		return this.lvaNr;
	}

	public String getTerm() {
		return this.term;
	}

	public Grade getGrade() {
		return this.grade;
	}

	public int getSkz() {
		return this.skz;
	}

	public GradeType getGradeType() {
		return this.gradeType;
	}

	public boolean isInitialized() {
		return this.gradeType != null && this.date != null
				&& this.grade != null;
	}

	public String getTitle() {
		return this.title;
	}

	public double getEcts() {
		return this.ects;
	}

	public double getSws() {
		return this.sws;
	}

	public ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(KusssContentContract.Grade.GRADE_COL_DATE, getDate().getTime());
		cv.put(KusssContentContract.Grade.GRADE_COL_GRADE, getGrade().ordinal());
		cv.put(KusssContentContract.Grade.GRADE_COL_LVANR, getLvaNr());
		cv.put(KusssContentContract.Grade.GRADE_COL_SKZ, getSkz());
		cv.put(KusssContentContract.Grade.GRADE_COL_TERM, getTerm());
		cv.put(KusssContentContract.Grade.GRADE_COL_TYPE, getGradeType()
				.ordinal());
		cv.put(KusssContentContract.Grade.GRADE_COL_CODE, getCode());
		cv.put(KusssContentContract.Grade.GRADE_COL_TITLE, getTitle());
		cv.put(KusssContentContract.Grade.GRADE_COL_ECTS, getEcts());
		cv.put(KusssContentContract.Grade.GRADE_COL_SWS, getSws());
		return cv;
	}

	@Override
	public boolean isGrade() {
		return true;
	}

	@Override
	public int getType() {
		return GradeListItem.TYPE_GRADE;
	}
}
