package vn.tribt.ai.image.process;

import java.util.ArrayList;
import java.util.List;

public class LineCharacter {

    private int top;
    private int bottom;

    private List<Character> characters;

    public LineCharacter() {
        characters = new ArrayList<>();
    }

    public LineCharacter(int top, int bottom) {
        this.top = top;
        this.bottom = bottom;
        characters = new ArrayList<>();
    }

    public void addCharacter(Character c) {
        characters.add(c);
    }

    public void removeCharacter(Character c) {
        characters.remove(c);
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        return "LineCharacter [top=" + top + ", bottom=" + bottom
                + ", characters=" + characters + "]";
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

}
