
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    
    static final int LDBA = 0xD0;      
    static final int STBA = 0xF1;      
    static final int LDA = 0xC0;       
    static final int ADDA = 0x60;     
    static final int STOP = 0x00;     

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Java statement: ");
        String input = sc.nextLine();

       
        if (input.contains("+") && !input.contains("\"")) {
            handleAddition(input);
        }
        
        else if (input.contains("println") && input.contains("\"")) {
            handlePrintln(input);
        }
        else {
            System.out.println("Error: Unsupported statement type");
        }
    }
    
    static void handlePrintln(String input) {
        int start = input.indexOf('"') + 1;
        int end = input.lastIndexOf('"');

        if (start <= 0 || end < start) {
            System.out.println("Error: Invalid string format");
            return;
        }

        String message = input.substring(start, end);

        System.out.println("\nAssembly Code:");
        StringBuilder hex = new StringBuilder();

        for (char c : message.toCharArray()) {
            int ascii = (int) c;
            System.out.printf("LDBA 0x%04X, i%n", ascii);
            System.out.println("STBA 0xFC16, d");
            hex.append(String.format("%02X 00 %02X %02X FC 16 ", LDBA, ascii, STBA));
        }

        System.out.println("STOP");
        System.out.println(".END");

        System.out.println("\nHexadecimal Machine Code:");
        hex.append(String.format("%02X", STOP));
        System.out.println(hex.toString().trim().replaceAll(" +", " "));
    }

   
    static void handleAddition(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.find()) {
            System.out.println("Error: First number not found");
            return;
        }
        int num1 = Integer.parseInt(matcher.group());

        if (!matcher.find()) {
            System.out.println("Error: Second number not found");
            return;
        }
        int num2 = Integer.parseInt(matcher.group());

        System.out.println("\nAssembly Code:");
        System.out.printf("LDA 0x%04X, i%n", num1);
        System.out.printf("ADDA 0x%04X, i%n", num2);
        System.out.println("STOP");
        System.out.println(".END");

        System.out.println("\nHexadecimal Machine Code:");
        System.out.printf("%02X 00 %02X %02X 00 %02X %02X%n",
                LDA, num1, ADDA, num2, STOP);
    }
}