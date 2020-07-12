package Paint;

/**
 * This interface is used to pass a button
 *
 * @author jonb1
 */
public interface OnTick {

    /**
     * This is function is run on the clock cycles.
     *
     * @param timeSeconds the current time gets passed through every event
     */
    void OnEvent(int timeSeconds);
}
