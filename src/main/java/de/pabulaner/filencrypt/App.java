package de.pabulaner.filencrypt;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import java.nio.file.Files;
import java.nio.file.Path;

public class App {

    public static void main(String[] args) {
        Command command;

        try {
            command = new Command(args);
        } catch (ParseException e) {
            return;
        }

        if (command.getMode() == Mode.DECRYPT && command.getPassword() == null) {
            System.err.println("No password provided for decryption");
            return;
        }

        Secure secure = new Secure(command.getPassword());
        String input = exec(() -> Files.readString(command.getInput()), "Failed to read input file");

        if (input == null) {
            return;
        }

        String output = switch (command.getMode()) {
            case ENCRYPT -> exec(() -> secure.encrypt(input), "Failed to encrypt file");
            case DECRYPT -> exec(() -> secure.decrypt(input), "Failed to decrypt file");
        };

        if (output == null) {
            return;
        }

        Path result = exec(() -> Files.writeString(command.getOutput(), output), "Failed to write output file");

        if (result == null) {
            return;
        }

        if (command.getPassword() == null) {
            System.out.println("Password: " + secure.getPassword());
        }

        System.out.println("Execution successful");
    }

    private static <T> T exec(ExceptionSupplier<T> supplier, String error) {
        try {
            return supplier.get();
        } catch (Exception e) {
            System.err.println(error);
            return null;
        }
    }
}
