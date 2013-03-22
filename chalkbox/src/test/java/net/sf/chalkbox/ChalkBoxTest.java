package net.sf.chalkbox;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ChalkBoxTest {
    private ChalkBox chalks;

    @Before
    public void before() {
        this.chalks = new ChalkBox();
    }

    @Test
    public void black() {
        assertEquals("[30mblack[m", chalks().black().write("black"));
    }

    @Test
    public void red() {
        assertEquals("[31mred[m", chalks().red().write("red"));
    }

    @Test
    public void green() {
        assertEquals("[32mgreen[m", chalks().green().write("green"));
    }

    @Test
    public void yellow() {
        assertEquals("[33myellow[m", chalks().yellow().write("yellow"));
    }

    @Test
    public void blue() {
        assertEquals("[34mblue[m", chalks().blue().write("blue"));
    }

    @Test
    public void magenta() {
        assertEquals("[35mmagenta[m", chalks().magenta().write("magenta"));
    }

    @Test
    public void cyan() {
        assertEquals("[36mcyan[m", chalks().cyan().write("cyan"));
    }

    @Test
    public void white() {
        assertEquals("[37mwhite[m", chalks().white().write("white"));
    }

    private ChalkBox chalks() {
        return this.chalks;
    }
}
