import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DescIntervalsConstructTest {


    private static final String DESC_NOTE_ORDER = "dsc";

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
    public void descIntervalConstructTest() {
        Assertions.assertEquals(NOTE.NOTE_A_DOUBLE_fLAT.noteName, Intervals.intervalConstruction(new String[]{INTERVAL.MAJOR_THIRD.intervalName, NOTE.NOTE_C_FLAT.noteName, DESC_NOTE_ORDER}));
        Assertions.assertEquals(NOTE.NOTE_A.noteName, Intervals.intervalConstruction(new String[]{INTERVAL.MINOR_SECOND.intervalName, NOTE.NOTE_B_FLAT.noteName, DESC_NOTE_ORDER}));
        Assertions.assertEquals(NOTE.NOTE_D_SHARP.noteName, Intervals.intervalConstruction(new String[]{INTERVAL.PERFECT_FOURTH.intervalName, NOTE.NOTE_G_SHARP.noteName, DESC_NOTE_ORDER}));
        Assertions.assertEquals(NOTE.NOTE_G_SHARP.noteName, Intervals.intervalConstruction(new String[]{INTERVAL.MINOR_THIRD.intervalName, NOTE.NOTE_B.noteName, DESC_NOTE_ORDER}));
        Assertions.assertEquals(NOTE.NOTE_B.noteName, Intervals.intervalConstruction(new String[]{INTERVAL.PERFECT_FOURTH.intervalName, NOTE.NOTE_E.noteName, DESC_NOTE_ORDER}));
        Assertions.assertEquals(NOTE.NOTE_D_SHARP.noteName, Intervals.intervalConstruction(new String[]{INTERVAL.MAJOR_SECOND.intervalName, NOTE.NOTE_E_SHARP.noteName, DESC_NOTE_ORDER}));
    }
}
