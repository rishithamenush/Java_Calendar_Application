package edu.curtin.terminalgriddemo;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RepeatPlugin {
    private String title;
    private String startDate;
    private String startTime;
    private int duration;
    private int repeat;

    public RepeatPlugin(String title, String startDate, String startTime, int duration, int repeat) {
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.repeat = repeat;
    }

    public List<Event> generateRecurringEvents() {
        List<Event> recurringEvents = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date eventDate = sdf.parse(startDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(eventDate);
            Date oneYearLater = cal.getTime();
            cal.add(Calendar.YEAR, 1);
            oneYearLater = cal.getTime();
            while (eventDate.before(oneYearLater)) {
                String formattedDate = sdf.format(eventDate);
                recurringEvents.add(new Event(title, formattedDate, duration, startTime));
                cal.setTime(eventDate);
                cal.add(Calendar.DATE, repeat);
                eventDate = cal.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recurringEvents;
    }
}
