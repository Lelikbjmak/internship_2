import java.util.Arrays;
import java.util.Map;

public class Intervals {

    private static final String INTERVAL_NOTE_ORDER_ASC = "asc"; // otherwise dsc

    public static final String ACCIDENTAL_SHARP = "#";
    public static final String ACCIDENTAL_DOUBLE_SHARP = "##";
    public static final String ACCIDENTAL_FLAT = "b";
    public static final String ACCIDENTAL_DOUBLE_FLAT = "bb";

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_INVALID_ARGUMENT_COUNT_MESSAGE =
            "Illegal number of elements in input array.";

    private static final String PERFECT_OCTAVE_INTERVAL = "P8";

    private static final int MAX_INTERVAL_COUNT = 8;
    private static final int MIN_INTERVAL_COUNT = 1;
    private static final int MAX_SEMITONE_NUMBER = 12;
    private static final int MIN_SEMITONE_NUMBER = 0;

    private record Interval(IntervalProperty intervalProperties, Note firstIntervalNote, String noteOrder) {
    }

    private record IntervalProperty(String intervalName, Integer semitonesNumber, Integer degreeQuantity) {
    }

    private record Note(String noteName, int semitoneNumber, int currentDegree) {
    }

    private enum NOTE_NAME_ENUM {

        NOTE_C("C"),
        NOTE_D("D"),
        NOTE_E("E"),
        NOTE_F("F"),
        NOTE_G("G"),
        NOTE_A("A"),
        NOTE_B("B");

        private final String noteName;

        NOTE_NAME_ENUM(String noteName) {
            this.noteName = noteName;
        }

        public String getNoteName() {
            return noteName;
        }
    }

    private enum NOTE_DEGREE_SEMITONE_ENUM {

        NOTE_C(0),
        NOTE_D(2),
        NOTE_E(4),
        NOTE_F(5),
        NOTE_G(7),
        NOTE_A(9),
        NOTE_B(11);

        private final int defaultSemitoneNumber;

        NOTE_DEGREE_SEMITONE_ENUM(int semitoneNumber) {
            this.defaultSemitoneNumber = semitoneNumber;
        }

        public int getDefaultSemitoneNumber() {
            return defaultSemitoneNumber;
        }

        public int getDefaultDegreeNumber() {
            return this.ordinal() + 1;
        }
    }

    private static final Map<String, IntervalProperty> INTERVAL_PROPERTIES_MAP = Map.ofEntries(
            Map.entry("m2", new IntervalProperty("Minor Second", 1, 2)),
            Map.entry("M2", new IntervalProperty("Major Second", 2, 2)),
            Map.entry("m3", new IntervalProperty("Minor Third", 3, 3)),
            Map.entry("M3", new IntervalProperty("Major Third", 4, 3)),
            Map.entry("P4", new IntervalProperty("Perfect Fourth", 5, 4)),
            Map.entry("P5", new IntervalProperty("Perfect Fifth", 7, 5)),
            Map.entry("m6", new IntervalProperty("Minor Sixth", 8, 6)),
            Map.entry("M6", new IntervalProperty("Major Sixth", 9, 6)),
            Map.entry("m7", new IntervalProperty("Minor Seventh", 10, 7)),
            Map.entry("M7", new IntervalProperty("Major Seventh", 11, 7)),
            Map.entry("P8", new IntervalProperty("Perfect Octave", 12, 8))
    );

    private static final Map<String, Integer> ACCIDENTAL_TO_SEMITONE_NUMBER_CONFORMATION_MAP = Map.of(
            ACCIDENTAL_FLAT, -1,
            ACCIDENTAL_DOUBLE_FLAT, -2,
            ACCIDENTAL_SHARP, 1,
            ACCIDENTAL_DOUBLE_SHARP, 2
    );

    private static final Map<Integer, String> SEMITONE_DIFFERENCE_TO_ACCIDENTAL_CONFORMATION_MAP = Map.of(
            -1, ACCIDENTAL_FLAT,
            -2, ACCIDENTAL_DOUBLE_FLAT,
            1, ACCIDENTAL_SHARP,
            2, ACCIDENTAL_DOUBLE_SHARP
    );

