package org.voidsink.kussslib;

import java.util.Date;

/**
 * Interface provides methods for retrieving detail information about exams listed in KUSSS.
 * 
 * An exam consists of the following elements:
 * - course id of the course to which the exam belongs
 * - term of the course to which the exam belongs
 * - date + time when the exam starts
 * - date + time when the exam ends
 * - location where the exam will take place
 * - title of the course to which the exam belongs
 * - curriculum id for the exam
 * - description of the exam (contains information about: maximum participants,
 * 		registered students, registration time frame and unregistration date + time)
 * - additional info for the exam (optional)
 * - indicator whether student is registered for the exam
 * - number of maximum participants
 * - number of currently registered students
 * - date + time when the registration starts
 * - date + time when the registration ends
 * - date + time when unregistration ends
 * 
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
public interface Exam {
	
    public String getCourseId();
    public Term getTerm();
    public Date getDtStart();
    public Date getDtEnd();
    public String getLocation();
    public String getTitle();
    public int getCid();
    public String getDescription();
    public String getInfo();
    public boolean isRegistered(); 
    
    /**
     * @return max number of participants, < 0 if no limit 
     */
    public int getMaxParticipants();
    
    public int getParticipants();
    public Date getRegistrationDtStart();
    public Date getRegistrationDtEnd();
    public Date getUnRegistrationDt();
    
}
