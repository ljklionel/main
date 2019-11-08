package Parser;

import Commands.Command;
import Commands.ShowWorkloadCommand;
import Commons.DukeLogger;
import DukeExceptions.DukeInvalidFormatException;
import Commons.LookupTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * This class parses the full command that calls for WorkloadParse.
 */
public class WorkloadParse extends Parse{
    private static String fullCommand;
    private static final int HOURS = 24;
    private static final int MINUTES = 60;
    private static final int SECONDS = 60;
    private static final int MILLISECONDS = 1000;
    private static final int ONE_WEEK = 7;
    private final Logger LOGGER = DukeLogger.getLogger(WorkloadParse.class);

    /**
     * Creates a WorkloadParse object.
     * @param fullCommand
     */
    public WorkloadParse(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * This method gets the date of 7 days later.
     * @param inDate date
     * @return date of 7 days later
     */
    private Date getNextWeekDate (Date inDate) {
        Date nextWeek = new Date(inDate.getTime() + ONE_WEEK * HOURS * MINUTES * SECONDS * MILLISECONDS);
        return nextWeek;
    }

    @Override
    public Command parse() throws DukeInvalidFormatException {
        try {
            Date today = Calendar.getInstance().getTime();
            Date nextWeek = getNextWeekDate(today);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String nextWeekDate = formatter.format(nextWeek);
            return new ShowWorkloadCommand(nextWeekDate);
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.severe("Invalid show workload format");
            throw new DukeInvalidFormatException("OOPS!!! Please enter show workload as follows:\n" +
                    "show/workload");
        }
    }
}
