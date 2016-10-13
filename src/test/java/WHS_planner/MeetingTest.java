package WHS_planner;

import WHS_planner.Core.Meeting;
import WHS_planner.Core.Student;
import WHS_planner.Util.Course;
import junit.framework.TestCase;

/**
 * Created by spam on 08.10.2016.
 */
public class MeetingTest extends TestCase {
    public void testMeetingCreation(){
        Course english = new Course("English", 4, "Teacher", Course.level.COLLEGE);
        Meeting m = new Meeting(new Student("John", "Smith", "test@test.com", 12), new Student("Smith", "John", "test@test2.com", 12), 10, 12, english);
        m.create();
    }
}