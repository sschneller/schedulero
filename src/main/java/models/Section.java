package models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static models.Util.nullToEmpty;

/**
 * Defines a Section for a Course.
 */
public class Section {
    private String sectionNumber, crn, teacher;
    private final List<MeetingTime> meetingTimes;

    /**
     * Constructor.
     *
     * @param sectionNumber the number of the section. e.g. 800
     * @param crn the CRN number of the section. e.g. 10279
     * @param teacher the name of the sections teacher. e.g. Mr. Filip
     */
    public Section(final String sectionNumber, final String crn, final String teacher) {
        setCrn(crn);
        setTeacher(teacher);
        setSectionNumber(sectionNumber);
        meetingTimes = new ArrayList<>();
    }

    /**
     * Adds the meeting time to the section.
     *
     * @param meetingTime the meeting time to add.
     * @throws IllegalArgumentException when there is a conflict with two or more meeting times in this section.
     */
    public void addMeetingTime(final MeetingTime meetingTime) throws IllegalArgumentException {
        Optional<MeetingTime> overlapping = meetingTimes.stream().filter(s -> s.overlaps(meetingTime)).findFirst();
        if(overlapping.isPresent()) {
            throw new IllegalArgumentException("Meeting time '" + meetingTime
                    + "' conflicts with the existing meeting time '" + overlapping.get() + "'");
        }

        meetingTimes.add(meetingTime);
    }

    /**
     * Removes the meeting time if it already exists.
     *
     * @param meetingTime the meeting time to remove.
     */
    public void removeMeetingTime(final MeetingTime meetingTime) {
        meetingTimes.remove(meetingTime);
    }

    /**
     * Returns the teacher name.
     *
     * @return the teacher name.
     */
    @Nonnull
    public String getTeacher() {
        return teacher;
    }

    /**
     * Sets the teacher name.
     *
     * @param teacher the teacher name.
     */
    public void setTeacher(final String teacher) {
        this.teacher = nullToEmpty(teacher);
    }


    /**
     * Returns an optional CRN.
     *
     * @return an optional CRN.
     */
    @Nonnull
    public String getCrn() { return crn; }

    /**
     * Sets the CRN.
     *
     * @param crn the CRN.
     */
    public void setCrn(final String crn) {
        this.crn = nullToEmpty(crn);
    }

    /**
     * Returns an optional section number.
     *
     * @return an optional section number.
     */
    @Nonnull
    public String getSectionNumber() {
        return sectionNumber;
    }

    /**
     * Sets the section number.
     *
     * @param sectionNumber the section number.
     */
    public void setSectionNumber(final String sectionNumber) {
        this.sectionNumber = nullToEmpty(sectionNumber);
    }

    /**
     * Returns an unmodifiable list of section times.
     *
     * @return an unmodifiable list of section times.
     */
    @Nonnull
    public List<MeetingTime> getMeetingTimes() {
        return Collections.unmodifiableList(meetingTimes);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(21, 49)
                .append(crn)
                .append(teacher)
                .append(meetingTimes)
                .append(sectionNumber)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        } else if(!(obj instanceof Section)) {
            return false;
        }

        Section section = (Section) obj;

        return new EqualsBuilder()
                .append(crn, section.getCrn())
                .append(teacher, section.getTeacher())
                .append(meetingTimes, section.getMeetingTimes())
                .append(sectionNumber, section.getSectionNumber())
                .isEquals();
    }
}
