package org.voidsink.kussslib;

import java.util.Date;

import org.voidsink.kussslib.impl.KusssFactoryImpl;

/**
 * KusssFactory provides methods for creating objects which represent important KUSSS data like
 * - courses
 * - curricula
 * - assessments
 * - exams
 * 
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
public final class KusssFactory {
	private KusssFactory() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates a course object which contains information to describe a course in KUSSS.
	 * These course data are accessible via the created course object.
	 * @param term Term when the course takes place
	 * @param courseId An individual ID of the course
	 * @param title Title of the course
	 * @param cid Curriculum id for the course
	 * @param lecturer Lecturer of the course
	 * @param ects ECTs for the course
	 * @param sws SWS for the course
	 * @param courseType Type of the course
	 * @param classCode An individual class code for the course
	 * @return A new course object
	 */
	public static Course getCourse(Term term, String courseId, String title, int cid,
			String lecturer, double ects, double sws, CourseType courseType,
			String classCode) {
		return KusssFactoryImpl.getCourse(term, courseId, title,
				cid, lecturer, ects, sws, courseType, classCode);
	}

	
	/**
	 * Creates a curriculum object which contains information to describe a curriculum in KUSSS.
	 * These curriculum data are accessible via the created curriculum object.
	 * @param cid Individual id of the curriculum
	 * @param title Title of the curriculum
	 * @param uni University where curriculum is offered
	 * @param dtStart Date when logged-in student started studying in the curriculum
	 * @param dtEnd Date when the logged-in student ended studying in the curriculum
	 * @param isStandard Indicator whether the curriculum is the standard selection for the logged-in student
	 * @param steopDone Indicator whether the StEOP in this curriculum has been finished by the logged-in student
	 * @param active Indicator whether this curriculum is still active for the logged-in student
	 * @return A new curriculum object
	 */
	public static Curricula getCurricula(int cid, String title, String uni,
			Date dtStart, Date dtEnd, boolean isStandard, boolean steopDone,
			boolean active) {
		return KusssFactoryImpl.getCurricula(cid, title, uni,
				dtStart, dtEnd, isStandard, steopDone, active);
	}

	
	/**
	 * Creates an assessment object which contains information to describe an assessment in KUSSS.
	 * These assessment data are accessible via the created assessment object.
	 * An assessment always belongs to a course in KUSSS, hence, it also contains course specific data.
	 * @param date Date of the assessment
	 * @param title Title of the course to which the assessment belongs
	 * @param term Term of the course to which the assessment belongs
	 * @param courseId Individual id of the course to which the assessment belongs
	 * @param grade Grade (numeric or textual assessment value)
	 * @param cid ID of the curriculum of the assessment
	 * @param assessmentType Type of the assessment
	 * @param classCode Individual class code of the course to which the assessment belongs
	 * @param ects ECTs of the course to which the assessment belongs
	 * @param sws SWS of the course to which the assessment belongs
	 * @param courseType Type of the course to which the assessment belongs
	 * @return A new assessment object
	 */
	public static Assessment getAssessment(Date date, String title, Term term,
			String courseId, Grade grade, int cid,
			AssessmentType assessmentType, String classCode, double ects,
			double sws, CourseType courseType) {
		return KusssFactoryImpl.getAssessment(date, title, term, courseId,
				grade, cid, assessmentType, classCode, ects, sws, courseType);
	}

	
	/**
	 * Creates an exam object which contains information to describe an exam in KUSSS.
	 * These exam data are accessible via the created exam object.
	 * An exam always belongs to a course in KUSSS, hence, it also contains course specific data.
	 * @param courseId ID of the course to which the exam belongs
	 * @param term Term of the course to which the exam belongs
	 * @param dtStart Date + time when the exam starts
	 * @param dtEnd Date + time when the exam ends
	 * @param location Location where the exam takes place
	 * @param title Title of the course to which the exam belongs
	 * @param cid Curriculum id for the exam
	 * @param description Additional description of the exam
	 * @param info Additional info for the exam (optional)
	 * @param isRegistered Indicator whether logged-in student is registered for the exam
	 * @param maxParticipants Number of maximum participants
	 * @param participants Currently registered students for the exam
	 * @param registrationDtStart Date + time when registration for the exam starts
	 * @param registrationDtEnd Date + time when registration for the exam ends
	 * @param unregistrationDt Date + time when unregistration for the exam ends
	 * @return A new exam object
	 */
	public static Exam getExam(String courseId, Term term, Date dtStart, Date dtEnd,
			String location, String title, int cid, String description,
			String info, boolean isRegistered, int maxParticipants,
			int participants, Date registrationDtStart, Date registrationDtEnd,
			Date unregistrationDt) {
		return KusssFactoryImpl.getExam(courseId, term, dtStart, dtEnd,
				location, title, cid, description, info, isRegistered, maxParticipants,
				participants, registrationDtStart, registrationDtEnd, unregistrationDt);
	}

}
