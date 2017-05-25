package esialrobotik.ia.utils.lcd;

import com.google.inject.Inject;

/**
 * Created by franc on 25/05/2017.
 */
public class DummyLCD implements LCD {

    @Inject
    public DummyLCD() {}

    @Override
    public void println(String str) {

    }

    @Override
    public void clear() {

    }
}
