package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;

/**
 * Schedule Generator Class.
 */
public class Generator {
    private final Gson gson;
    private final Set<Course> courses;

    /**
     * Constructor.
     */
    public Generator() {
        gson    = new Gson();
        courses = new HashSet<>();
    }

    /**
     * Generates all possible schedules with the course information provided.
     *
     * @return a set of schedule objects.
     */
    public Set<Schedule> generate() {
        final List<Course> mandatory = new ArrayList<>();
        final List<Course> optional = new ArrayList<>();
        courses.forEach(c -> {
            if(c.isOptional()) {
                optional.add(c);
            } else {
                mandatory.add(c);
            }
        });

        System.out.println("Mandatory: " + mandatory.size());
        System.out.println("Optional: " + optional.size());

        System.out.println("Schedule:");

        final Set<Schedule> mandatorySchedules = generate(Collections.unmodifiableList(mandatory), new Schedule(), false);
        final Set<Schedule> optionalSchedules = generate(Collections.unmodifiableList(optional), new Schedule(), true);
        System.out.println("HI!");
        return mandatorySchedules;
//        // Generate the optional class schedules
//        final Set<Schedule> optionalSchedules = generate(Collections.unmodifiableList(optional), new Schedule(), true);
//
//        // Check for left out optional classes
//        optional.parallelStream()
//                .flatMap(c -> c.getSections().parallelStream()
//                        .map(s -> Pair.of(c,s)))
//                .filter(p -> optionalSchedules.parallelStream()
//                        .noneMatch(s -> s.getSchedule().contains(p)))
//                .map(p -> {
//                    final Schedule s = new Schedule();
//                    s.addCourse(p.getLeft(), p.getRight());
//                    return s;})
//                .forEach(optionalSchedules::add);
//
//        // Create the schedules with optional classes
//        return generate(Collections.unmodifiableList(mandatory), new Schedule(), false).stream()
//                .flatMap(s -> optionalSchedules.parallelStream().map(os -> {
//            final Schedule schedule = s.duplicate();
//            os.getSchedule().stream()
//                    .filter(p -> s.fits(p.getRight())).forEach(p -> schedule.addCourse(p.getLeft(), p.getRight()));
//            return schedule;
//        })).collect(Collectors.toSet());
    }

    /**
     * Generates schedules from the provided classes.
     *
     * @param courses the courses used to generate the schedules.
     * @param schedule the base schedule.
     * @param isOptional if true, the schedules generated will vary in length because non fit classes will be removed.
     * @return valid course schedules.
     */
    @Nonnull
    private Set<Schedule> generate(final List<Course> courses, final Schedule schedule, final boolean isOptional) {
        // Check if there is no more courses
        if(courses.isEmpty()) {
            if(schedule.getSchedule().isEmpty()) {
                return new HashSet<>();
            } else {
                return new HashSet<>(Collections.singletonList(schedule));
            }
        }

        // Get the current course
        final Course course = courses.get(0);
        // Remove the current course from the courses list
        System.out.println("Size: " + courses.size() + " Begin: " + Math.min(1,courses.size() - 1));
        final List<Course> coursesLeft = courses.size() == 1 ? new ArrayList<>(1) : courses.subList(1, courses.size());
        // Create the schedules set
        final Set<Schedule> schedules = course.getSections().size() == 0 ? new HashSet<>(Collections.singletonList(schedule)) : new HashSet<>();

        System.out.println("Current Course: " + course.getName());
        System.out.println("\tSections: " + course.getSections().size());
        System.out.println("\tCourses Left: " + coursesLeft.size());

        // Iterate through the sections
        course.getSections().forEach(s -> {
            System.out.println("\tSection: " + s.getTeacher() + " - " + s.getSectionNumber());
            if(schedule.fits(s)) {
                final Schedule newSchedule = schedule.duplicate();
                newSchedule.addCourse(course, s);
                schedules.addAll(generate(coursesLeft, newSchedule, isOptional));
            } else if(isOptional) {
                schedules.addAll(generate(coursesLeft, schedule.duplicate(), isOptional));
            }
        });

        return schedules;
    }

    /**
     * Adds the course to the generator.
     *
     * @param course the course to add.
     */
    public void addCourse(final Course course) {
        courses.add(course);
    }

    /**
     * Removes the course if it exists.
     *
     * @param course the course to be removed.
     */
    public void removeCourse(final Course course) {
        courses.remove(course);
    }

    /**
     * Returns a unmodifiable view of the courses set.
     *
     * @return a unmodifiable view of the courses set.
     */
    @Nonnull
    public Set<Course> getCourses() {
        return Collections.unmodifiableSet(courses);
    }

    /**
     * Exports the generator to a file for importing later.
     *
     * @param exportFile the file to exportCourses to.
     * @throws IOException when the file cannot be written or accessed.
     * @see #importCourses(File)
     */
    public void exportCourses(final File exportFile) throws IOException {
        try(final Writer writer = new FileWriter(exportFile)) {
            gson.toJson(courses, writer);
        }
    }

    /**
     * Constructor. This is not a constructor, Mateusz. -Penis
     *
     * @param importFile the file to load the courses from.
     * @throws IOException when the file cannot be accessed.
     * @see #exportCourses(File)
     */
    public void importCourses(final File importFile) throws IOException {
        try(final Reader reader = new FileReader(importFile)) {
            System.out.println(courses);
            courses.addAll(gson.fromJson(reader, new TypeToken<Set<Course>>(){}.getType()));
        }
    }
}