    /**
     * IntervalConstruct - Construct Interval from certain {@link Note} to another Note according to Interval properties
     *
     * @param args - Contain: args[0] - {@link Interval} we're going to build,
     *             args[1] - N{@link Note} we start our interval,
     *             args[2] - optional, noteOrder (asc/desc)
     * @return - Name of the LAST Note, our constructed interval comprises
     */
    public static String intervalConstruction(String[] args) {

        if (args.length < 1 || args.length > 3)
            throw new IllegalNumberOfArgumentsException(ILLEGAL_ARGUMENT_EXCEPTION_INVALID_ARGUMENT_COUNT_MESSAGE);

        String intervalName = args[0];

        String beginNoteName = args[1].substring(0, 1);
        String accidentalForNote = args[1].substring(1);

        String noteOrder = args.length == 3 ? args[2] : INTERVAL_NOTE_ORDER_ASC;

        if (intervalName.equals(PERFECT_OCTAVE_INTERVAL))
            return beginNoteName;

        Note constructedIntervalFirstNote = getNoteAccordingToNameAndAccidentals(beginNoteName, accidentalForNote);

        IntervalProperty constructedIntervalProperties = getConstructionalIntervalPropertiesByName(intervalName);
        Interval constructedInterval = constructIntervalAccordingToPropertiesAndFirstNote(constructedIntervalProperties, constructedIntervalFirstNote, noteOrder);

        Note constructedIntervalExpectedLastNote = getExpectedConstructedIntervalLastNote(constructedInterval);
        Note constructedIntervalActualLastNote = getActualLastNoteForIntervalAccordingToExpectedLastNote(constructedInterval, constructedIntervalExpectedLastNote);

        return constructedIntervalActualLastNote.noteName();
    }

    /**
     * IntervalIdentifier
     *
     * @param args
     * @return
     */
    public static String intervalIdentification(String[] args) {

        String firstNoteName = args[0].substring(0, 1);
        String firstNoteAccidentals = args[0].substring(1);
        String lastNoteName = args[1].substring(0, 1);
        String lastNoteAccidentals = args[1].substring(1);
        String noteOrder = args.length > 1 ? args[2] : INTERVAL_NOTE_ORDER_ASC;

        Note firstNote = getNoteAccordingToNameAndAccidentals(firstNoteName, firstNoteAccidentals);
        Note lastNote = getNoteAccordingToNameAndAccidentals(lastNoteAccidentals, lastNoteAccidentals);

        int degreeDifference = firstNote.currentDegree - lastNote.currentDegree;
        int semitoneDifference = firstNote.semitoneNumber - lastNote.semitoneNumber;

        return "";
    }

    private static IntervalProperty getConstructionalIntervalPropertiesByName(String intervalName) {
        return INTERVAL_PROPERTIES_MAP.get(intervalName);
    }

    private static Note getNoteAccordingToNameAndAccidentals(String briefNoteName, String accidentals) {

        NOTE_NAME_ENUM currentNoteFullName = getNoteFullName(briefNoteName);
        NOTE_DEGREE_SEMITONE_ENUM degreeAndSemitoneForCurrentNote = NOTE_DEGREE_SEMITONE_ENUM.valueOf(currentNoteFullName.name());

        int degreeNumberForNote = degreeAndSemitoneForCurrentNote.getDefaultDegreeNumber();
        int semitoneNumberForNote = getNoteSemitoneNumberAccordingToAccidentals(degreeAndSemitoneForCurrentNote, accidentals);

        return new Note(currentNoteFullName.getNoteName(), semitoneNumberForNote, degreeNumberForNote);
    }

