package org.voidsink.kussslib;
import java.util.Date;

/**
 * Interface provides methods for retrieving detail information about a curriculum
 * for which a student is enrolled.
 * 
 * Curriculum information consists of the following elements:
 * - indicator whether the curriculum is the standard selection (out of several curricula)
 * - curriculum id
 * - title of the curriculum
 * - indicator whether the StEOP for this curriculum is done
 * - indicator whether the curriculum is active for the student
 * - university where the curriculum belongs to
 * - date when student started studying in the specific curriculum
 * - date when the student stopped studying in the specific curriculum
 * 
 * @author Paul Pretsch, Lukas Habring, Michaela Schoenbauer
 * @version 1.0
 *
 */
public interface Curricula {

    public boolean isStandard();
    public int getCid();
    public String getTitle();
    public boolean isSteopDone();
    public boolean isActive();
    public String getUniversity();
    public Date getDtStart();
    public Date getDtEnd( );
}
