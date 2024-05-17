import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

public class Ejecucion {
    private static String VCI = "tabla.txt";
    private static String tablaSimboloss = "symbolTable.txt";
    private static String tablaDirecciones = "addressTable.txt";
    static Stack<Token> pilaControl = new Stack<>();
    static ArrayList<ObjSim> tablaSimbolos = new ArrayList<>();

    public static void main(String[] args) {
        List<Token> tokens = leerTokens(VCI);
        procesar(tokens);
    }

    private static List<Token> leerTokens(String fileName) {
        List<Token> tokens = new ArrayList<>();
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();

            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split("\\s+");
                    if (values.length == 4) {
                        try {
                            Token token = new Token(values[0], Integer.parseInt(values[1]),
                                    Integer.parseInt(values[2]),
                                    Integer.parseInt(values[3]));
                            tokens.add(token);
                        } catch (NumberFormatException e) {
                            System.err.println("Error: No se pudo convertir a entero en línea: " + line);
                        }
                    } else if (values.length > 4) {
                        Pattern pattern = Pattern.compile("\".*\"");
                        Matcher matcher = pattern.matcher(line);
                        matcher.find();
                        line = line.replace(matcher.group(0), "");
                        String[] lineaToken = line.trim().split("\\s+");
                        Token token = new Token(matcher.group(), Integer.parseInt(lineaToken[0]),
                                Integer.parseInt(lineaToken[1]), Integer.parseInt(lineaToken[2]));
                        tokens.add(token);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Operación cancelada por el usuario.");
        }

        return tokens;
    }

