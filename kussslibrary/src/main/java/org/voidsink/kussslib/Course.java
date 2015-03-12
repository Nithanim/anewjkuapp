package org.voidsink.kussslib;

/**
 * Interface provides methods for retrieving detail information about courses in KUSSS.
 * 
 * Course information consists of the following elements:
 * - term in which the course will take place
 * - curriculum id for the course
 * - class code (individual code for the course)
 * - course type
 * - title of the course
 * - course id
 * - lecturer of the course
 * - ECTs and SWS of the course
 * 
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
public interface Course {
	
	public Term getTerm();
	public int getCid();
	public String getClassCode();
	public CourseType getCourseType();
	public String getTitle();
	public String getCourseId(); 
	public String getLecturer();
	public double getEcts();
	public double getSws();
}