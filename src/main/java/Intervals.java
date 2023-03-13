import java.util.Map;

public class Intervals {

    private static final String INTERVAL_NOTE_ORDER_ASC = "asc"; // otherwise dsc

    public static final String ACCIDENTAL_SHARP = "#";
    public static final String ACCIDENTAL_DOUBLE_SHARP = "##";
    public static final String ACCIDENTAL_FLAT = "b";
    public static final String ACCIDENTAL_DOUBLE_FLAT = "bb";

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_INVALID_ARGUMENT_COUNT_MESSAGE =
            "Illegal number of elements in input array.";

    private static final int MAX_INTERVAL_COUNT = 8;
    private static final int MIN_INTERVAL_COUNT = 1;
    private static final int MAX_SEMITONE_NUMBER = 12;
    private static final int MIN_SEMITONE_NUMBER = 0;

    private static final String DEGREE_PROPERTY = "degree";
    private static final String SEMITONE_PROPERTY = "semitone";

    private static final String MINOR_SECOND_INTERVAL = "m2";
    private static final String MAJOR_SECOND_INTERVAL = "M2";
    private static final String MINOR_THIRD_INTERVAL = "m3";
    private static final String MAJOR_THIRD_INTERVAL = "M3";
    private static final String PERFECT_FOURTH_INTERVAL = "P4";
    private static final String PERFECT_FIFTH_INTERVAL = "P5";
    private static final String MINOR_SIXTH_INTERVAL = "m6";
    private static final String MAJOR_SIXTH_INTERVAL = "M6";
    private static final String MINOR_SEVENTH_INTERVAL = "m7";
    private static final String MAJOR_SEVENTH_INTERVAL = "M7";
    private static final String PERFECT_OCTAVE_INTERVAL = "P8";

    private static final Map<String, Map<String, Integer>> INTERVALS_MAP = Map.ofEntries(
            Map.entry(MINOR_SECOND_INTERVAL, Map.of(DEGREE_PROPERTY, 2,
                    SEMITONE_PROPERTY, 1)),
            Map.entry(MAJOR_SECOND_INTERVAL, Map.of(DEGREE_PROPERTY, 2,
                    SEMITONE_PROPERTY, 2)),
            Map.entry(MINOR_THIRD_INTERVAL, Map.of(DEGREE_PROPERTY, 3,
                    SEMITONE_PROPERTY, 3)),
            Map.entry(MAJOR_THIRD_INTERVAL, Map.of(DEGREE_PROPERTY, 3,
                    SEMITONE_PROPERTY, 4)),
            Map.entry(PERFECT_FOURTH_INTERVAL, Map.of(DEGREE_PROPERTY, 4,
                    SEMITONE_PROPERTY, 5)),
            Map.entry(PERFECT_FIFTH_INTERVAL, Map.of(DEGREE_PROPERTY, 5,
                    SEMITONE_PROPERTY, 7)),
            Map.entry(MINOR_SIXTH_INTERVAL, Map.of(DEGREE_PROPERTY, 6,
                    SEMITONE_PROPERTY, 8)),
            Map.entry(MAJOR_SIXTH_INTERVAL, Map.of(DEGREE_PROPERTY, 6,
                    SEMITONE_PROPERTY, 9)),
            Map.entry(MINOR_SEVENTH_INTERVAL, Map.of(DEGREE_PROPERTY, 7,
                    SEMITONE_PROPERTY, 10)),
            Map.entry(MAJOR_SEVENTH_INTERVAL, Map.of(DEGREE_PROPERTY, 7,
                    SEMITONE_PROPERTY, 11)),
            Map.entry(PERFECT_OCTAVE_INTERVAL, Map.of(DEGREE_PROPERTY, 8,
                    SEMITONE_PROPERTY, 12))
    );

    private static final String NOTE_C = "C";
    private static final String NOTE_D = "D";
    private static final String NOTE_E = "E";
    private static final String NOTE_F = "F";
    private static final String NOTE_G = "G";
    private static final String NOTE_A = "A";
    private static final String NOTE_B = "B";

    private static final Map<String, Integer> NOTE_DEFAULT_DEGREE_MAP = Map.of(
            NOTE_C, 1,
            NOTE_D, 2,
            NOTE_E, 3,
            NOTE_F, 4,
            NOTE_G, 5,
            NOTE_A, 6,
            NOTE_B, 7
    );