    private static NOTE_NAME_ENUM getNoteFullName(String briefNoteName) {
        return Arrays.stream(NOTE_NAME_ENUM.values())
                .filter(note -> note.getNoteName().equals(briefNoteName))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private static int getNoteSemitoneNumberAccordingToAccidentals(NOTE_DEGREE_SEMITONE_ENUM defaultNoteProperties,
                                                                   String accidentals) {
        int noteSemitoneNumber = defaultNoteProperties.getDefaultSemitoneNumber();

        if (!accidentals.isBlank())
            noteSemitoneNumber += getAdditionalSemitonesFromAccidentals(accidentals);

        return noteSemitoneNumber;
    }

    private static int getAdditionalSemitonesFromAccidentals(String accidentals) {
        return ACCIDENTAL_TO_SEMITONE_NUMBER_CONFORMATION_MAP.get(accidentals);
    }

    private static Note getExpectedConstructedIntervalLastNote(Interval constructedInterval) {

        int lastNoteDegree = getConstructedIntervalLastNoteDegreeAccordingToIntervalNoteOrder(constructedInterval);

        NOTE_NAME_ENUM constructedIntervalLastNoteName = NOTE_NAME_ENUM.values()[lastNoteDegree - 1];
        NOTE_DEGREE_SEMITONE_ENUM lastNoteActualDegreeAndSemitones = NOTE_DEGREE_SEMITONE_ENUM.valueOf(constructedIntervalLastNoteName.name());

        return new Note(constructedIntervalLastNoteName.getNoteName(),
                lastNoteActualDegreeAndSemitones.getDefaultSemitoneNumber(), lastNoteActualDegreeAndSemitones.getDefaultDegreeNumber());
    }

    private static int getConstructedIntervalLastNoteDegreeAccordingToIntervalNoteOrder(Interval constructedInterval) {

        int lastNoteDegree;

        if (constructedInterval.noteOrder().equals(INTERVAL_NOTE_ORDER_ASC)) {
            lastNoteDegree = getConstructedIntervalLastNoteDegreeForAscNoteOrder(constructedInterval);
        } else {
            lastNoteDegree = getConstructedIntervalLastNoteDegreeForDescNoteOrder(constructedInterval);
        }

        return lastNoteDegree;
    }

    private static int getConstructedIntervalLastNoteDegreeForAscNoteOrder(Interval constructedInterval) {

        int lastNoteDegree = constructedInterval.firstIntervalNote().currentDegree() +
                constructedInterval.intervalProperties().degreeQuantity() - 1;

        if (lastNoteDegree >= MAX_INTERVAL_COUNT) // indicates that we have a note step over the note 'B' - 1 more additional semitone to achieve note 'C'
            lastNoteDegree = lastNoteDegree - MAX_INTERVAL_COUNT + 1;

        return lastNoteDegree;
    }

    private static int getConstructedIntervalLastNoteDegreeForDescNoteOrder(Interval constructedInterval) {

        int lastNoteDegree = constructedInterval.firstIntervalNote().currentDegree() -
                constructedInterval.intervalProperties().degreeQuantity() + 1;
        // 0 or less
        if (lastNoteDegree < MIN_INTERVAL_COUNT)
            lastNoteDegree = lastNoteDegree + MAX_INTERVAL_COUNT - 1;

        return lastNoteDegree;
    }

    private static Interval constructIntervalAccordingToPropertiesAndFirstNote(IntervalProperty constructedIntervalProperties, Note constructedIntervalFirstNote, String noteOrder) {
        return new Interval(constructedIntervalProperties, constructedIntervalFirstNote, noteOrder);
    }

    private static Note getActualLastNoteForIntervalAccordingToExpectedLastNote(Interval constructedInterval, Note expectedLastNote) {

        int actualSemitoneNumber = computeConstructedIntervalLastNoteSemitoneNumberAccordingToIntervalFirstNoteAndNoteOrder(constructedInterval);

        int differenceBetweenActualAndExpectedSemitoneNumbers = actualSemitoneNumber - expectedLastNote.semitoneNumber();

        String actualLastNoteName = generateActualIntervalLastNoteAccordingToActualAndExpectedSemitoneDifference(expectedLastNote,
                differenceBetweenActualAndExpectedSemitoneNumbers);

        return new Note(actualLastNoteName, actualSemitoneNumber, expectedLastNote.currentDegree());
    }

    private static String generateActualIntervalLastNoteAccordingToActualAndExpectedSemitoneDifference(Note expectedLastNote, int differenceBetweenActualAndExpectedSemitoneNumbers) {

        String actualLastNoteName = expectedLastNote.noteName();

        if (differenceBetweenActualAndExpectedSemitoneNumbers == 0)
            return actualLastNoteName;

        actualLastNoteName = actualLastNoteName
                .concat(SEMITONE_DIFFERENCE_TO_ACCIDENTAL_CONFORMATION_MAP
                        .get(differenceBetweenActualAndExpectedSemitoneNumbers));
        return actualLastNoteName;
    }

    private static int computeConstructedIntervalLastNoteSemitoneNumberAccordingToIntervalFirstNoteAndNoteOrder(Interval constructedInterval) {

        int actualSemitoneNumber;

        if (constructedInterval.noteOrder().equals(INTERVAL_NOTE_ORDER_ASC)) {
            actualSemitoneNumber = computeConstructedIntervalLastNoteActualSemitoneNumberForNoteOrderAsc(constructedInterval);
        } else {
            actualSemitoneNumber = computeConstructedIntervalLastNoteActualSemitoneNumberForNoteOrderDesc(constructedInterval);
        }

        return actualSemitoneNumber;
    }

    private static int computeConstructedIntervalLastNoteActualSemitoneNumberForNoteOrderAsc(Interval constructedInterval) {

        int actualSemitoneNumber = constructedInterval.intervalProperties().semitonesNumber() +
                constructedInterval.firstIntervalNote().semitoneNumber();

        if (actualSemitoneNumber >= MAX_SEMITONE_NUMBER)
            actualSemitoneNumber -= MAX_SEMITONE_NUMBER;

        return actualSemitoneNumber;
    }

    private static int computeConstructedIntervalLastNoteActualSemitoneNumberForNoteOrderDesc(Interval constructedInterval) {

        int actualSemitoneNumber = constructedInterval.firstIntervalNote().semitoneNumber() -
                constructedInterval.intervalProperties().semitonesNumber();

        if (actualSemitoneNumber <= MIN_SEMITONE_NUMBER)
            actualSemitoneNumber += MAX_SEMITONE_NUMBER;

        return actualSemitoneNumber;
    }

    private static class IllegalNumberOfArgumentsException extends RuntimeException {
        public IllegalNumberOfArgumentsException(String message) {
            super(message);
        }
    }

}