    public static void procesar(List<Token> tokens) {
        for (Token token : tokens) {
            if (token.getToken() == -21 || token.getToken() == -22 || token.getToken() == -23 || token.getToken() == -24
                    || token.getToken() == -25 || token.getToken() == -26) {
                switch (token.getToken()) {
                    case -21:
                        Token tem = pilaControl.pop();
                        if (tem.getToken() == -51 || tem.getToken() == -61) {
                            pilaControl.add(new Token("" + (Integer.parseInt(tem.getLexema())
                                    * Integer.parseInt(pilaControl.pop().getLexema())), 0, 0, 0));
                        } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                            pilaControl.add(new Token("" + (Double.parseDouble(tem.getLexema())
                                    * Double.parseDouble(pilaControl.pop().getLexema())), 0, 0, 0));
                        }
                        break;
                    case -22:
                        tem = pilaControl.pop();
                        Token temp1 = pilaControl.pop();
                        boolean esVariable = false;
                        Object valor = null;
                        if (tem.getToken() == -51 || tem.getToken() == -61) {
                            for (int i = 0; i < tablaSimbolos.size(); i++) {
                                if (temp1.getLexema().equals(tablaSimbolos.get(i).getNombre())) {
                                    esVariable = true;
                                    valor = tablaSimbolos.get(i).getValor();
                                }
                            }
                            if (esVariable) {
                                pilaControl.add(new Token(
                                        "" + (Integer.parseInt("" + valor) / Integer.parseInt(tem.getLexema())), -51, 0,
                                        0));
                            } else {
                                pilaControl.add(new Token("" + (Integer.parseInt(pilaControl.pop().getLexema())
                                        / Integer.parseInt(pilaControl.pop().getLexema())), 0, 0, 0));
                            }
                        } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                            pilaControl.add(new Token("" + (Double.parseDouble(pilaControl.pop().getLexema())
                                    / Double.parseDouble(pilaControl.pop().getLexema())), 0, 0, 0));
                        }
                        break;
                    case -23:
                        tem = pilaControl.pop();
                        if (tem.getToken() == -51 || tem.getToken() == -61) {
                            pilaControl.add(new Token("" + (Integer.parseInt(tem.getLexema())
                                    - Integer.parseInt(pilaControl.pop().getLexema())), 0, 0, 0));
                        } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                            pilaControl.add(new Token("" + (Double.parseDouble(pilaControl.pop().getLexema())
                                    - Double.parseDouble(pilaControl.pop().getLexema())), 0, 0, 0));
                        }
                        break;

                    case -24:
                        tem = pilaControl.pop();
                        if (tem.getToken() == -51 || tem.getToken() == -61) {
                            pilaControl.add(new Token("" + (Integer.parseInt(tem.getLexema())
                                    + Integer.parseInt(pilaControl.pop().getLexema())), 0, 0, 0));
                        } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                            pilaControl.add(new Token("" + (Double.parseDouble(pilaControl.pop().getLexema())
                                    + Double.parseDouble(pilaControl.pop().getLexema())), 0, 0, 0));
                        }
                        break;
                    case -26:
                        tem = pilaControl.pop();
                        Token guardar = pilaControl.pop();
                        for (int i = 0; i < tablaSimbolos.size(); i++) {
                            if (guardar.getLexema().equals(tablaSimbolos.get(i).getNombre())) {
                                tablaSimbolos.get(i).setValor(Integer.parseInt(tem.getLexema()));
                            }
                        }
                        break;
                }
                if (token.getToken() == -31 || token.getToken() == -32 || token.getToken() == -33
                        || token.getToken() == -34
                        || token.getToken() == -35 || token.getToken() == -36) {
                    switch (token.getToken()) {
                        case -31: // <
                            Token tem = pilaControl.pop();
                            if (tem.getToken() == -51 || tem.getToken() == -61) {
                                pilaControl
                                        .add(new Token("" + (Integer.parseInt(pilaControl.pop().getLexema()) < Integer
                                                .parseInt(tem.getLexema())), 0, 0, 0));
                            } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                                pilaControl
                                        .add(new Token("" + (Double.parseDouble(pilaControl.pop().getLexema()) < Double
                                                .parseDouble(tem.getLexema())), 0, 0, 0));
                            }
                            break;

                        case -32: // <=
                            tem = pilaControl.pop();
                            if (tem.getToken() == -51 || tem.getToken() == -61) {
                                pilaControl
                                        .add(new Token("" + (Integer.parseInt(pilaControl.pop().getLexema()) <= Integer
                                                .parseInt(tem.getLexema())), 0, 0, 0));
                            } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                                pilaControl
                                        .add(new Token("" + (Double.parseDouble(pilaControl.pop().getLexema()) <= Double
                                                .parseDouble(tem.getLexema())), 0, 0, 0));
                            }
                            break;

                        case -33: // >
                            tem = pilaControl.pop();
                            if (tem.getToken() == -51 || tem.getToken() == -61) {
                                pilaControl
                                        .add(new Token("" + (Integer.parseInt(pilaControl.pop().getLexema()) > Integer
                                                .parseInt(tem.getLexema())), 0, 0, 0));
                            } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                                pilaControl
                                        .add(new Token("" + (Double.parseDouble(pilaControl.pop().getLexema()) > Double
                                                .parseDouble(tem.getLexema())), 0, 0, 0));
                            }
                            break;

                        case -34: // >=
                            tem = pilaControl.pop();
                            if (tem.getToken() == -51 || tem.getToken() == -61) {
                                pilaControl
                                        .add(new Token("" + (Integer.parseInt(pilaControl.pop().getLexema()) >= Integer
                                                .parseInt(tem.getLexema())), 0, 0, 0));
                            } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                                pilaControl
                                        .add(new Token("" + (Double.parseDouble(pilaControl.pop().getLexema()) >= Double
                                                .parseDouble(tem.getLexema())), 0, 0, 0));
                            }
                            break;

                        case -35: // ==
                            tem = pilaControl.pop();
                            if (tem.getToken() == -51 || tem.getToken() == -61) {
                                pilaControl
                                        .add(new Token("" + (Integer.parseInt(pilaControl.pop().getLexema()) == Integer
                                                .parseInt(tem.getLexema())), 0, 0, 0));
                            } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                                pilaControl
                                        .add(new Token("" + (Double.parseDouble(pilaControl.pop().getLexema()) == Double
                                                .parseDouble(tem.getLexema())), 0, 0, 0));
                            }
                            break;

                        case -36: // !=
                            tem = pilaControl.pop();
                            if (tem.getToken() == -51 || tem.getToken() == -61) {
                                pilaControl
                                        .add(new Token("" + (Integer.parseInt(pilaControl.pop().getLexema()) != Integer
                                                .parseInt(tem.getLexema())), 0, 0, 0));
                            } else if (tem.getToken() == -52 || tem.getToken() == -62) {
                                pilaControl
                                        .add(new Token("" + (Double.parseDouble(pilaControl.pop().getLexema()) != Double
                                                .parseDouble(tem.getLexema())), 0, 0, 0));
                            }
                            break;

                    }
                }
                if (token.getToken() == -41 || token.getToken() == -42 || token.getToken() == -43) {
                    switch (token.getToken()) {
                        case -41: // &&
                            Token tem = pilaControl.pop();
                            boolean valorTemBoolAnd = Boolean.parseBoolean(tem.getLexema());
                            boolean valorTopBoolAnd = Boolean.parseBoolean(pilaControl.peek().getLexema());
                            boolean resultadoAnd = valorTopBoolAnd && valorTemBoolAnd;
                            pilaControl.push(new Token(resultadoAnd ? "true" : "false", 0, 0, 0));
                            break;

                        case -42: // ||
                            tem = pilaControl.pop();
                            boolean valorTemBoolOr = Boolean.parseBoolean(tem.getLexema());
                            boolean valorTopBoolOr = Boolean.parseBoolean(pilaControl.peek().getLexema());
                            boolean resultadoOr = valorTopBoolOr || valorTemBoolOr;
                            pilaControl.push(new Token(resultadoOr ? "true" : "false", 0, 0, 0));
                            break;

                        case -43: // !
                            boolean valorTopBoolNot = Boolean.parseBoolean(pilaControl.peek().getLexema());
                            boolean resultadoNot = !valorTopBoolNot;
                            pilaControl.push(new Token(resultadoNot ? "true" : "false", 0, 0, 0));
                            break;

                    }
                }
            }
        }
    }

}
