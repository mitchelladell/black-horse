package com.maplewood.dto;
import com.maplewood.model.TimeSlot;

public record TimeSlotDto(
        Long id,
        String dayOfWeek,
        String startTime,
        String endTime
) {
    private static final String[] DAYS = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    public static TimeSlotDto from(TimeSlot ts) {
        String day = (ts.getDayOfWeek() >= 1 && ts.getDayOfWeek() <= 7)
                ? DAYS[ts.getDayOfWeek() - 1]
                : "Unknown";
        return new TimeSlotDto(ts.getId(), day, ts.getStartTime(), ts.getEndTime());
    }
}