package edu.curtin.terminalgriddemo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Event {
    private String date;
    private String time;
    private int duration;
    private String title;

    public Event(String date, String time, int duration, String title) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.title = title;
    }

    public static Event parse(String line) {
        // Use regular expression to match the parts and handle double-quoted titles
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        Matcher matcher = pattern.matcher(line);

        List<String> parts = new ArrayList<>();
        while (matcher.find()) {
            String part = matcher.group(1);
            if (part != null) {
                parts.add(part); // Add double-quoted part
            } else {
                parts.add(matcher.group(2)); // Add non-quoted part
            }
        }

        if (parts.size() >= 5) {
            String date = parts.get(1);
            String time = parts.get(2);
            int duration = Integer.parseInt(parts.get(3));
            String title = parts.get(4);

            return new Event(date, time, duration, title);
        } else {
            throw new IllegalArgumentException("Invalid event format: " + line);
        }
    }


    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }
}
