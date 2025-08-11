package dev.sim0n.caesium;

import dev.sim0n.caesium.manager.MutatorManager;
import dev.sim0n.caesium.util.ByteUtil;
import dev.sim0n.caesium.util.wrapper.impl.ClassWrapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.util.Base64;

/**
 * A standalone class obfuscator that can obfuscate a single class bytecode
 */
public class ClassObfuscator {
    private final MutatorManager mutatorManager;

    public ClassObfuscator() {
        this.mutatorManager = new MutatorManager();
        // Enable all mutators by default
        mutatorManager.getMutators().forEach(mutator -> mutator.setEnabled(true));
    }

    /**
     * Obfuscates a single class bytecode
     *
     * @param classBytes The class bytecode to obfuscate
     * @return The obfuscated class bytecode
     * @throws Exception If an error occurs during obfuscation
     */
    public byte[] obfuscateClass(byte[] classBytes) throws Exception {
        // Parse the class bytes into a ClassNode
        ClassNode classNode = ByteUtil.parseClassBytes(classBytes);

        // Wrap the ClassNode
        ClassWrapper classWrapper = new ClassWrapper(classNode);

        // Apply mutations
        mutatorManager.handleMutation(classWrapper);

        // Convert back to bytes
        return ByteUtil.getClassBytes(classNode);
    }

    /**
     * Obfuscates a single class file
     *
     * @param inputFile  The input class file to obfuscate
     * @param outputFile The output obfuscated class file
     * @throws Exception If an error occurs during obfuscation
     */
    public void obfuscateClassFile(File inputFile, File outputFile) throws Exception {
        // Read the input file
        byte[] classBytes = readBytesFromFile(inputFile);

        // Obfuscate the class
        byte[] obfuscatedBytes = obfuscateClass(classBytes);

        // Write the obfuscated bytes to the output file
        writeBytesToFile(outputFile, obfuscatedBytes);
    }

    /**
     * Obfuscates a single class from a Base64 encoded string
     *
     * @param base64Class The Base64 encoded class bytecode
     * @return The Base64 encoded obfuscated class bytecode
     * @throws Exception If an error occurs during obfuscation
     */
    public String obfuscateClassBase64(String base64Class) throws Exception {
        // Decode the Base64 string
        byte[] classBytes = Base64.getDecoder().decode(base64Class);

        // Obfuscate the class
        byte[] obfuscatedBytes = obfuscateClass(classBytes);

        // Encode the obfuscated bytes as Base64
        return Base64.getEncoder().encodeToString(obfuscatedBytes);
    }

    /**
     * Reads all bytes from a file
     *
     * @param file The file to read
     * @return The file contents as a byte array
     * @throws IOException If an error occurs while reading the file
     */
    private byte[] readBytesFromFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    /**
     * Writes bytes to a file
     *
     * @param file  The file to write to
     * @param bytes The bytes to write
     * @throws IOException If an error occurs while writing the file
     */
    private void writeBytesToFile(File file, byte[] bytes) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
    }

    /**
     * Gets the mutator manager
     *
     * @return The mutator manager
     */
    public MutatorManager getMutatorManager() {
        return mutatorManager;
    }
}