    private static final Map<String, Integer> NOTE_DEFAULT_SEMITONE_MAP = Map.of(
            NOTE_C, 0,
            NOTE_D, 2,
            NOTE_E, 4,
            NOTE_F, 5,
            NOTE_G, 7,
            NOTE_A, 9,
            NOTE_B, 11
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
     *
     */
    public static String intervalConstruction(String[] args) {

        if (args.length < 1 || args.length > 3)
            throw new IllegalNumberOfArgumentsException(
                    ILLEGAL_ARGUMENT_EXCEPTION_INVALID_ARGUMENT_COUNT_MESSAGE);

        String intervalName = args[0];

        String beginNoteName = args[1].substring(0, 1);
        String accidentalForNote = args[1].substring(1);

        String noteOrder = args.length == 3 ? args[2] : INTERVAL_NOTE_ORDER_ASC;

        if (intervalName.equals(PERFECT_OCTAVE_INTERVAL))
            return beginNoteName;

        // String - noteName, Map<String, integer> - properties of Semitones = x, Degree = y
        Map<String, Integer> constructedIntervalFirstNote =
                getNoteAccordingToNameAndAccidentals(beginNoteName, accidentalForNote);

        Map<String, Integer> constructedInterval = getConstructedIntervalByName(intervalName);

        Map<String, Integer> constructedIntervalExpectedLastNote =
                getExpectedConstructedIntervalLastNoteAccordingToFirstNoteAndNoteOrder(
                        constructedInterval, constructedIntervalFirstNote, noteOrder);

        return getActualLastNoteForIntervalAccordingToExpectedLastNote(
                constructedInterval, constructedIntervalExpectedLastNote,
                constructedIntervalFirstNote, noteOrder);
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

        return "";
    }

    private static Map<String, Integer> getConstructedIntervalByName(String intervalName) {
        return INTERVALS_MAP.get(intervalName);
    }

    private static Map<String, Integer> getNoteAccordingToNameAndAccidentals(
            String noteName, String accidentals) {

        int degreeNumberForNote = NOTE_DEFAULT_DEGREE_MAP.get(noteName);
        int semitoneNumberForNote = getNoteSemitoneNumberAccordingToNameAndAccidentals(
                noteName, accidentals);

        return Map.of(
                DEGREE_PROPERTY, degreeNumberForNote,
                SEMITONE_PROPERTY, semitoneNumberForNote
        );
    }

    private static int getNoteSemitoneNumberAccordingToNameAndAccidentals(
            String noteName, String accidentals) {

        int noteSemitoneNumber = NOTE_DEFAULT_SEMITONE_MAP.get(noteName);

        if (!accidentals.isBlank())
            noteSemitoneNumber += getAdditionalSemitonesFromAccidentals(accidentals);

        return noteSemitoneNumber;
    }

    private static int getAdditionalSemitonesFromAccidentals(String accidentals) {
        return ACCIDENTAL_TO_SEMITONE_NUMBER_CONFORMATION_MAP.get(accidentals);
    }

    private static Map<String, Integer> getExpectedConstructedIntervalLastNoteAccordingToFirstNoteAndNoteOrder(
            Map<String, Integer> constructedInterval, Map<String, Integer> constructedIntervalFirstNote, String noteOrder) {

        int lastNoteDegree = getConstructedIntervalLastNoteDegreeAccordingToIntervalNoteOrder(
                constructedInterval, constructedIntervalFirstNote, noteOrder);

        String constructedIntervalLastNoteName = getNoteNameByDegree(lastNoteDegree);

        return Map.of(
                DEGREE_PROPERTY, NOTE_DEFAULT_DEGREE_MAP.get(constructedIntervalLastNoteName),
                SEMITONE_PROPERTY, NOTE_DEFAULT_SEMITONE_MAP.get(constructedIntervalLastNoteName)
        );
    }

    private static String getNoteNameByDegree(int degree) {
        return NOTE_DEFAULT_DEGREE_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().equals(degree))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private static int getConstructedIntervalLastNoteDegreeAccordingToIntervalNoteOrder(
            Map<String, Integer> constructedInterval, Map<String, Integer> constructedIntervalFirstNote, String noteOrder) {

        int lastNoteDegree;

        if (noteOrder.equals(INTERVAL_NOTE_ORDER_ASC)) {
            lastNoteDegree = getConstructedIntervalLastNoteDegreeForAscNoteOrder(
                    constructedInterval, constructedIntervalFirstNote);
        } else {
            lastNoteDegree = getConstructedIntervalLastNoteDegreeForDescNoteOrder(
                    constructedInterval, constructedIntervalFirstNote);
        }

        return lastNoteDegree;
    }

    private static int getConstructedIntervalLastNoteDegreeForAscNoteOrder(
            Map<String, Integer> constructedInterval, Map<String, Integer> constructedIntervalFirstNote) {

        int lastNoteDegree = constructedIntervalFirstNote.get(DEGREE_PROPERTY) +
                constructedInterval.get(DEGREE_PROPERTY) - 1;

        if (lastNoteDegree >= MAX_INTERVAL_COUNT) // indicates that we have a note step over the note 'B' - 1 more additional semitone to achieve note 'C'
            lastNoteDegree = lastNoteDegree - MAX_INTERVAL_COUNT + 1;

        return lastNoteDegree;
    }

    private static int getConstructedIntervalLastNoteDegreeForDescNoteOrder(
            Map<String, Integer> constructedInterval, Map<String, Integer> constructedIntervalFirstNote) {

        int lastNoteDegree = constructedIntervalFirstNote.get(DEGREE_PROPERTY) -
                constructedInterval.get(DEGREE_PROPERTY) + 1;
        // 0 or less
        if (lastNoteDegree < MIN_INTERVAL_COUNT)
            lastNoteDegree = lastNoteDegree + MAX_INTERVAL_COUNT - 1;

        return lastNoteDegree;
    }

    private static String getActualLastNoteForIntervalAccordingToExpectedLastNote(
            Map<String, Integer> constructedInterval, Map<String, Integer> expectedLastNote,
            Map<String, Integer> constructedIntervalFirstNote, String noteOrder) {

        int actualSemitoneNumber = computeConstructedIntervalLastNoteSemitoneNumberAccordingToIntervalFirstNoteAndNoteOrder(
                constructedInterval, constructedIntervalFirstNote, noteOrder);

        int differenceBetweenActualAndExpectedSemitoneNumbers = actualSemitoneNumber -
                expectedLastNote.get(SEMITONE_PROPERTY);

        return generateActualIntervalLastNoteAccordingToActualAndExpectedSemitoneDifference(
                expectedLastNote, differenceBetweenActualAndExpectedSemitoneNumbers);
    }

    private static String generateActualIntervalLastNoteAccordingToActualAndExpectedSemitoneDifference(
            Map<String, Integer> expectedLastNote, int differenceBetweenActualAndExpectedSemitoneNumbers) {

        String actualLastNoteName = getNoteNameByDegree(expectedLastNote.get(DEGREE_PROPERTY));

        if (differenceBetweenActualAndExpectedSemitoneNumbers == 0)
            return actualLastNoteName;

        actualLastNoteName = actualLastNoteName
                .concat(SEMITONE_DIFFERENCE_TO_ACCIDENTAL_CONFORMATION_MAP
                        .get(differenceBetweenActualAndExpectedSemitoneNumbers));

        return actualLastNoteName;
    }

    private static int computeConstructedIntervalLastNoteSemitoneNumberAccordingToIntervalFirstNoteAndNoteOrder(
            Map<String, Integer> constructedInterval, Map<String, Integer> constructedIntervalFirstNote, String noteOrder) {

        int actualSemitoneNumber;

        if (noteOrder.equals(INTERVAL_NOTE_ORDER_ASC)) {
            actualSemitoneNumber = computeConstructedIntervalLastNoteActualSemitoneNumberForNoteOrderAsc(
                    constructedInterval, constructedIntervalFirstNote);
        } else {
            actualSemitoneNumber = computeConstructedIntervalLastNoteActualSemitoneNumberForNoteOrderDesc(
                    constructedInterval, constructedIntervalFirstNote);
        }

        return actualSemitoneNumber;
    }

    private static int computeConstructedIntervalLastNoteActualSemitoneNumberForNoteOrderAsc(
            Map<String, Integer> constructedInterval, Map<String, Integer> constructedIntervalFirstNote) {

        int actualSemitoneNumber = constructedInterval.get(SEMITONE_PROPERTY) +
                constructedIntervalFirstNote.get(SEMITONE_PROPERTY);

        if (actualSemitoneNumber >= MAX_SEMITONE_NUMBER)
            actualSemitoneNumber -= MAX_SEMITONE_NUMBER;

        return actualSemitoneNumber;
    }

    private static int computeConstructedIntervalLastNoteActualSemitoneNumberForNoteOrderDesc(
            Map<String, Integer> constructedInterval, Map<String, Integer> constructedIntervalFirstNote) {

        int actualSemitoneNumber = constructedIntervalFirstNote.get(SEMITONE_PROPERTY) -
                constructedInterval.get(SEMITONE_PROPERTY);

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
