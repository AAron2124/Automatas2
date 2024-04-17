public class Token {
    private String lexema;
    private int token;
    private int positionTable;
    private int line;
    private String value;

    public Token(String lexema, int token, int positionTable, int line) {
        this.lexema = lexema;
        this.token = token;
        this.positionTable = positionTable;
        this.line = line;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getPositionTable() {
        return positionTable;
    }

    public void setPositionTable(int positionTable) {
        this.positionTable = positionTable;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getIndiceTabla() {
        return positionTable;
    }

    @Override
    public String toString() {
        return lexema + ", " + token + ", " + positionTable + ", " + line;
    }

    public void setError(boolean b) {
        // Método para establecer errores, si es necesario
    }

    public Token(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
