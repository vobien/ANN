package vn.tribt.ai.image.process;

public class Character {

    private int top;
    private int bottom;
    private int left;
    private int right;

    public Character() {
    }

    public Character(int top, int bottom, int left, int right) {
        super();
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
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

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Character [top=" + top + ", bottom=" + bottom + ", left=" + left
                + ", right=" + right + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bottom;
        result = prime * result + left;
        result = prime * result + right;
        result = prime * result + top;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Character other = (Character) obj;
        if (bottom != other.bottom) {
            return false;
        }
        if (left != other.left) {
            return false;
        }
        if (right != other.right) {
            return false;
        }
        if (top != other.top) {
            return false;
        }
        return true;
    }

}
