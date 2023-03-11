import java.util.Arrays;
import java.util.Map;

public class Intervals {

    private static final String INTERVAL_NOTE_ORDER_ASC = "asc";
    private static final String INTERVAL_NOTE_ORDER_DESC = "dsc";

    public static final String ACCIDENTAL_SHARP = "#";
    public static final String ACCIDENTAL_DOUBLE_SHARP = "##";
    public static final String ACCIDENTAL_FLAT = "b";
    public static final String ACCIDENTAL_DOUBLE_FLAT = "bb";

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_INVALID_ARGUMENT_COUNT_MESSAGE =
            "Illegal number of arguments in input array. Array have to comprise 2, or 3 arguments.";
    private static final String ILLEGAL_ARGUMENT_EXCEPTION_INVALID_ARGUMENT_VALUE_MESSAGE =
            "Invalid value in derived arguments array.";

    private static final String PERFECT_OCTAVE_INTERVAL = "P8";

    private record Interval(String intervalName, Integer semitonesNumber, Integer degreeQuantity) {
    }

    private record Note(String noteName, int semitoneNumber, int currentDegree) {
    }

    private enum NOTE_ENUM {

        NOTE_C("C"),
        NOTE_D("D"),
        NOTE_E("E"),
        NOTE_F("F"),
        NOTE_G("G"),
        NOTE_A("A"),
        NOTE_B("B");

        private final String noteName;

        NOTE_ENUM(String noteName) {
            this.noteName = noteName;
        }

        public String getNoteName() {
            return noteName;
        }
    }

    private static final Map<String, Interval> INTERVALS_MAP = Map.ofEntries(
            Map.entry("m2", new Interval("Minor Second", 1, 2)),
            Map.entry("M2", new Interval("Major Second", 2, 2)),
            Map.entry("m3", new Interval("Minor Third", 3, 3)),
            Map.entry("M3", new Interval("Major Third", 4, 3)),
            Map.entry("P4", new Interval("Perfect Fourth", 5, 4)),
            Map.entry("P5", new Interval("Perfect Fifth", 7, 5)),
            Map.entry("m6", new Interval("Minor Sixth", 8, 6)),
            Map.entry("M6", new Interval("Major Sixth", 9, 6)),
            Map.entry("m7", new Interval("Minor Seventh", 10, 7)),
            Map.entry("M7", new Interval("Major Seventh", 11, 7)),
            Map.entry("P8", new Interval("Perfect Octave", 12, 8))
    );

