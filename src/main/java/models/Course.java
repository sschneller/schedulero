package models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static models.Util.nullToEmpty;

/**
 * Defines a course.
 */
public class Course {
    private boolean isOptional;
    private final List<Section> sections;
    private String name, subject, courseNumber;

    /**
     * Constructor.
     *
     * @param name the name of the course. e.g. Graphical User Interfaces
     * @param subject the subject of the course. e.g. CSC
     * @param courseNumber the course number. e.g. 420
     * @param isOptional set to true if the course does not have to be in every generated schedule.
     */
    public Course(final String name, final String subject, final String courseNumber, final boolean isOptional) {
        setName(name);
        setSubject(subject);
        setCourseNumber(courseNumber);
        setOptional(isOptional);
        sections = new ArrayList<>();
    }

    /**
     * Returns the sections of this course.
     *
     * @return the sections of this course.
     */
    @Nonnull
    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    /**
     * Adds a section to this course.
     *
     * @param section a section.
     */
    public void addSection(final Section section) {
        sections.add(section);
    }

    /**
     * Removes a section from this course if it exists.
     *
     * @param section a section.
     */
    public void removeSection(final Section section) {
        sections.remove(section);
    }

    /**
     * Returns true if the course does not have to be in every generated schedule.
     *
     * @return true if the course does not have to be in every generated schedule.
     */
    public boolean isOptional() {
        return isOptional;
    }

    /**
     * Set to true if the course does not have to be in every generated schedule.
     *
     * @param optional true if the course does not have to be in every generated schedule.
     */
    public void setOptional(final boolean optional) {
        isOptional = optional;
    }

    /**
     * Returns an optional course number.
     *
     * @return an optional course number.
     */
    @Nonnull
    public String getCourseNumber() {
        return courseNumber;
    }

    /**
     * Sets the course number.
     *
     * @param courseNumber a course number.
     */
    public void setCourseNumber(final String courseNumber) {
        this.courseNumber = nullToEmpty(courseNumber);
    }

    /**
     * Returns the name of this course. e.g. Graphical User Interfaces
     *
     * @return the name of this course.
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this course.
     *
     * @param name a course name.
     */
    public void setName(final String name) {
        this.name = nullToEmpty(name);
    }

    /**
     * Returns the subject of this course. e.g. CSC
     *
     * @return the subject of this course.
     */
    @Nonnull
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of this course.
     *
     * @param subject the subject of this course.
     */
    public void setSubject(final String subject) {
        this.subject = nullToEmpty(subject);
    }

    @Override
    public String toString() {
        return getSubject().trim() + getCourseNumber().trim() + (!getName().isEmpty() ? " - " + getName().trim() : "");
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(43, 73)
                .append(name)
                .append(subject)
                .append(courseNumber)
                .append(isOptional)
                .append(sections)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if(obj == this) {
            return true;
        } else if(!(obj instanceof Course)) {
            return false;
        }

        Course course = (Course) obj;

        return new EqualsBuilder()
                .append(name, course.getName())
                .append(subject, course.getSubject())
                .append(courseNumber, course.getCourseNumber())
                .append(isOptional, course.isOptional())
                .append(sections, course.getSections())
                .isEquals();
    }
}
