/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Swing;

import static Swing.Erreur.erreur;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author el-barae
 */
public class Ventes extends javax.swing.JFrame {
    String[] pro; 
    int nbrecu=0;
      /**
     * Creates new form test
     */
    public Ventes() {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Lohit Devanagari",Font.BOLD,20));
        setLocation(400, 160);
        tableModel.addColumn("Non produit");
        tableModel.addColumn("Quantité(stock)");
        tableModel.addColumn("Prix");
        jTable1.setModel(tableModel);
    }
    private void writeDataToFile() throws IOException, PrinterException {
        // Récupérez les données de la JTable
        int rowCount = tableModel.getRowCount();
        ProcessBuilder processBuilder = new ProcessBuilder("pwd");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
        // Définissez le nom du fichier où vous souhaitez enregistrer les données
        String fileName = "/home/el-barae/NetBeansProjects/GestionLibrary/src/Swing/ticket.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(" \n Olas Multiservices");
            writer.write("  \n\n Av Dakhla NR 14 ");
            writer.write("  \n      Tetouan");
            writer.write("  \nTel1: 0671034038");
            writer.write("  \n Tel2: 0763652727");
            writer.write("\n"+formattedDate+"\n");
            //writer.write("Recu:"+nbrecu+"\n");
            Object[][] arr = new Object[rowCount][3]; 
            writer.write("  \n\n Nom produit       ");
            writer.write("  Prix\n\n");
            int a =0; 
            // Parcourez la JTable et écrivez les données dans le fichier
            for (int row = 0; row < rowCount; row++) {
                    Object value1 = tableModel.getValueAt(row, 0);
                    for (int row1 = 0; row1 < rowCount; row1++) {
                        Object valcol=tableModel.getValueAt(row1, 0);
                        if(valcol != null && valcol.equals(value1)){
                            a++;
                        }
                    }
                    Float value2 = (Float) tableModel.getValueAt(row, 2);
                    Float prices=0.0f;
                    for (int row3 = 0; row3 < jTable1.getRowCount(); row3++) {
                        String name = (String) jTable1.getValueAt(row3, 0);
                        Float price = (Float) jTable1.getValueAt(row3, 2);

                    // Si le nom correspond au nom recherché, ajoutez le prix au total
                        if (name.equals(value1)) {
                        prices += price;
                        }
                    }
                    arr[row][0]=value1;
                    arr[row][1]=a;
                    arr[row][2]=prices;
                    a=0;
            }
            Object[][] narr = removeDuplicateRows(arr);
            for (int r = 0; r < narr.length; r++) {
                String mot = (narr[r][1]+"* "+narr[r][0]).toString();
                int longueurSouhaitee = 20;
                String resultat = String.format("%-" + longueurSouhaitee + "s", mot);
                writer.write(resultat);
                writer.write("   ");
                String prix = narr[r][2].toString();
                writer.write(prix);
                // Utilisez une tabulation pour séparer les colonnes
                writer.write("\n"); // Saut de ligne entre les lignes
            }
            writer.write("\n    ***************************\n\n");
            String total=jLabel6.getText();
            writer.write("  Total : "+total+" DH\n");
            writer.write("\n  Merci pour votre visite\n\n\n    ***************************");
            writer.close();
        }
        // Imprimez le fichier ou effectuez d'autres actions d'impression ici
        System.out.println("Les données ont été enregistrées dans le fichier : " + fileName+"\n");
        
    }
    private void imprimer() throws IOException{
            ProcessBuilder processBuilder = new ProcessBuilder("pwd");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
             String filePath = "/home/el-barae/NetBeansProjects/GestionLibrary/src/Swing/ticket.txt";

        try {
            // Créez un objet PrinterJob pour gérer l'impression
            PrinterJob printerJob = PrinterJob.getPrinterJob();

            // Définissez un Printable qui lira le fichier texte et l'imprimera
            Printable printable = new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    // Vérifiez si la page demandée n'existe pas
                    if (pageIndex > 0) {
                        return Printable.NO_SUCH_PAGE;
                    }

                    try {
                        // Créez un BufferedReader pour lire le fichier texte
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
                        Graphics2D g2d = (Graphics2D) graphics;
                        Font font = new Font("Arial", Font.PLAIN, 13);
                        g2d.setFont(font);

                        int x = (int) pageFormat.getImageableX();
                        int y = (int) pageFormat.getImageableY();
                        int lineHeight = g2d.getFontMetrics().getHeight();

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            g2d.drawString(line, x, y);
                            y += lineHeight;
                        }

                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return Printable.PAGE_EXISTS;
                }
            };

            // Définissez l'imprimable pour l'objet PrinterJob
            printerJob.setPrintable(printable);
            if (printerJob.printDialog()) {
                // Lancez l'impression
                printerJob.print();
            }
        } catch (PrinterException e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Erreur.erreur = sw.toString();
            JOptionPane.showMessageDialog(this, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
            Double Total = 0.0;
            String formattedDate= "";

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jDialog3 = new javax.swing.JDialog();
        jDialog4 = new javax.swing.JDialog();
        jDialog5 = new javax.swing.JDialog();
        jDialog6 = new javax.swing.JDialog();
        jDialog7 = new javax.swing.JDialog();
        jDialog8 = new javax.swing.JDialog();
        jDialog9 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog5Layout = new javax.swing.GroupLayout(jDialog5.getContentPane());
        jDialog5.getContentPane().setLayout(jDialog5Layout);
        jDialog5Layout.setHorizontalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog5Layout.setVerticalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog6Layout = new javax.swing.GroupLayout(jDialog6.getContentPane());
        jDialog6.getContentPane().setLayout(jDialog6Layout);
        jDialog6Layout.setHorizontalGroup(
            jDialog6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog6Layout.setVerticalGroup(
            jDialog6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog7Layout = new javax.swing.GroupLayout(jDialog7.getContentPane());
        jDialog7.getContentPane().setLayout(jDialog7Layout);
        jDialog7Layout.setHorizontalGroup(
            jDialog7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog7Layout.setVerticalGroup(
            jDialog7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog8Layout = new javax.swing.GroupLayout(jDialog8.getContentPane());
        jDialog8.getContentPane().setLayout(jDialog8Layout);
        jDialog8Layout.setHorizontalGroup(
            jDialog8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog8Layout.setVerticalGroup(
            jDialog8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog9Layout = new javax.swing.GroupLayout(jDialog9.getContentPane());
        jDialog9.getContentPane().setLayout(jDialog9Layout);
        jDialog9Layout.setHorizontalGroup(
            jDialog9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog9Layout.setVerticalGroup(
            jDialog9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 102, 0));

        jLabel1.setFont(new java.awt.Font("Lohit Devanagari", 1, 24)); // NOI18N
        jLabel1.setText("Codebarre");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Lohit Devanagari", 0, 24)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Produit", "Prix", "Quantite"
            }
        ));
        jTable1.setRowHeight(30);
        jScrollPane1.setViewportView(jTable1);

        jButton5.setBackground(new java.awt.Color(255, 255, 0));
        jButton5.setFont(new java.awt.Font("Lohit Devanagari", 1, 24)); // NOI18N
        jButton5.setText("Imprimer");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(102, 255, 0));
        jButton6.setFont(new java.awt.Font("Lohit Devanagari", 1, 24)); // NOI18N
        jButton6.setText("Retour");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Lohit Devanagari", 1, 36)); // NOI18N
        jLabel5.setText("Total :");

        jLabel6.setFont(new java.awt.Font("Lohit Devanagari", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 255, 0));
        jLabel6.setText("00.00");

        jLabel2.setFont(new java.awt.Font("Lohit Devanagari", 1, 24)); // NOI18N
        jLabel2.setText("Pagne de ventes");

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("X");
        jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 0, 0));
        jButton3.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("-");
        jButton3.setMargin(new java.awt.Insets(1, 1, 1, 1));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jButton1.setBackground(new java.awt.Color(255, 255, 0));
        jButton1.setFont(new java.awt.Font("Lohit Devanagari", 1, 24)); // NOI18N
        jButton1.setText("Supprimer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabel3.setText("Prix :");

        jLabel4.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 255, 204));
        jLabel4.setText("00.00");

        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(20, 20, 20)))
                        .addGap(25, 25, 25))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jButton6))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(54, 54, 54)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(46, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(303, 303, 303))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(90, 90, 90)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        String codebarre = jTextField1.getText();
        int nbr = Integer.valueOf(jTextField3.getText());
        String categorie ="";
        String nomproduit ="";
        Float prix = 3.14f;
        Float prix_initial = 0.0f;
        boolean modifier=false;
        boolean ok=true;
        boolean vide=true;
        int quantities=0;
       
       
        int att=0;
         
        try {
          for(int i=0;i<nbr;i++){
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/stock" , "root" , "1234abcD.");
            String query6="SELECT nom FROM stock_product WHERE codebarre = ?";
            PreparedStatement statement2 = conn.prepareStatement(query6);
            statement2.setString(1,codebarre);
            ResultSet res5 = statement2.executeQuery();
             while (res5.next()) {
                 nomproduit = res5.getString("nom");
             }
            String query5="SELECT SUM(quantity) AS quantities FROM stock_product WHERE nom = ?";
            PreparedStatement statement1 = conn.prepareStatement(query5);
            statement1.setString(1,nomproduit);
            ResultSet res4 = statement1.executeQuery();
             while (res4.next()) {
                 quantities = res4.getInt("quantities")-1;
             }
            String query = "SELECT * FROM stock_product WHERE codebarre = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,codebarre);
            if (codebarre !=""){
            ResultSet res = statement.executeQuery();
             while (res.next()) {
                    Float Prix=res.getFloat("prix");
                    String nom = res.getString("nom");
                    int quantity = res.getInt("quantity");
                    
                    
                       Object[] rowData = {
                            nom,
                            quantities,
                            Prix
                        };  
                    
                   
                    if(quantity == 1){
                        modifier=true;
                        ok=false;
                    }
                    else if(quantity<=0){
                        ok=false;
                        break;
                     }else{
                        ok=true;
                    }
                    tableModel.addRow(rowData);
                    categorie = res.getString("type");
                    prix = Prix;
                    att= res.getInt("en_attend");
                    prix_initial = res.getFloat("prix_ini");
                    Total = Total+Prix;
                    jLabel6.setText(Double.toString(Total));
                    vide=false;
            }
            if(modifier==true){
                Float P=0.0f;
                int q=0;
                Float pi=0.0f;
                String query1 = "SELECT prix, quantity, prix_ini FROM stock_product WHERE nom = ? AND codebarre = '1'";
                PreparedStatement smt = conn.prepareStatement(query1);
                smt.setString(1,nomproduit);
                ResultSet res1 = smt.executeQuery();
                if (res1.next()) {
                    P=res1.getFloat("prix");
                    q = res1.getInt("quantity");
                    pi = res1.getFloat("prix_ini");
                String query2 = "UPDATE stock_product SET quantity = ?, prix = ?, prix_ini = ?, en_attend = en_attend - 1 WHERE codebarre = ?";
                PreparedStatement stm = conn.prepareStatement(query2);
                stm.setInt(1,q);
                stm.setFloat(2,P);
                stm.setFloat(3,pi);
                stm.setString(4,codebarre);
                stm.executeUpdate();
                String query3 = "DELETE FROM stock_product WHERE nom = ? AND codebarre = '1'";
                PreparedStatement stm1 = conn.prepareStatement(query3);
                stm1.setString(1,nomproduit);
                stm1.executeUpdate();
                if(att>1){
                    for(int j=2;j<=att;j++){
                    String query4 = "UPDATE stock_product SET codebarre = codebarre - 1 WHERE codebarre = ?";
                    PreparedStatement stm2 = conn.prepareStatement(query4);
                    stm2.setString(1,String.valueOf(j));
                    stm2.executeUpdate();
                    }
                }
                }else{
                    ok = true;
                }
                modifier = false;
            }
            if(ok==true){
                
                String query2 = "UPDATE stock_product SET quantity = quantity - 1 WHERE codebarre = ?";
                PreparedStatement stm = conn.prepareStatement(query2);
                stm.setString(1,codebarre);
                stm.executeUpdate();
            }
            if(vide==false){
            Float earnings = prix-prix_initial;
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedDate = currentDate.format(dateFormatter);
            String query8 = "SELECT nbr_recu FROM vente WHERE date = (SELECT max_date FROM (SELECT MAX(date) AS max_date FROM vente) AS subquery)";
                PreparedStatement stm = conn.prepareStatement(query8);
                ResultSet res9 = stm.executeQuery();
                
                while (res9.next()){
                    nbrecu = res9.getInt("nbr_recu");
                }
            String query1 = "INSERT INTO vente(codebarre,type,nom,prix,earnings,date,nbr_recu) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement smt = conn.prepareStatement(query1);
            smt.setString(1,codebarre);
            smt.setString(2, categorie);
            smt.setString(3, nomproduit);
            smt.setFloat(4, prix);
            smt.setFloat(5, earnings);
            smt.setString(6, formattedDate);
            smt.setInt(7, nbrecu);
            smt.executeUpdate();
             System.out.println("la table est a remplir ...");
            }
            }
            conn.close();
            jTextField1.setText("");
           jTextField3.setText("1");
          }  
        } catch (Exception ex) {
            
            System.out.println("erreur: "+ex);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            Erreur.erreur = sw.toString();
            JOptionPane.showMessageDialog(this, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
        
        }
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Acceuil a = new Acceuil();
        a.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
                LocalDateTime currentDate = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                formattedDate = currentDate.format(dateTimeFormatter);
                writeDataToFile();
                imprimer();
                }catch (IOException ex) {
                    ex.printStackTrace();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    Erreur.erreur = sw.toString();
                    JOptionPane.showMessageDialog(this, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (PrinterException ex) {
            Logger.getLogger(Ventes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int[] selectedRows = jTable1.getSelectedRows();
        String nomProduit;
        Float Prix = 0.0f;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/stock" , "root" , "1234abcD.");
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    nomProduit = (String) jTable1.getValueAt(selectedRows[i], 0); 
                    tableModel.removeRow(selectedRows[i]);
                    String query1 = "DELETE FROM vente WHERE date = (SELECT max_date FROM (SELECT MAX(date) AS max_date FROM vente) AS subquery) AND nom = ?";
                    PreparedStatement smt = conn.prepareStatement(query1);
                    smt.setString(1,nomProduit);
                    smt.executeUpdate();
                    String query2 = "UPDATE stock_product SET quantity = quantity + 1 WHERE nom = ?";
                    PreparedStatement smt1 = conn.prepareStatement(query2);
                    smt1.setString(1,nomProduit);
                    smt1.executeUpdate();
                    String query3 = "SELECT prix FROM vente WHERE nom = ?";
                    PreparedStatement smt2 = conn.prepareStatement(query3);
                    smt2.setString(1,nomProduit);
                    ResultSet res = smt2.executeQuery();
                    while (res.next()) {
                        Prix=res.getFloat("prix");
                    }
                    Total = Total- Prix;
                    jLabel6.setText(Double.toString(Total));
                }
            }catch (Exception ex) {
            
            System.out.println("erreur: "+ex);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            Erreur.erreur = sw.toString();
            JOptionPane.showMessageDialog(this, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        String codebarre = jTextField2.getText();
        Float Prix = 0.0f;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/stock" , "root" , "1234abcD.");
            String query = "SELECT prix FROM stock_product WHERE codebarre = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,codebarre);
            if (codebarre !=""){
            ResultSet res = statement.executeQuery();
             while (res.next()) {
                    Prix=res.getFloat("prix");
                }
            }
            jLabel4.setText(Double.toString(Prix));
        } catch (Exception ex) {
            System.out.println("erreur: "+ex);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            Erreur.erreur = sw.toString();
            JOptionPane.showMessageDialog(this, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jTextField2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventes().setVisible(true);
            }
        });
    }
    DefaultTableModel tableModel = new DefaultTableModel();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JDialog jDialog5;
    private javax.swing.JDialog jDialog6;
    private javax.swing.JDialog jDialog7;
    private javax.swing.JDialog jDialog8;
    private javax.swing.JDialog jDialog9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
public static boolean areRowsEqual(Object[] row1, Object[] row2) {
        // Compare deux lignes pour déterminer si elles sont égales
        return Arrays.equals(row1, row2);
    }

    public static boolean containsRow(Object[][] matrix, Object[] targetRow) {
    // Vérifie si la matrice contient déjà une ligne identique
    for (Object[] row : matrix) {
        if (row != null && row.length > 0 && targetRow != null && targetRow.length > 0) {
            if (row[0] != null && targetRow[0] != null && row[0].equals(targetRow[0])) {
                return true;
            }
        }
    }
    return false;
}
    public static Object[][] removeDuplicateRows(Object[][] matrix) {
        int numRows = matrix.length;
        Object[][] uniqueMatrix = new Object[numRows][];
        int uniqueCount = 0;

        for (int i = 0; i < numRows; i++) {
            if (!containsRow(uniqueMatrix, matrix[i])) {
                uniqueMatrix[uniqueCount] = matrix[i];
                uniqueCount++;
            }
        }

        // Redimensionnez la matrice unique si nécessaire
        return Arrays.copyOf(uniqueMatrix, uniqueCount);
    }
}

