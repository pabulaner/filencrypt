package de.pabulaner.filencrypt;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.nio.file.Path;

public class Command {

    private final Option encrypt;

    private final Option decrypt;

    private final Option input;

    private final Option output;

    private final Option password;

    private final Options options;

    private final CommandLine cmd;

    public Command(String[] args) throws ParseException {
        encrypt = new Option("e", "encrypt", false, "Enables encryption");
        decrypt = new Option("d", "decrypt", false, "Enables decryption");
        input = new Option("i", "input", true, "Input file");
        output = new Option("o", "output", true, "Output file");
        password = new Option("p", "password", true, "Optional password");

        input.setRequired(true);
        output.setRequired(true);

        OptionGroup group = new OptionGroup();
        group.addOption(encrypt);
        group.addOption(decrypt);
        group.setRequired(true);

        CommandLineParser parser = new DefaultParser();

        options = new Options()
                .addOptionGroup(group)
                .addOption(input)
                .addOption(output)
                .addOption(password);

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter help = new HelpFormatter();
            help.printHelp("filencrypt", options);

            throw e;
        }
    }

    public Mode getMode() {
        if (cmd.hasOption(encrypt)) {
            return Mode.ENCRYPT;
        }

        if (cmd.hasOption(decrypt)) {
            return Mode.DECRYPT;
        }

        throw new IllegalStateException();
    }

    public Path getInput() {
        return Path.of(cmd.getOptionValue(input));
    }

    public Path getOutput() {
        return Path.of(cmd.getOptionValue(output));
    }

    public Options getOptions() {
        return options;
    }

    public String getPassword() {
        return cmd.getOptionValue(password);
    }
}
