package com.worktimetracker.WorkCalendar;

import com.worktimetracker.DataClasses.Period;
import com.worktimetracker.DataClasses.WorkSession;
import com.worktimetracker.DataClasses.Date;
import java.util.*;

public abstract class AbstractWorkCalendarFrame {
    
    public AbstractWorkCalendarFrame(List<WorkSession> sessions, Date date){
        this.sessions = sessions;
        this.date = date;
        this.mapSessionToWeek = new HashMap<>();
        //fill map <session - weekNr>
        for (WorkSession workSession : sessions) {
            mapSessionToWeek.put(workSession, calculateWeekNrBasedOnWeekAndMonth(
                    workSession.start().date().getDayOfWeek(), 
                    workSession.start().date().day()));
        }
    }

    protected final List<WorkSession> sessions;
    protected final Map<WorkSession, Integer> mapSessionToWeek;
    protected final Date date;
    public List<WorkSession> getSessions(){return sessions;}

    public static int calculateWeekNrBasedOnWeekAndMonth(int weekDay, int monthDay){
        return (int) Math.ceil((monthDay - weekDay) / 7.0) + 1;
    }

    /**
     * Calculate the total work (as Period) already worked off based on the week
     * @param weekNr
     * @return
     */
    public Period getWorkedOffByWeek(int weekNr){
        List<WorkSession> sessionsOfWeek = mapSessionToWeek.entrySet().stream()
                .filter((entry) -> entry.getValue().equals(weekNr))
                .map((entry) -> entry.getKey())
                .toList();
        return sessionsOfWeek.stream().map( it -> it.getWorkTime()).reduce(new Period(0, 0, 0), (subtotal, element) -> subtotal.add(element));
    }

    public Period getWorkedOffByDay(int dayNr){
        List<WorkSession> sessionsOfDay = sessions.stream()
            .filter((session) -> (session.start().date().day() == dayNr))
            .toList();
        return sessionsOfDay.stream().map( it -> it.getWorkTime()).reduce(new Period(0, 0, 0), (subtotal, element) -> subtotal.add(element));
    }


    public abstract Period getWorkedOffTotal();
    public abstract String getDistributionForWorkedOffOfOptimalAsString(Period totalWorkLoad);
    protected abstract Period[] getDistributionForWorkedOff();
    public abstract String getOptimalDistributionForWorkLoadAddonAsString(Period totalWorkLoad);
    protected abstract Period[] getOptimalDistributionForWorkLoad(Period totalWorkLoad);
    public abstract String getDistributionForRemainingWorkLoadAsString(Period remainingWorkLoad);
    protected abstract Period[] getDistributionForRemainingWorkLoad(Period remainingWorkLoad);
}
