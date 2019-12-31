package basic;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class CsvDeleteFolders extends JFrame {
    private static final String fileName = "C:\\tmp\\2.csv";
    private static final String path = "C:\\tmp\\otchet.txt";

    public static void main(String[] args) throws Throwable {
        CsvDeleteFolders main = new CsvDeleteFolders();
//        main.deleteFile(fileName);
        main.TFFrame();
    }

    public void TFFrame() {
        TextField tf = new TextField("Выполнено");
        TextField tf2 = new TextField("Сюда 3 столбика C:\\tmp\\2.csv");
        TextField tf3 = new TextField("Тут отчет C:\\tmp\\otchet.txt");
        tf.setBounds(50, 50, 100, 25);
        tf2.setBounds(50, 75, 200, 25);
        tf3.setBounds(50, 100, 200, 25);
        add(tf);
        add(tf2);
        add(tf3);
        setSize(300, 300);
        setLayout(null);
        setVisible(true);
    }

    public boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    public void deleteFile(String fileName) throws Exception {
        try {
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "Cp1251"));
            String row;
            String UserPCrow;
            String PCprocess;
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write("");
            writer.close();
            while ((row = csvReader.readLine()) != null) {
                row = row.replace(':', '$');
                row = row.replace(';', '\\');
                String s4 = row.replaceAll("\\\\(.*)", "");
                String PC = s4;
                s4 = "\\" + "\\" + s4 + "\\c$";
                UserPCrow = "\\" + "\\" + row;
                PCprocess = row.replaceAll("(.*)\\\\", "");
                final File directory = new File(UserPCrow);
                final File pk = new File(s4);
                if (pk.exists()) {
                    if (directory.exists()) {
                        String psexec = "wmic /node: \"" + PC + "\" Path win32_process Where \"CommandLine Like '%" + PCprocess + "%'\" Call Terminate";
                        Runtime.getRuntime().exec("TAKEOWN /F " + directory);
                        String exec = "icacls " + directory + " /grant \"Domain Users\":F";
                        //kill proccess wmic /node: "c000006660" Path win32_process Where "CommandLine Like '%excel%'" Call Terminate
                        Runtime.getRuntime().exec(psexec);
                        deleteDirectory(directory);
                        if (directory.exists()) {
                            Runtime.getRuntime().exec(exec);
                            Thread.sleep(2000);
                            deleteDirectory(directory);
                            Thread.sleep(5000);

                            if (directory.exists()) {
                                String result = "Удалено";
                                writer = new BufferedWriter(new FileWriter(path, true));
                                writer.write(result + UserPCrow);
                                writer.newLine();
                                writer.close();

                                System.out.println(result + UserPCrow);
                            } else {
                                String noDel = "Не удаляется";
                                writer = new BufferedWriter(new FileWriter(path, true));
                                writer.write(noDel + UserPCrow);
                                writer.newLine();
                                writer.close();

                                System.out.println(noDel + UserPCrow);
                            }
                        } else {
                            String result = "Удалено";
                            writer = new BufferedWriter(new FileWriter(path, true));
                            writer.write(result + UserPCrow);
                            writer.newLine();
                            writer.close();

                            System.out.println(result + UserPCrow);
                        }
                    } else {
                        String result = "Папки(Файла) нет";
                        writer = new BufferedWriter(new FileWriter(path, true));
                        writer.write(result + UserPCrow);
                        writer.newLine();
                        writer.close();

                        System.out.println(result + UserPCrow);
                    }
                } else {
                    String result = "ПК не вкл";
                    writer = new BufferedWriter(new FileWriter(path, true));
                    writer.write(result + UserPCrow);
                    writer.newLine();
                    writer.close();

                    System.out.println(result + UserPCrow);
                }
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}