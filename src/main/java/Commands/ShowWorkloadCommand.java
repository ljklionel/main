package Commands;

import Commons.LookupTable;
import Commons.Storage;
import Commons.Ui;
import Tasks.Assignment;
import Tasks.TaskList;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


public class ShowWorkloadCommand extends Command {
    private String week;


    /**
     * Show recommended weekly workload
     * @param week week to see workload
     */
    public ShowWorkloadCommand(String week) {
        this.week = week;
    }

    @Override
    public String execute (LookupTable LT,TaskList events, TaskList deadlines, Ui ui, Storage storage) throws ParseException, FileNotFoundException {
        String workloadWeek = LT.getValue(week);
        HashMap<String, HashMap<String, ArrayList<Assignment>>> eventMap = events.getMap();
        HashMap<String, HashMap<String, ArrayList<Assignment>>> deadlineMap = deadlines.getMap();
        HashMap<String, ArrayList<Assignment>> workloadMap = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String nextWeekMon = LT.getValue(workloadWeek + " mon");
        String nextWeekTue = LT.getValue(workloadWeek + " tue");
        String nextWeekWed = LT.getValue(workloadWeek + " wed");

        Date followingDate = new Date((formatter.parse(week).getTime()) + 7 * 24 * 60 * 60 * 1000);
        String followingWeekDate = formatter.format(followingDate);
        String followingWeek = LT.getValue(followingWeekDate);
        String followingWeekMon = LT.getValue(followingWeek + " mon");
        String followingWeekTue = LT.getValue(followingWeek + " tue");
        String followingWeekWed = LT.getValue(followingWeek + " wed");


        for (Map.Entry<String, HashMap<String, ArrayList<Assignment>>> eventModule: eventMap.entrySet()) {
            HashMap<String, ArrayList<Assignment>> eventModuleValue = eventModule.getValue();
            for (Map.Entry<String, ArrayList<Assignment>> eventItem: eventModuleValue.entrySet()) {
                String[] strDayDate = eventItem.getKey().trim().split(" ");
                String strDate = strDayDate[1].trim();
                String selectedWeek = LT.getValue(strDate);
                if (selectedWeek.equals(workloadWeek)) {
                    workloadMap.put(strDate, eventItem.getValue());
                    }
                }
            }
        for (Map.Entry<String, HashMap<String, ArrayList<Assignment>>> deadlineModule: deadlineMap.entrySet()) {
            HashMap<String, ArrayList<Assignment>> deadlineModuleValue = deadlineModule.getValue();
            for (Map.Entry<String, ArrayList<Assignment>> deadlineItem: deadlineModuleValue.entrySet()) {
                String[] strDayDate = deadlineItem.getKey().trim().split(" ");
                String strDate = strDayDate[1].trim();
                String selectedWeek = LT.getValue(strDate);
                if (selectedWeek.equals(workloadWeek) || strDate.equals(followingWeekMon) ||
                        strDate.equals(followingWeekTue) || strDate.equals(followingWeekWed)) {
                    if (!strDate.equals(nextWeekMon) || !strDate.equals(nextWeekTue) || !strDate.equals(nextWeekWed)) {
                        Date tempDate = formatter.parse(strDate);
                        tempDate = new Date(tempDate.getTime() - 3 * 24 * 60 * 60 * 1000); //put all deadline 3 days before
                        String newDate = formatter.format(tempDate).trim();
                        if (workloadMap.containsKey(newDate)) {
                            for (int i = 0; i < deadlineItem.getValue().size(); i++) {
                                workloadMap.get(newDate).add(deadlineItem.getValue().get(i));
                            }
                        } else {
                            workloadMap.put(newDate, deadlineItem.getValue());
                        }
                    }
                }
            }
        }
        TreeMap<String, ArrayList<Assignment>> sortedWorkLoadMap = new TreeMap<>();
        sortedWorkLoadMap.putAll(workloadMap);
        return ui.showWorkload(sortedWorkLoadMap);
    }
}
