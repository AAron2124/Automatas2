import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Ejercicio1 {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingresa un nombre para guardar ");
        String nombre = scanner.nextLine();
        scanner.close();

        File file = new File("ejercicio1.txt");
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println(nombre);
        printWriter.close();
        fileWriter.close();

        System.out.println("el nombre se ha guardado exitosamente");
    }
}