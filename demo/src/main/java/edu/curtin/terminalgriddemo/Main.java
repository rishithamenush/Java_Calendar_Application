package edu.curtin.terminalgriddemo;

import edu.curtin.terminalgrid.TerminalGrid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar calendar = new GregorianCalendar();
        Date currentDate = new Date();
        calendar.setTime(currentDate);
        System.out.println("Enter a valid IETF language tag (e.g. en, af, it): ");
        String languageTag = scanner.nextLine();
        Locale selectedLocale = Locale.forLanguageTag(languageTag);
        Locale.setDefault(selectedLocale);
        List<Event> events = new ArrayList<>();
        List<Plugin> plugins = new ArrayList<>();
        List<String> scripts = new ArrayList<>();

        try {
            File file = new File(Main.class.getClassLoader().getResource("calendar.utf8.cal").getFile());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder scriptContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("event")) {
                    Event event = Event.parse(line);
                    events.add(event);
                } else if (line.startsWith("plugin")) {
                    Plugin plugin = Plugin.parse(line);
                    plugins.add(plugin);
                } else if (line.startsWith("script")) {
                    scriptContent = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        if (line.equals("script\"")) {
                            break;
                        }
                        scriptContent.append(line).append("\n");
                    }
                    String scriptText = scriptContent.toString();
                    if (scriptText.startsWith("script\"")) {
                        scriptText = scriptText.substring(7);
                    }
                    if (scriptText.endsWith("\"")) {
                        scriptText = scriptText.substring(0, scriptText.length() - 1);
                    }
                    scripts.add(scriptText);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        ResourceBundle bundle;
        if (languageTag.equalsIgnoreCase("en")) {
            bundle = ResourceBundle.getBundle("language_en", Locale.getDefault());
        } else if (languageTag.equalsIgnoreCase("af")) {
            bundle = ResourceBundle.getBundle("language_af", Locale.getDefault());
        } else if (languageTag.equalsIgnoreCase("it")) {
            bundle = ResourceBundle.getBundle("language_it", Locale.getDefault());
        } else {
            bundle = ResourceBundle.getBundle("language_en", Locale.getDefault());
        }

        String[][] messages = new String[2][7];
        String[] rowHeadings = {bundle.getString("Date"), bundle.getString("Event")};
        String[] colHeadings = new String[7];
        int columnWidths = 200;
        TerminalGrid terminalGrid = TerminalGrid.create();
        terminalGrid.setTerminalWidth(columnWidths);
        terminalGrid.setCharset(java.nio.charset.Charset.forName("UTF-8"));

        boolean running = true;
        while (running) {
            Date currentWeekStart = calendar.getTime();
            for (int i = 0; i < 7; i++) {
                messages[0][i] = dateFormat.format(calendar.getTime());
                messages[1][i] = getEvent(messages[0][i], events);
                colHeadings[i] = getDayName(messages[0][i]);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            terminalGrid.print(messages, rowHeadings, colHeadings);
            System.out.println();
            System.out.println(bundle.getString("Option") + ": ");
            System.out.println("+d - " + bundle.getString("dayPlus1"));
            System.out.println("+w - " + bundle.getString("weekPlus1"));
            System.out.println("+m - " + bundle.getString("monthPlus1"));
            System.out.println("+y - " + bundle.getString("yearPlus1"));
            System.out.println("-d - " + bundle.getString("dayMin1"));
            System.out.println("-w - " + bundle.getString("weekMin1"));
            System.out.println("-m - " + bundle.getString("monthMin1"));
            System.out.println("-y - " + bundle.getString("yearMin1"));
            System.out.println("t - " + bundle.getString("return"));
            System.out.println("s - Search events by date");
            System.out.println("quit - " + bundle.getString("Quit"));

            String input = scanner.nextLine();
            switch (input) {
                case "+d":
                    calendar.setTime(currentWeekStart);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    break;
                case "+w":
                    calendar.setTime(currentWeekStart);
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case "+m":
                    int currentYear = calendar.get(Calendar.YEAR);
                    int currentMonth = calendar.get(Calendar.MONTH);
                    calendar.setTime(currentWeekStart);
                    calendar.set(Calendar.YEAR, currentYear);
                    calendar.set(Calendar.MONTH, currentMonth + 1);
                    break;
                case "+y":
                    int currentYearPlus1 = calendar.get(Calendar.YEAR) + 1;
                    calendar.setTime(currentWeekStart);
                    calendar.set(Calendar.YEAR, currentYearPlus1);
                    break;
                case "-d":
                    calendar.setTime(currentWeekStart);
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    break;
                case "-w":
                    calendar.setTime(currentWeekStart);
                    calendar.add(Calendar.WEEK_OF_YEAR, -1);
                    break;
                case "-m":
                    int currentYear2 = calendar.get(Calendar.YEAR);
                    int currentMonth2 = calendar.get(Calendar.MONTH);
                    calendar.setTime(currentWeekStart);
                    calendar.set(Calendar.YEAR, currentYear2);
                    calendar.set(Calendar.MONTH, currentMonth2 - 1);
                    break;
                case "-y":
                    int currentYearMinus1 = calendar.get(Calendar.YEAR) - 1;
                    calendar.setTime(currentWeekStart);
                    calendar.set(Calendar.YEAR, currentYearMinus1);
                    break;
                case "t":
                    calendar.setTime(currentDate);
                    break;
                case "s":
                    System.out.println("Enter date (format: dd/MM/yyyy):");
                    String searchDate = scanner.nextLine();
                    String eventsOnDate = getEvent(searchDate, events);
                    System.out.println(eventsOnDate);
                    break;
                case "quit":
                    running = false;
                    break;
                default:
                    System.out.println(bundle.getString("Invalid"));
                    break;
            }
        }
        scanner.close();
    }

    private static String getDayName(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdf.parse(dateString);
            SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());
            return dayFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Date";
        }
    }

    private static String getEvent(String dateString, List<Event> events) {
        try {
            for (Event event : events) {
                String[] parts = event.getDate().split("-");
                String formattedDate = parts[2] + "/" + parts[1] + "/" + parts[0];
                if (dateString.equals(formattedDate)) {
                    String eventString = event.getTitle() + "\n" + event.getTime() + "\n" + "Duration : " + event.getDuration();
                    return eventString;
                }
            }
            return "No event";
        } catch (Exception e) {
            return "Error";
        }
    }
}