    public static String intervalConstruction(String[] args) {

        if (args.length < 2 || args.length > 3) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_INVALID_ARGUMENT_COUNT_MESSAGE);
        }

        final String additionalIntervalName = args[0];
        final String furtherIntervalFirstNoteName = args[1].substring(0, 1); // get only Note without accidentals
        final String[] furtherIntervalFirstNoteAccidentals = args[1].substring(1).split("");
        final String intervalNoteOrder = args.length == 3 ? args[2] : INTERVAL_NOTE_ORDER_ASC;

        if (!areDerivedArgumentsValid(additionalIntervalName, furtherIntervalFirstNoteName, intervalNoteOrder))
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_INVALID_ARGUMENT_VALUE_MESSAGE);

        if (additionalIntervalName.equals(PERFECT_OCTAVE_INTERVAL))
            return furtherIntervalFirstNoteName;

        Note furtherIntervalFirstNote = getIntervalFirstNoteByNameAndAccidentals(furtherIntervalFirstNoteName, furtherIntervalFirstNoteAccidentals);
        Interval additionalInterval = getIntervalByName(additionalIntervalName);

        return getIntervalLastNoteByDegreeAndSemitone(furtherIntervalFirstNote, additionalInterval);
    }

    public static String intervalIdentification(String[] args) {
        return "";
    }

    private static Note getIntervalFirstNoteByNameAndAccidentals(String noteName, String[] noteAccidentals) {

        NOTE_ENUM enumNote = getNoteEnumValueByName(noteName);  // first Note in further interval (It's a note we start.)

        int semitonesNumberForNote = enumNote.ordinal() * 2;  // semitones THIS FIRST Note may have if it was the end of an interval
        int degreeNumberForNote = enumNote.ordinal();        // degree of the interval THIS FIRST may be situated in

        semitonesNumberForNote = semitonesNumberForNote + computeAdditionalSemitoneNumberAccordingToAccidentals(noteAccidentals); // add additional semitones for Note if it contains accidentals

        return new Note(enumNote.getNoteName(), semitonesNumberForNote, degreeNumberForNote);
    }

    private static int computeAdditionalSemitoneNumberAccordingToAccidentals(String[] noteAccidentals) {

        int additionalSemitonesForNote = 0;

        for (String possibleAccidental :
                noteAccidentals) {
            if (possibleAccidental.equals(ACCIDENTAL_SHARP))
                additionalSemitonesForNote++;
            else if (possibleAccidental.equals(ACCIDENTAL_FLAT)) {
                additionalSemitonesForNote--;
            }
        }

        return additionalSemitonesForNote;
    }

    private static NOTE_ENUM getNoteEnumValueByName(String noteName) {
        return Arrays.stream(NOTE_ENUM.values())
                .filter(note -> noteName.equals(note.getNoteName()))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    private static String getIntervalLastNoteByDegreeAndSemitone(Note firstNote, Interval additionalInterval) {

        int lastNoteIndexForInterval = getActualIntervalDegreeStartsWithNote(firstNote, additionalInterval);
        NOTE_ENUM lastIntervalNoteEnumValue = NOTE_ENUM.values()[lastNoteIndexForInterval];
        int actualSemitoneNumberForLastNoteInterval = getActualIntervalSemitonesStartsWithNote(firstNote, additionalInterval);
        int expectedSemitoneNumberForNewInterval = lastIntervalNoteEnumValue.ordinal() * 2; // semitones for the last Note of our interval we are building
        int differenceBetweenActualAndExpectedSemitoneNumbers = expectedSemitoneNumberForNewInterval - actualSemitoneNumberForLastNoteInterval;

        return getNoteNameAccordingToDifferenceBetweenActualAndExpectedSemitoneNumber(lastIntervalNoteEnumValue,
                differenceBetweenActualAndExpectedSemitoneNumbers);
    }

    private static String getNoteNameAccordingToDifferenceBetweenActualAndExpectedSemitoneNumber(NOTE_ENUM note,
                                                                                                 int differenceBetweenActualAndExpectedSemitoneNumbers) {
        String noteName = note.getNoteName();

        if (differenceBetweenActualAndExpectedSemitoneNumbers == -1) {
            return noteName.concat(ACCIDENTAL_FLAT);
        } else if (differenceBetweenActualAndExpectedSemitoneNumbers == -2) {
            return noteName.concat(ACCIDENTAL_DOUBLE_FLAT);
        }

        if (differenceBetweenActualAndExpectedSemitoneNumbers == 1) {
            return noteName.concat(ACCIDENTAL_SHARP);
        } else if (differenceBetweenActualAndExpectedSemitoneNumbers == 2) {
            return noteName.concat(ACCIDENTAL_DOUBLE_SHARP);
        }

        return noteName;
    }

    private static int getActualIntervalDegreeStartsWithNote(Note noteIntervalStartsWith, Interval interval) {

        int lastNoteIndexForInterval = noteIntervalStartsWith.currentDegree() +
                interval.degreeQuantity() - 1;

        if (lastNoteIndexForInterval > NOTE_ENUM.values().length)
            lastNoteIndexForInterval = lastNoteIndexForInterval - NOTE_ENUM.values().length;

        return lastNoteIndexForInterval;
    }

    private static int getActualIntervalSemitonesStartsWithNote(Note noteIntervalStartsWith, Interval interval) {

        int actualSemitoneNumberForLastNoteInterval = noteIntervalStartsWith.semitoneNumber() + interval.semitonesNumber();

        if (actualSemitoneNumberForLastNoteInterval > NOTE_ENUM.values().length)
            actualSemitoneNumberForLastNoteInterval = actualSemitoneNumberForLastNoteInterval - NOTE_ENUM.values().length * 2;

        return actualSemitoneNumberForLastNoteInterval;
    }

    private static boolean areDerivedArgumentsValid(String intervalName, String firstIntervalNote,
                                                    String intervalNoteOrder) {
        if (!INTERVALS_MAP.containsKey(intervalName))
            return false;

        if (!isNoteExistsByName(firstIntervalNote))
            return false;

        return intervalNoteOrder.contentEquals(INTERVAL_NOTE_ORDER_ASC) ||
                intervalNoteOrder.contentEquals(INTERVAL_NOTE_ORDER_DESC);
    }

    private static boolean isNoteExistsByName(String firstIntervalNote) {
        return Arrays.stream(NOTE_ENUM.values())
                .anyMatch(p -> p.getNoteName().equals(firstIntervalNote));
    }




    private static Interval getIntervalByName(String intervalName) {
        return INTERVALS_MAP.get(intervalName);
    }
}
