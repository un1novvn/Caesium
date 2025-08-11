package dev.sim0n.caesium;

import dev.sim0n.caesium.mutator.impl.ControlFlowMutator;
import dev.sim0n.caesium.mutator.impl.StringMutator;

import java.io.File;

/**
 * Example usage of the ClassObfuscator
 */
public class ClassObfuscatorExample {
    public static void main(String[] args) {
        try {
            // Create a new class obfuscator
            ClassObfuscator obfuscator = new ClassObfuscator();
            
            // Optionally configure specific mutators
            // For example, to enable only string mutation:
            obfuscator.getMutatorManager().getMutators().forEach(mutator -> mutator.setEnabled(false));
            StringMutator stringMutator = obfuscator.getMutatorManager().getMutator(StringMutator.class);
            ControlFlowMutator controlFlowMutator = obfuscator.getMutatorManager().getMutator(ControlFlowMutator.class);

            stringMutator.setEnabled(true);
            controlFlowMutator.setEnabled(true);

            // Obfuscate a class file
            File inputFile = new File("target/classes/Example.class");
            File outputFile = new File("Example.class");
            
            if (inputFile.exists()) {
                obfuscator.obfuscateClassFile(inputFile, outputFile);
                System.out.println("Class obfuscated successfully!");
            } else {
                System.out.println("Input file not found: " + inputFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}