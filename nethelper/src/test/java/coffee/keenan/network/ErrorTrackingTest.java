package coffee.keenan.network;

import coffee.keenan.network.helpers.ErrorTracking;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorTrackingTest
{

    @Test
    public void addException()
    {
        ErrorTracking e = new ErrorTracking()
        {
        };
        e.addException("test1", new Exception("1test exception"));
        e.addException("test2", new Exception("2test exception"));
        e.addException("test2", new Exception("2test exception2"));
        int count1 = 0;
        int count2 = 0;
        for (String line : e.getExceptions())
        {
            if (line.contains("[test1]")) count1++;
            if (line.contains("[test2]")) count2++;
        }
        assertEquals(1, count1);
        assertEquals(2, count2);
    }

    @Test
    public void getExceptions()
    {
        ErrorTracking e = new ErrorTracking()
        {
        };
        assertEquals(0, e.getExceptions().length);
    }
}