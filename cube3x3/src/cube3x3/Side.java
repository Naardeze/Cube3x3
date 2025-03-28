package cube3x3;

public enum Side implements Index {
    MIN(0), PLUS(2);
    
    final private int value;
    
    Side(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

}
