package vn.tribt.ai.ann.libs;

public class BoundNumber {

    public static final double ABS_TOO_SMALL = 1.0E-10;

    public BoundNumber() {
    }

    public static double adjust(double d) {
        if (Math.abs(d) > ABS_TOO_SMALL) {
            return d;
        }

        if (d > 0) {
            return ABS_TOO_SMALL;
        }

        if (d < 0) {
            return (-1) * ABS_TOO_SMALL;
        }

        return d;
    }

}
