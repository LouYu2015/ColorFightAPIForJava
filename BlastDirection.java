public enum BlastDirection {
    SQUARE("square"),
    VERTICAL("vertical"),
    HORIZENTAL("horizental")
    ;

    private final String text;

    /**
     * @param text
     */
    BlastDirection(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}