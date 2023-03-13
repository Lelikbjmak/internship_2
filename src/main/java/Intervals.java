import java.util.Map;

public class Intervals {

    /**
     * Note order in our interval.
     * 2 cases: ASC or DESC
     * Dur toi the fact that we obtain only valid String of 'noteOrder'. If we obtain not 'asc' obviously it's desc.
     */
    private static final String INTERVAL_NOTE_ORDER_ASC = "asc"; // otherwise dsc

    /**
     * Accidentals for notes.
     * # - rise semitone for 1
     * ## - rise semitone for 2
     * b - reduce semitone for 1
     * bb - reduce semitone for 2
     */
    private static final String ACCIDENTAL_SHARP = "#";
    private static final String ACCIDENTAL_DOUBLE_SHARP = "##";
    private static final String ACCIDENTAL_FLAT = "b";
    private static final String ACCIDENTAL_DOUBLE_FLAT = "bb";

    /**
     * Exception messages.
     */
    private static final String ILLEGAL_ARGUMENT_EXCEPTION_INVALID_ARGUMENT_COUNT_MESSAGE =
            "Illegal number of elements in input array.";
    private static final String INTERVAL_NOT_DEFINED_EXCEPTION_MESSAGE =
            "Can't identify the interval.";

    /**
     * Max interval count - Consider all notes as a circe hence it's 8 intervals between all notes.
     * Min interval count - Each note essentially is a part of at least 1 interval. C - part of interval m2, D - M2, etc...
     */
    private static final int MAX_INTERVAL_COUNT = 8;
    private static final int MIN_INTERVAL_COUNT = 1;

    /**
     * Max semitones number for note is 11.
     * We pick cause when we overlap the 1st circle (transfer from B to D, etc...).
     * There is an interval after Note 'B' leads to Note 'C'. To eradicate often subtraction this interval we take 12.
     * <p>
     * Min - 0. From start Note 'C' has semitone 0.
     */
    private static final int MAX_SEMITONE_NUMBER = 12;
    private static final int MIN_SEMITONE_NUMBER = 0;

    /**
     * Default properties for both Interval and Note.
     */
    private static final String DEGREE_PROPERTY = "degree";
    private static final String SEMITONE_PROPERTY = "semitone";

    /**
     * All possible Intervals
     */
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

