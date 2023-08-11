package fefe;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String year = dateFormat.format(date);
        Date datum = new Date();
        SimpleDateFormat datumFormat = new SimpleDateFormat("MM");
        String month = datumFormat.format(datum);
        URL fefe = new URL("https://blog.fefe.de/?mon=" + year + month);
        BufferedReader in = new BufferedReader(new InputStreamReader(fefe.openStream(), "UTF-8"));

        List<String> posts = new ArrayList<>(); // Liste zum Speichern der Beiträge

        String inputLine;
        Pattern htmlTagPattern = Pattern.compile("<[^>]*>");
        int emptyLineCount = 0; // Zählvariable für leere Zeilen

        while ((inputLine = in.readLine()) != null) {
            String textWithoutTags = removeHtmlTags(inputLine, htmlTagPattern);
            if (textWithoutTags.startsWith("Wer schöne Verschwörungslinks für mich hat: ab an felix-bloginput (at) fefe.de!") ||
                textWithoutTags.startsWith("Fragen?  Antworten!  Siehe auch: Alternativlos") ||
                textWithoutTags.startsWith("Fefes Blog")) {
                textWithoutTags = ""; // Setze leere Zeichenkette für unerwünschte Zeilen
            }
            if (textWithoutTags.startsWith("[l]")) {
                emptyLineCount++; // Erhöhe die Zählvariable für leere Zeilen
                posts.add(""); // Füge leere Zeichenkette als Trenner hinzu
            }
            posts.add(textWithoutTags);
        }

        in.close();

        Collections.reverse(posts); // Kehre die Reihenfolge der Beiträge um

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fefe.txt"), "UTF-8"));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("fefe.txt"), "UTF-8"));

        for (String post : posts) {
            if (!post.isEmpty()) {
                System.out.println(post);
                out.write(post);
                out.newLine();
                pw.println(post);
            } else if (emptyLineCount > 0) {
                out.newLine(); // Leere Zeile als Trenner
                pw.println();
                emptyLineCount--; // Verringere die Zählvariable für leere Zeilen
            }
        }

        out.close();
        pw.close();

        Scanner sc = new Scanner(new File("fefe.txt"), "UTF-8");
        while (sc.hasNextLine()) {
            System.out.println(sc.nextLine());
        }
        sc.close();

        quitProgram();
    }

    public static String removeHtmlTags(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }

    public static void quitProgram() {
        System.out.println("Drücke 'Q' und Enter, um das Programm zu beenden.");
        Scanner userInputScanner = new Scanner(System.in);
        String userInput = userInputScanner.nextLine();
        if ("q".equalsIgnoreCase(userInput)) {
            System.exit(0);
        } else if ("Q".equalsIgnoreCase(userInput)) {
            System.exit(0);
        }
    }
}
