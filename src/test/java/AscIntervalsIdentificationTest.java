import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AscIntervalsIdentificationTest {


    private static final String ASC_NOTE_ORDER = "asc";

    private enum NOTE {

        NOTE_C_DOUBLE_FLAT("Cbb"),
        NOTE_C_FLAT("Cb"),
        NOTE_C("C"),
        NOTE_C_SHARP("C#"),
        NOTE_C_DOUBLE_SHARP("C##"),

        NOTE_D_DOUBLE_fLAT("Dbb"),
        NOTE_D_FLAT("Db"),
        NOTE_D("D"),
        NOTE_D_SHARP("D#"),
        NOTE_D_DOUBLE_SHARP("D##"),

        NOTE_E_DOUBLE_fLAT("Ebb"),
        NOTE_E_FLAT("Eb"),
        NOTE_E("E"),
        NOTE_E_SHARP("E#"),
        NOTE_E_DOUBLE_SHARP("E##"),

        NOTE_F_DOUBLE_fLAT("Fbb"),
        NOTE_F_FLAT("Fb"),
        NOTE_F("F"),
        NOTE_F_SHARP("F#"),
        NOTE_F_DOUBLE_SHARP("F##"),

        NOTE_G_DOUBLE_fLAT("Gbb"),
        NOTE_G_FLAT("Gb"),
        NOTE_G("G"),
        NOTE_G_SHARP("G#"),
        NOTE_G_DOUBLE_SHARP("G##"),

        NOTE_A_DOUBLE_fLAT("Abb"),
        NOTE_A_FLAT("Ab"),
        NOTE_A("A"),
        NOTE_A_SHARP("A#"),
        NOTE_A_DOUBLE_SHARP("A##"),

        NOTE_B_DOUBLE_fLAT("Bbb"),
        NOTE_B_FLAT("Bb"),
        NOTE_B("B"),
        NOTE_B_SHARP("B#"),
        NOTE_B_DOUBLE_SHARP("B##");

        private final String noteName;

        NOTE(String noteName) {
            this.noteName = noteName;
        }
    }

    private enum INTERVAL {
        MINOR_SECOND("m2"),
        MAJOR_SECOND("M2"),

        MINOR_THIRD("m3"),
        MAJOR_THIRD("M3"),

        PERFECT_FOURTH("P4"),

        PERFECT_FIFTH("P5"),

        MINOR_SIXTH("m6"),
        MAJOR_SIXTH("M6"),

        MINOR_SEVENTH("m7"),
        MAJOR_SEVENTH("M7"),

        PERFECT_OCTAVE("P8");

        private final String intervalName;

        INTERVAL(String intervalName) {
            this.intervalName = intervalName;
        }
    }

    @Test
    public void ascIntervalIdentificationTest() {

        Assertions.assertEquals(INTERVAL.MAJOR_SECOND.intervalName,
                Intervals.intervalIdentification(new String[]{
                        NOTE.NOTE_C.noteName, NOTE.NOTE_D.noteName}));

        Assertions.assertEquals(INTERVAL.PERFECT_FIFTH.intervalName,
                Intervals.intervalIdentification(new String[]{
                        NOTE.NOTE_B.noteName, NOTE.NOTE_F_SHARP.noteName, ASC_NOTE_ORDER
                }));

        Assertions.assertEquals(INTERVAL.MINOR_SECOND.intervalName,
                Intervals.intervalIdentification(new String[]{
                        NOTE.NOTE_F_FLAT.noteName, NOTE.NOTE_G_DOUBLE_fLAT.noteName
                }));

        Assertions.assertEquals(INTERVAL.MAJOR_SEVENTH.intervalName,
                Intervals.intervalIdentification(new String[]{
                        NOTE.NOTE_G.noteName, NOTE.NOTE_F_SHARP.noteName, ASC_NOTE_ORDER
                }));
    }
}
