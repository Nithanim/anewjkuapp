package org.voidsink.kussslib;
import java.util.Date;

/**
 * Interface provides methods for retrieving detail information about assessments listed in KUSSS.
 * 
 * An assessment consists of the following elements:
 * - date of the assessments
 * - title of the assessed course
 * - information about term and the course id
 * - grade (includes information about the assessment type)
 * - class code (individual code for the course)
 * - type of the assessed course
 * - ECTs and SWS of the assessed course
 * - curriculum id for the assessed course
 * 
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
public interface Assessment {
	
	public Date getDate();
	public String getTitle();
	public Term getTerm();
    public String getCourseId();
    public Grade getGrade();

    public int getCid();
    public AssessmentType assessmentType();
    public String getClassCode();
    public double getEcts();
    public double getSws();
    public CourseType getCourseType();
}

