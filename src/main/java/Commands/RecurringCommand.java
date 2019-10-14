package Commands;

import Interface.Storage;
import Interface.Ui;
import Tasks.Event;
import Tasks.TaskList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecurringCommand extends Command {
    private static final String NO_FIELD = "void";
    private final String description;
    private final String startTimeString;
    private final String endTimeString;
    private final Date endDate;
    private Date startDate;
    private Date startOfFollowingWeek;

    /**
     * Add task that is recurring over a period of time
     * @param description Description of a task
     * @param startDate Start date of a task
     * @param endDate End date of a task
     * @param startTimeString Start time of a task
     * @param endTimeString End time of a task
     */
    public RecurringCommand(String description, Date startDate, Date endDate, String startTimeString, String endTimeString) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTimeString = startTimeString;
        this.endTimeString = endTimeString;
    }

    @Override
    public String execute(TaskList events, TaskList deadlines, Ui ui, Storage storage) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String startDateString = dateFormat.format(startDate);

        do {
            new AddCommand(new Event(description, startDateString, startTimeString, endTimeString));
            startOfFollowingWeek = new Date(startDate.getTime() + 7 * 24 * 60 * 60 * 1000);
            startDateString = dateFormat.format(startOfFollowingWeek);
            startDate = startOfFollowingWeek;
        }
        while (startOfFollowingWeek.before(endDate) || startOfFollowingWeek.equals(endDate));
        return NO_FIELD;
    }
}
