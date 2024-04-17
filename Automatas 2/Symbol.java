public class Symbol {
    private String tipo;
  private String valor;
  private String vci;
  private String ambito;
  private int linea;

  public Symbol(String tipo, String valor, String ambito) {
    this.tipo = tipo;
    this.valor = valor;
    this.ambito = ambito;
  }
  public Symbol(String tipo, int linea, String vci) {
    this.tipo = tipo;
    this.linea=linea;
    this.vci=vci;
  }

public String getTipo() {
    return tipo;
}

public void setTipo(String tipo) {
    this.tipo = tipo;
}

public String getValor() {
    return valor;
}

public void setValor(String valor) {
    this.valor = valor;
}

public String getAmbito() {
    return ambito;
}

public void setAmbito(String ambito) {
    this.ambito = ambito;
}
public String getVci() {
    return vci;
}
public void setVci(String vci) {
    this.vci = vci;
}
public int getLinea() {
    return linea;
}
public void setLinea(int linea) {
    this.linea = linea;
}

public String toString() {
    return String.format("%s,%s,%s", tipo,linea,vci);
}
}
