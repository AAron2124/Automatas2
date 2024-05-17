public class ObjSim {
    private String nombre;
    private int Token;
    private Object valor;

    public ObjSim(String nombre, int token, int valor) {
        this.nombre = nombre;
        this.Token = token;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public int getToken() {
        return Token;
    }

    public Object getValor() {
        return valor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setToken(int token) {
        Token = token;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Tokensimbolo{" +
                "nombre='" + nombre + '\'' +
                ", Token=" + Token +
                ", valor=" + valor +
                '}';
    }
}
