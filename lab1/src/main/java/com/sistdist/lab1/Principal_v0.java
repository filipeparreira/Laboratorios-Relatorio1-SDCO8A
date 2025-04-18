/**
 * Laboratorio 1 - Lista 1 de Sistemas Distribuidos
 *
 * Autores: Filipe Augusto e Mabylly Kauany Ultima atualizacao: 18/04/2025
 */

package com.sistdist.lab1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Scanner;

public class Principal_v0 {
    // Inserir o caminho para a base de dados
    public final static Path path = Paths.get("/fortune-br.txt");
    private int NUM_FORTUNES = 0;

    public class FileReader {

        public int countFortunes() throws FileNotFoundException {

            int lineCount = 0;

            InputStream is = new BufferedInputStream(new FileInputStream(
                    path.toString()));
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    is))) {

                String line = "";
                while (!(line == null)) {

                    if (line.equals("%")) {
                        lineCount++;
                    }

                    line = br.readLine();

                }// fim while

                System.out.println(lineCount);
            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
            return lineCount;
        }

        public void parser(HashMap<Integer, String> hm)
                throws FileNotFoundException {

            InputStream is = new BufferedInputStream(new FileInputStream(
                    path.toString()));
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    is))) {

                int lineCount = 0;

                String line = "";
                while (!(line == null)) {

                    if (line.equals("%")) {
                        lineCount++;
                    }

                    line = br.readLine();
                    StringBuffer fortune = new StringBuffer();
                    while (!(line == null) && !line.equals("%")) {
                        fortune.append(line + "\n");
                        line = br.readLine();
                        // System.out.print(lineCount + ".");
                    }

                    hm.put(lineCount, fortune.toString());
                    System.out.println(fortune.toString());

                    System.out.println(lineCount);
                }// fim while

            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
        }

        public void read(HashMap<Integer, String> hm)
                throws FileNotFoundException {

            try (InputStream in = Files.newInputStream(path); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                String line = null;
                int lineCount = 0;
                StringBuilder fortune = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    if (line.equals("%")) {
                        hm.put(lineCount++, fortune.toString());
                        // limpa buffer
                        fortune.setLength(0);
                    } else {
                        fortune.append(line).append("\n");
                    }
                }
                // quando a msg não termina com %
                if (fortune.length() > 0) {
                    hm.put(lineCount++, fortune.toString());
                }
                if (!hm.isEmpty()) {
                    //gerar um valor aleatorio de chave
                    java.util.Random rand = new java.util.Random();
                    java.util.List<Integer> chaves = hm.keySet().stream().toList();
                    int indiceAleatorio = rand.nextInt(chaves.size());
                    int chaveSorteada = chaves.get(indiceAleatorio);

                    System.out.println("\nFortune sorteada: ");
                    System.out.println(hm.get(chaveSorteada));
                }
            } catch (IOException x) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
        }

        public void write(HashMap<Integer, String> hm) throws FileNotFoundException {
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                //Pegar mensagem do teclado
                Scanner scanner = new Scanner(System.in);

                System.out.println("\nEscreva a mensagem do arquivo: ");

                StringBuilder novaMensagem = new StringBuilder();
                String linha;

                while (!(linha = scanner.nextLine()).isEmpty()) {
                    novaMensagem.append(linha).append("\n");
                }

                writer.write("%\n");
                writer.write(novaMensagem.toString());
                writer.write("%\n");

            } catch (IOException x) {
                System.err.println("SHOW: Excecao na escritura do arquivo.");
            }
        }

    }

    public void iniciar() {

        FileReader fr = new FileReader();
        try {
            NUM_FORTUNES = fr.countFortunes();
            HashMap hm = new HashMap<Integer, String>();
            fr.parser(hm);
            fr.read(hm);
            fr.write(hm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Principal_v0().iniciar();
    }

}
