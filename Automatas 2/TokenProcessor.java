import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

class TokenProcessor {
    private static String archivoEntrada = "tabla.txt";
    private static String tablaSimbolos = "symbolTable.txt";
    private static String tablaDirecciones = "addressTable.txt";

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

    private static String obtenerTipo(String lexema) {
        String tipo = "";

        if (lexema.contains("&")) {
            tipo = "-51";
        } else if (lexema.contains("%")) {
            tipo = "-52";
        } else if (lexema.contains("#")) {
            tipo = "-53";
        } else if (lexema.contains("$")) {
            tipo = "-54";
        } else if (lexema.contains("@")) {
            tipo = "-55";
        }
        return tipo;
    }

    private static String obtenerValor(String token) {
        String valor = "";
        if (token.contains("&")) {
            valor = "0";
        } else if (token.contains("%")) {
            valor = "0.0";
        } else if (token.contains("#")) {
            valor = "true";
        } else if (token.contains("$")) {
            valor = "null";
        }
        return valor;
    }

    private static Map<String, Symbol> processSymbolTable(List<Token> tokens) {
        Map<String, Symbol> symbolTable = new HashMap<>();

        for (Token token : tokens) {

            if (token.getToken() == -51 ||
                    token.getToken() == -52 ||
                    token.getToken() == -53 ||
                    token.getToken() == -54) {
                String tipo = obtenerTipo(token.getLexema());
                String valor = obtenerValor(token.getLexema());
                String ambito = "Main";

                if (!symbolTable.containsKey(token.getLexema())) {
                    symbolTable.put(token.getLexema(), new Symbol(tipo, valor, ambito));
                }
            }
        }

        return symbolTable;
    }

    

    private static void writeSymbolTableToFile(Map<String, Symbol> symbolTable, String tablaSimbolos) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Symbol> entry : symbolTable.entrySet()) {
            Symbol symbol = entry.getValue();
            sb.append(entry.getKey())
                    .append(",").append(symbol.getTipo())
                    .append(",").append(symbol.getValor())
                    .append(",").append(symbol.getAmbito())
                    .append('\n');
        }

        try (java.io.FileWriter writer = new java.io.FileWriter(tablaSimbolos)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Symbol> processAddressTable(List<Token> tokens) {
        Map<String, Symbol> addressTable = new HashMap<>();

        for (Token token : tokens) {
            if (token.getToken() == -55) {
                String tipo = obtenerTipo(token.getLexema());
                int linea = token.getLine();
                String vci = "0";
                if (!addressTable.containsKey(token.getLexema())) {
                    addressTable.put(token.getLexema(), new Symbol(tipo, linea, vci));
                }

            }

        }

        return addressTable;
    }

    private static void writeAddressTableToFile(Map<String, Symbol> addressTable, String tablaDirecciones) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Symbol> entry : addressTable.entrySet()) {
            sb.append(entry.getKey())
                    .append(",").append(entry.getValue())
                    .append('\n');
        }

        try (java.io.FileWriter writer = new java.io.FileWriter(tablaDirecciones)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
    private static void reemplazarVariablesSimbolo(List<Token> tokens) {
        int indiceVariableSimbolo = 0;
        Map<String, Integer> variablesSimbolos = new HashMap<>(); // // Mapa para almacenar las variables y sus indices
        for (Token token : tokens) {
            if (token.getLexema().equals("inicio")) {
                indiceVariableSimbolo = 0;
            } else if (token.getToken() == -51 || token.getToken() == -52 || token.getToken() == -53 || token.getToken() == -54) {
                String lexema = token.getLexema();
                if (!variablesSimbolos.containsKey(lexema)) {
                    variablesSimbolos.put(lexema, indiceVariableSimbolo);
                    token.setPositionTable(indiceVariableSimbolo);
                    indiceVariableSimbolo++;
                } else {
                    token.setPositionTable(variablesSimbolos.get(lexema));
                }
            }
        }
    }
    private static void reemplazarVariablesDireccion(List<Token> tokens) {
        int indiceVariableDireccion = 0;
        Map<String, Integer> variablesDirecciones = new HashMap<>(); // Mapa para almacenar las variables y sus indices
    
        for (Token token : tokens) {
            if (token.getLexema().equals("inicio")) {
                indiceVariableDireccion = 0;
            } else if (token.getToken() == -55) {
                String lexema = token.getLexema();
                if (!variablesDirecciones.containsKey(lexema)) {
                    variablesDirecciones.put(lexema, indiceVariableDireccion);
                    token.setPositionTable(indiceVariableDireccion);
                    indiceVariableDireccion++;
                } else {
                    token.setPositionTable(variablesDirecciones.get(lexema));
                }
            }
        }
    }

    public static void printModifiedTokenTable(List<Token> tokens) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream("tabla.txt")) {
            for (Token token : tokens) {
                outputStream.write(token.toString().getBytes()); // Write bytes directly to avoid String limitations
                outputStream.write("\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkVariableDeclaration(List<Token> tokens) {
        Map<String, Integer> declaredVariables = new HashMap<>(); // Almacenar las variables declaradas en la sección de declaración
        boolean dentroDeDeclaracion = false;
    
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
    
            if (token.getToken()==-15) {
                dentroDeDeclaracion = true;
            } else if (token.getToken()==-2) {
                dentroDeDeclaracion = false;
            }
    
            if (dentroDeDeclaracion && (token.getToken() == -51 || token.getToken() == -52 || token.getToken() == -53 || token.getToken() == -54)) {
                String lexema = token.getLexema();
                if (!declaredVariables.containsKey(lexema)) {
                    declaredVariables.put(lexema, i);
                } else {
                    // Error: Variable duplicada en la sección de declaración
                    System.out.println("Error: Variable '" + lexema + "' duplicada (line " + token.getLine() + ")");
                    token.setError(true);
                }
            } 
        }
    
        // Verificar uso de variables fuera de la sección de declaración
        for (Token token : tokens) {
            if (!dentroDeDeclaracion && (token.getToken() == -51 || token.getToken() == -52 || token.getToken() == -53 || token.getToken() == -54)) {
                String lexema = token.getLexema();
                if (!declaredVariables.containsKey(lexema)) {
                    // Error: Variable no declarada usada fuera de la sección de declaración
                    System.out.println("Error: Variable '" + lexema + "' no esta declarada (linea " + token.getLine() + ")");
                    token.setError(true);
                }
            }
        }
        
    }
    

    public static void main(String[] args) throws IOException {
        List<Token> tokens = leerTokens(archivoEntrada);
        Map<String, Symbol> symbolTable = processSymbolTable(tokens);
        Map<String, Symbol> addressTable = processAddressTable(tokens);
        checkVariableDeclaration(tokens);
        writeSymbolTableToFile(symbolTable, tablaSimbolos);
        writeAddressTableToFile(addressTable, tablaDirecciones);
        reemplazarVariablesDireccion(tokens);
        reemplazarVariablesSimbolo(tokens);
        printModifiedTokenTable(tokens);
    }
}