import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

public class prueba {
    private static String archivoEntrada = "tabla.txt";
    private static String imprimirVci = "vci.txt";
    private static Cola vci;
    private static Cola posicion;

    public static void main(String[] args) throws Exception {
        List<Token> tokens = leerTokens(archivoEntrada);
        vci(tokens);
        writeVCIToFile(vci, posicion, imprimirVci);
    }

    private static List<Token> leerTokens(String fileName) {
        List<Token> tokens = new ArrayList<>();
        boolean encontradoInicio = false; // Bandera para indicar si se encontró el lexema "inicio"

        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();

            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Procesar tokens solo si se encontró el lexema "inicio"
                    if (encontradoInicio) {
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
                    } else if (line.contains("inicio")) {
                        // Si se encuentra el lexema "inicio" por primera vez, establecer la bandera en
                        // true
                        encontradoInicio = true;
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

    private static boolean debeAgregarAlVCI(Token token) {
        return !token.getLexema().equals("inicio");
    }

    public static boolean esOperador(int numeroToken) {
        return (numeroToken == -9||numeroToken == -10||
        		numeroToken == -17||numeroToken == -8||numeroToken == -3 || numeroToken == -43 
        		|| numeroToken == -42 || numeroToken == -41 || numeroToken == -31
                || numeroToken == -32 || numeroToken == -33 || numeroToken == -34 || numeroToken == -36
                || numeroToken == -35 || numeroToken == -26 || numeroToken == -24 || numeroToken == -25
                || numeroToken == -21 || numeroToken == -22 || numeroToken == -75 || numeroToken == -73
                || numeroToken == -74);
    }

    private static boolean evaluarCondicion() {

        return true;
    }

    public static void vci(List<Token> tokens) {
        Pila stack = new Pila();
        Pila fals = new Pila();
        vci = new Cola();
        posicion = new Cola();
        Pila condicionales = new Pila();
        Pila dir = new Pila();
        Pila est = new Pila();
        int i = 0;
        System.out.println("Cantidad de tokens encontrados: " + tokens.size());
        System.out.println("Tokens encontrados: " + tokens);

        for (Token token : tokens) {
            if (debeAgregarAlVCI(token)) {
                int numeroToken = token.getToken();
                if (!esOperador(numeroToken)) {
                    vci.enqueue(token);
                    i++; // Incrementa el índice solo cuando se agrega un nuevo token a la cola vci
                    posicion.enqueue(i);
                }
                
                if (numeroToken == -8) { // Token "While"
    System.out.println("Token 'While' encontrado.");
    est.push("mientras");
    dir.push(i); // Almacena la dirección siguiente al While
}


if (numeroToken == -17) { // Token "Do"
    System.out.println("Encontrado token 'Do'.");
    while (!stack.isEmpty()) {
        vci.enqueue(stack.top());
        stack.pop();
        i++;
        posicion.enqueue(i);
    }
    vci.enqueue(""); // Casilla vacía
    int direccionCasillaVacia = i; // Guarda la dirección de la casilla vacía
    i++;
    posicion.enqueue(i);
    dir.push(direccionCasillaVacia); // Almacena la dirección de la casilla vacía
    vci.enqueue("hacer"); // Genera token Do
    i++;
    posicion.enqueue(i);
}

if (numeroToken == -3 && !est.isEmpty() && est.top().equals("mientras")) { // Token "End"
    System.out.println("Encontrado token 'End' para 'While'.");
    est.pop(); // Saca el estatuto de la pila para saber a quién corresponde el End
    int direccionDo = (int) dir.pop(); // Saca la dirección del Do

    // Guarda la dirección actual + 2 en la posición que tenía el directorio
    int nuevaDireccion = i + 2;
    vci.enqueue(Integer.toString(direccionDo), nuevaDireccion);
    i++;
    posicion.enqueue(i);

    // Pop del directorio y guarda la dirección en VCI
    vci.enqueue(Integer.toString((int) dir.pop()));
    i++;
    posicion.enqueue(i);

    vci.enqueue("Fin-mientras"); // Genera token END-WHILE
    i++;
    posicion.enqueue(i);
    fals.push(0); // Indicar que se procesó correctamente la condición
}


                // Pop "Repetir" de la pila de estatutos y guardar dirección en VCI cuando se encuentra "End"
                if (numeroToken == -3) { // Token "End"
                    if (!est.isEmpty() && est.top().equals("repetir")) {
                        est.pop(); // Saca el estatuto de la pila
                        int direccionPop = (int) dir.pop(); // Saca la dirección del "Repetir"
                        vci.enqueue(Integer.toString(direccionPop)); // Guarda la dirección en VCI
                        i++;
                        posicion.enqueue(i);
                    }
                }
                // or
                if (token.getToken() == -42) {
                    if ((int) fals.top() == 10) {
                        do {
                            vci.enqueue(stack.top());
                            i++;
                            posicion.enqueue(i);
                            stack.pop();
                            fals.pop();
                        } while ((int) fals.top() != 0);
                    }
                    if ((int) fals.top() == 0) {
                        stack.push(token);
                        fals.push(10);
                    }
                }
                // and
                if (token.getToken() == -41) {
                    if ((int) fals.top() == 20) {
                        do {
                            vci.enqueue(stack.top());
                            i++;
                            posicion.enqueue(i);
                            stack.pop();
                            fals.pop();
                        } while ((int) fals.top() != 0 && (int) fals.top() > 10);
                    }
                    if ((int) fals.top() == 0 || (int) fals.top() <= 10) {
                        stack.push(token);
                        fals.push(20);
                    }
                }
                // not
                if (token.getToken() == -43) {
                    if ((int) fals.top() == 30) {
                        do {
                            vci.enqueue(stack.top());
                            i++;
                            posicion.enqueue(i);
                            stack.pop();
                            fals.pop();
                        } while ((int) fals.top() != 0 && (int) fals.top() > 20);
                    }
                    if ((int) fals.top() == 0 || (int) fals.top() <= 20) {
                        stack.push(token);
                        fals.push(30);
                    }
                }
                // =
                if (numeroToken == -26) {
                    if (stack.isEmpty()) {
                        stack.push(token);
                        fals.push(0);
                    }
                }
                // +||-
                if (numeroToken == -24 || numeroToken == -25) {
                    if ((int) fals.top() == 50) {
                        do {
                            vci.enqueue(stack.top());
                            i++;
                            posicion.enqueue(i);
                            stack.pop();
                            fals.pop();
                        } while ((int) fals.top() != 0 || (int) fals.top() > 40);
                    }
                    if ((int) fals.top() == 0 || (int) fals.top() <= 40) {
                        stack.push(token);
                        fals.push(50);
                    }
                }
                // * || / || %
                if (numeroToken == -21 || numeroToken == -22 || numeroToken == -23) {
                    if ((int) fals.top() == 60) {
                        do {
                            vci.enqueue(stack.top());
                            i++;
                            posicion.enqueue(i);
                            stack.pop();
                            fals.pop();
                        } while ((int) fals.top() != 0 && (int) fals.top() == 60);
                    }
                    if ((int) fals.top() == 0 || (int) fals.top() <= 50) {
                        stack.push(token);
                        fals.push(60);
                    }
                }
                // ;
                if (numeroToken == -75) {
                    while (!stack.isEmpty()) {
                        vci.enqueue(stack.top());
                        stack.pop();
                        fals.pop();
                        i++;
                        posicion.enqueue(i);
                    }
                }
                // (
                if (numeroToken == -73) {
                    stack.push(token);
                    fals.push(0);
                }
                // )
                if (numeroToken == -74) {
                    while ((int) fals.top() != 0) {
                        vci.enqueue(stack.top());
                        fals.pop();
                        stack.pop();
                        i++;
                        posicion.enqueue(i);
                    }
                    stack.pop();
                    fals.pop();
                    i++;
                    posicion.enqueue(i);
                }

                if (numeroToken == -31 || numeroToken == -32 || numeroToken == -33 || numeroToken == -34
                        || numeroToken == -36 || numeroToken == -35) {
                    if ((int) fals.top() == 40) {
                        do {
                            vci.enqueue(stack.top());
                            i++;
                            posicion.enqueue(i);
                            stack.pop();
                            fals.pop();
                        } while ((int) fals.top() != 0 || (int) fals.top() > 40);
                    }
                    if ((int) fals.top() == 0 || (int) fals.top() <= 30) {
                        stack.push(token);
                        fals.push(40);
                    }
                }


                if (numeroToken == -6 || numeroToken == -7) {
                    fals.push(token.getLine());
                    condicionales.push(token.getLexema());
                }

                if (numeroToken == -6 || numeroToken == -7) {
                    continue;
                }

                
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                stack.Mostrar();
                fals.Mostrar();
                System.out.println("VCI " + vci.toString() + "\n" + posicion.toString());
            }
        }
    }

    public static void writeVCIToFile(Cola vci, Cola posicion, String imprimirVci) {
        StringBuilder sb = new StringBuilder();

        while (!vci.isEmpty()) {
            sb.append(vci.dequeue()).append('\n');
        }

        try (FileWriter writer = new FileWriter(imprimirVci)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}