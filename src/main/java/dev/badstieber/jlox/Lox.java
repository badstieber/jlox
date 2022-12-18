package dev.badstieber.jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Lox {
    static boolean hadError = false;
    static int errorCounter = 0;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError) {
            System.exit(65);
        }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        System.out.println("=== Lox interactive shell started ===");
        for (; ; ) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);

            // Handle errors:
            if (hadError) {
                System.err.println("Total errors: " + errorCounter);
                errorCounter = 0;
                hadError = false;
            }
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // For now, just print tokens.
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    static void error(int line, String message, char c) {
        report(line, "", message, c);
    }

    private static void report(int line, String where, String message, char c) {
        errorCounter++;
        if (c != ' ') {
            System.err.println(errorCounter + ". Error" + where + ": " + message + " --> " + c + " <-- in " + "Line " + line);
        } else {
            System.err.println(errorCounter + ". Error" + where + ": " + message + " in Line " + line);
        }
        hadError = true;
    }

}