    /**
     * Description of all intervals. Each interval has a certain degree number and semitone number.
     */
    private static final Map<String, Map<String, Integer>> INTERVALS_MAP = Map.ofEntries(
            Map.entry(MINOR_SECOND_INTERVAL, Map.of(DEGREE_PROPERTY, 2, // Interval Name - degree number, semitone number.
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

    /**
     * All possible Notes.
     */
    private static final String NOTE_C = "C";
    private static final String NOTE_D = "D";
    private static final String NOTE_E = "E";
    private static final String NOTE_F = "F";
    private static final String NOTE_G = "G";
    private static final String NOTE_A = "A";
    private static final String NOTE_B = "B";

    /**
     * In this solution we consider notes as spots, with certain `index`.
     * This index equals the degree number of the interval current note is a part of.
     * Like Note 'C'. Start counting interval from the beginning, it indicates from Note 'C' ->
     * Note 'C' is a part of 1st interval (m2) and M2.
     * Next note 'D' is a part of M2 and m3, etc...
     * In that case our notes has their own DEFAULT degree number which equals degree number of the interval they are part of.
     */
    private static final Map<String, Integer> NOTE_DEFAULT_DEGREE_MAP = Map.of(
            NOTE_C, 1,
            NOTE_D, 2,
            NOTE_E, 3,
            NOTE_F, 4,
            NOTE_G, 5,
            NOTE_A, 6,
            NOTE_B, 7
    );

    /**
     * Due to the fact that out Notes has DEFAULT degree number.
     * They have their own DEFAULT semitone number. If they DON'T have any accidentals (#, ##, b, bb).
     * In that case we define a serial semitone number for each of the Notes.
     */
    private static final Map<String, Integer> NOTE_DEFAULT_SEMITONE_MAP = Map.of(
            NOTE_C, 0,
            NOTE_D, 2,
            NOTE_E, 4,
            NOTE_F, 5,
            NOTE_G, 7,
            NOTE_A, 9,
            NOTE_B, 11
    );

    /**
     * Semitones add or subtract semitones from note therefore according to definitions from Music.
     * bb - reduce tone for 2 semitones
     * b - reduce tone for 1 semitone
     * # - rise tone for  1 semitone
     * ## - rise tone for 2 semitones
     */
    private static final Map<String, Integer> ACCIDENTAL_TO_SEMITONE_NUMBER_CONFORMATION_MAP = Map.of(
            ACCIDENTAL_FLAT, -1,
            ACCIDENTAL_DOUBLE_FLAT, -2,
            ACCIDENTAL_SHARP, 1,
            ACCIDENTAL_DOUBLE_SHARP, 2
    );

    /**
     * According to difference between semitones we define an accidental symbol we eventually add to note.
     */
    private static final Map<Integer, String> SEMITONE_DIFFERENCE_TO_ACCIDENTAL_CONFORMATION_MAP = Map.of(
            -1, ACCIDENTAL_FLAT,
            -2, ACCIDENTAL_DOUBLE_FLAT,
            1, ACCIDENTAL_SHARP,
            2, ACCIDENTAL_DOUBLE_SHARP
    );

    /**
     * IntervalConstruction - construct interval according to Note order and first Note in sequence.
     *
     * @param args - Array of input arguments, contains:
     *             args[0] - interval name, (interval we'll build);
     *             args[1] - Note our constructional interval starts with;
     *             args[2](optional) - interval order - descending|ascending, if not present - asc.
     * @return - Note name interval ends with, if input array - not valid throw {@link IllegalNumberOfArgumentsException}
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

        Map<String, Integer> constructedIntervalFirstNote =
                getNoteAccordingToNameAndAccidentals(beginNoteName, accidentalForNote); //first Note with default properties

        Map<String, Integer> constructedInterval = getConstructedIntervalByName(intervalName); // interval with properties

        Map<String, Integer> constructedIntervalExpectedLastNote =
                getExpectedConstructedIntervalLastNoteAccordingToFirstNoteAndNoteOrder(
                        constructedInterval, constructedIntervalFirstNote, noteOrder);  // expected last note with default properties

        return getActualLastNoteForIntervalAccordingToExpectedLastNote(
                constructedInterval, constructedIntervalExpectedLastNote,
                constructedIntervalFirstNote, noteOrder);
    }

    /**
     * IntervalIdentifier - Identify an interval according to first, last note of interval + note order.
     *
     * @param args - Array of input arguments, contains:
     *             args[0] - first Note of interval;
     *             args[1] - last Note of interval;
     *             args[2](optional) - Note order - ascending|descending, if not present - asc.
     * @return - Interval name which is situated between two Notes. If interval isn't defined - throw {@link IntervalNotDefinedException}.
     */
    public static String intervalIdentification(String[] args) {

        String firstNoteName = args[0].substring(0, 1);
        String firstNoteAccidentals = args[0].substring(1);
        String lastNoteName = args[1].substring(0, 1);
        String lastNoteAccidentals = args[1].substring(1);
        String noteOrder = args.length == 3 ? args[2] : INTERVAL_NOTE_ORDER_ASC;

        if (args[0].equals(args[1]))
            return PERFECT_OCTAVE_INTERVAL;

        Map<String, Integer> identicalIntervalFirstNote = getNoteAccordingToNameAndAccidentals(
                firstNoteName, firstNoteAccidentals); // First Note of the interval

        Map<String, Integer> identicalIntervalLastNote = getNoteAccordingToNameAndAccidentals(
                lastNoteName, lastNoteAccidentals); // Last Note of the interval

        return identifyIntervalBetweenTwoNotes(
                identicalIntervalFirstNote, identicalIntervalLastNote, noteOrder); // Identified interval name
    }

    /**
     * Identify Intervals by Two Notes (first, last). Like found an Interval that comprises two certain Notes as begin and end.
     *
     * @param firstNote - First note of the interval we are identifying.
     * @param lastNote  - Last note of the interval we are identifying.
     * @param noteOrder - Note order in constructional interval.
     * @return - Name of identified interval. If interval is not defined - throw {@link IntervalNotDefinedException}.
     */
    private static String identifyIntervalBetweenTwoNotes(
            Map<String, Integer> firstNote, Map<String, Integer> lastNote, String noteOrder) {

        int degreeDifference = getNotesDegreeDifferenceAccordingNoteOrder(firstNote, lastNote, noteOrder);
        int semitoneDifference = getNotesSemitoneDifferenceAccordingNoteOrder(firstNote, lastNote, noteOrder);

        return identifyIntervalAccordingToDegreeAndSemitoneDifferences(degreeDifference, semitoneDifference);
    }

    /**
     * Find Interval between two Notes according to their degree and semitone differences.
     * Throw exception if Interval with such properties wasn't found.
     *
     * @param degreeDifference - Degree difference.
     * @param semitoneDifference - Semitone difference.
     * @return Interval name with certain properties (degree = degreeDifference, semitone = semitoneDifference).
     *
     */
    private static String identifyIntervalAccordingToDegreeAndSemitoneDifferences(int degreeDifference, int semitoneDifference) {
        return INTERVALS_MAP.entrySet().stream()
                .filter(interval -> interval.getValue().get(DEGREE_PROPERTY).equals(degreeDifference) &&
                        interval.getValue().get(SEMITONE_PROPERTY).equals(semitoneDifference))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() ->
                        new IntervalNotDefinedException(INTERVAL_NOT_DEFINED_EXCEPTION_MESSAGE));
    }

    /**
     * @param firstNote - First Note of interval.
     * @param lastNote  - Last Note of interval.
     * @param noteOrder - Order of Notes.
     * @return - Semitone difference between First Note and Last Note of identical interval.
     */
    private static int getNotesSemitoneDifferenceAccordingNoteOrder(
            Map<String, Integer> firstNote, Map<String, Integer> lastNote, String noteOrder) {

        int semitoneDifferenceBetweenNotes;
        if (noteOrder.equals(INTERVAL_NOTE_ORDER_ASC))
            semitoneDifferenceBetweenNotes = getNotesSemitoneDifferenceNoteOrderAsc(firstNote, lastNote);
        else
            semitoneDifferenceBetweenNotes = getNotesSemitoneDifferenceNoteOrderDesc(firstNote, lastNote);

        return semitoneDifferenceBetweenNotes;
    }

    private static int getNotesSemitoneDifferenceNoteOrderAsc(
            Map<String, Integer> firstNote, Map<String, Integer> lastNote) {

        int notesSemitoneDifference = lastNote.get(SEMITONE_PROPERTY) -
                firstNote.get(SEMITONE_PROPERTY);

        if (notesSemitoneDifference <= MIN_SEMITONE_NUMBER)
            notesSemitoneDifference += MAX_SEMITONE_NUMBER;

        return notesSemitoneDifference;
    }

    private static int getNotesSemitoneDifferenceNoteOrderDesc(
            Map<String, Integer> firstNote, Map<String, Integer> lastNote) {

        int notesSemitoneDifference = firstNote.get(SEMITONE_PROPERTY) -
                lastNote.get(SEMITONE_PROPERTY);

        if (notesSemitoneDifference <= MIN_SEMITONE_NUMBER)
            notesSemitoneDifference += MAX_SEMITONE_NUMBER;

        return notesSemitoneDifference;
    }

    /**
     * @param firstNote - First Note of the interval.
     * @param lastNote  - Last Note of the interval.
     * @return - Degree difference between Notes, according to Note order.
     */
    private static int getNotesDegreeDifferenceAccordingNoteOrder(
            Map<String, Integer> firstNote, Map<String, Integer> lastNote, String noteOrder) {

        int degreeDifferenceBetweenNotes;
        if (noteOrder.equals(INTERVAL_NOTE_ORDER_ASC))
            degreeDifferenceBetweenNotes = getNotesDegreeDifferenceNoteOrderAsc(firstNote, lastNote);
        else
            degreeDifferenceBetweenNotes = getNotesDegreeDifferenceNoteOrderDesc(firstNote, lastNote);

        return degreeDifferenceBetweenNotes;
    }

    private static int getNotesDegreeDifferenceNoteOrderAsc(
            Map<String, Integer> firstNote, Map<String, Integer> lastNote) {

        int degreeDifferenceBetweenNotes = lastNote.get(DEGREE_PROPERTY) -
                firstNote.get(DEGREE_PROPERTY) + 1;

        if (degreeDifferenceBetweenNotes < MIN_INTERVAL_COUNT) // indicates that we have a note step over the note 'B' - 1 more additional semitone to achieve note 'C'
            degreeDifferenceBetweenNotes = degreeDifferenceBetweenNotes + MAX_INTERVAL_COUNT - 1;

        return degreeDifferenceBetweenNotes;
    }

    private static int getNotesDegreeDifferenceNoteOrderDesc(
            Map<String, Integer> firstNote, Map<String, Integer> lastNote) {

        int degreeDifferenceBetweenNotes = firstNote.get(DEGREE_PROPERTY) -
                lastNote.get(DEGREE_PROPERTY) + 1;

        if (degreeDifferenceBetweenNotes < MIN_INTERVAL_COUNT) // indicates that we have a note step over the note 'B' - 1 more additional semitone to achieve note 'C'
            degreeDifferenceBetweenNotes = degreeDifferenceBetweenNotes + MAX_INTERVAL_COUNT - 1;

        return degreeDifferenceBetweenNotes;
    }

    /**
     * @param intervalName - Interval name.
     * @return - Interval with properties (degree, semitones).
     */
    private static Map<String, Integer> getConstructedIntervalByName(String intervalName) {
        return INTERVALS_MAP.get(intervalName);
    }

    /**
     * @param noteName    - Note name.
     * @param accidentals - Accidentals, that provides us with additional semitones for Note.
     * @return - Note with default properties(degree, semitones).
     */
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

    /**
     * @param noteName    - Note name.
     * @param accidentals - Note accidentals.
     * @return - Add complete semitone number for current Note.
     * <p>
     * If Note has accidentals, take into account additional semitones we extract from accidentals.
     * Otherwise, return DEFAULT Note semitone number
     */
    private static int getNoteSemitoneNumberAccordingToNameAndAccidentals(
            String noteName, String accidentals) {

        int noteSemitoneNumber = NOTE_DEFAULT_SEMITONE_MAP.get(noteName);

        if (!accidentals.isBlank())
            noteSemitoneNumber += getAdditionalSemitonesFromAccidentals(accidentals);

        return noteSemitoneNumber;
    }

    /**
     * @param accidentals - Accidentals for Note.
     * @return - Extract additional semitones from accidentals.
     */
    private static int getAdditionalSemitonesFromAccidentals(String accidentals) {
        return ACCIDENTAL_TO_SEMITONE_NUMBER_CONFORMATION_MAP.get(accidentals);
    }

    /**
     * @param constructedInterval          - Interval.
     * @param constructedIntervalFirstNote - First note of the interval.
     * @param noteOrder                    - Order of notes in interval.
     * @return - Expected interval last note (with default params(degree + semitone)).
     *
     * This 'expected' note is a note, which has a degree = intervalDegree + firstNoteDegree.
     */
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

    /**
     * @param degree - Note default Degree.
     * @return - Name of the Note with given degree property.
     */
    private static String getNoteNameByDegree(int degree) {
        return NOTE_DEFAULT_DEGREE_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().equals(degree))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    /**
     * @param constructedInterval          - Interval we are building.
     * @param constructedIntervalFirstNote - First note in the interval.
     * @param noteOrder                    - Order of notes (asc|desc).
     * @return - Degree of the last Note of a certain interval we are building. According to Note order in the interval.
     */
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

    /**
     * @param constructedInterval          - Interval we have built
     * @param expectedLastNote             - Last note we anticipate to observe as the end of the Interval
     * @param constructedIntervalFirstNote - First note of the interval we have built
     * @param noteOrder                    - Order of notes in Interval (asc|desc)
     * @return - Actual note name according to semitones.
     * Cause the whole letter, like ('C', 'D', ...) we have found thanks to degree - it's our 'expected last note'.
     * To obtain a fullName of the last note, get 'Actual' last note we have to process difference between semitones.
     * According to that difference we arrive at a conclusion, what accidental to add in order to correspond semitone numbers.
     * Like Expected Note = A(degree = 6, semitone = 9). Actual number of semitones is 10 hence we have to make them equal.
     * To accomplish that we have to add SHARP symbol after our expected Note name.
     * As a result we have A (expected) -> A# (actual).
     */
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

    /**
     * Generate actual Last Note, reference is 'expected' Last Note. It means that we have name of the last Note.
     * Only one but - semitone number. According to difference between semitone numbers of the expected and actual Notes.
     * We add accidental symbol to actual Note to make semitone equal to semitone of the expected Note.
     * It'll be ultimate Note name.
     *
     * @param expectedLastNote - Expected last Note.
     * @param differenceBetweenActualAndExpectedSemitoneNumbers - Expected and actual semitones difference.
     * @return - Actual last Note name, according to semitone difference.
     */
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

    /**
     * Compute Interval last Note semitone number according to first Interval Note adn Note order.
     *
     * @param constructedInterval - Constructed Interval.
     * @param constructedIntervalFirstNote - First Note of the constructed Interval.
     * @param noteOrder - Interval Note order.
     * @return - Semitone number for the last Note of the interval.
     */
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

    /**
     * IllegalNumberOfArgumentsException - Exception if interval construction args Array contain not valid number of args.
     */
    private static class IllegalNumberOfArgumentsException extends RuntimeException {
        public IllegalNumberOfArgumentsException(String message) {
            super(message);
        }
    }

    /**
     * IntervalNotDefinedException - Exception if interval is not defined by two Notes.
     */
    private static class IntervalNotDefinedException extends RuntimeException {
        public IntervalNotDefinedException(String message) {
            super(message);
        }
    }
}
