import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class NotepadClone extends JFrame implements ActionListener {
    private String text;
    private JTextArea area;

    File file = null; // this is actual file after saved && open.
    String filePath = "";
    int flag = 0;


    NotepadClone() {

        setSize(1950, 1050);
        setLayout(new BorderLayout());
        setTitle("NotepadClone");
        ImageIcon note = new ImageIcon("note.jpg");
        Image icon = note.getImage();
        setIconImage(icon);

        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");

        JMenuItem newDoc = new JMenuItem("New");
        newDoc.addActionListener(this);
        newDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

        JMenuItem openDoc = new JMenuItem("Open");
        openDoc.addActionListener(this);
        openDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        JMenuItem saveDoc = new JMenuItem("Save");
        saveDoc.addActionListener(this);
        saveDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        JMenuItem saveAsDoc = new JMenuItem("Save As");
        saveAsDoc.addActionListener(this);
        saveAsDoc.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK));

        JMenuItem printDoc = new JMenuItem("Print");
        printDoc.addActionListener(this);
        printDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        file.add(newDoc);

        JMenuItem exitDoc = new JMenuItem("Exit");
        exitDoc.addActionListener(this);
        exitDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, ActionEvent.SHIFT_MASK));

        file.add(newDoc);
        file.add(openDoc);
        file.add(saveDoc);
        file.add(saveAsDoc);
        file.add(printDoc);
        file.add(exitDoc);

        menuBar.add(file);

        JMenu edit = new JMenu("Edit");
        edit.addActionListener(this);

        JMenuItem copyDoc = new JMenuItem("Copy");
        copyDoc.addActionListener(this);
        copyDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

        JMenuItem pasteDoc = new JMenuItem("Paste");
        pasteDoc.addActionListener(this);
        pasteDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        JMenuItem cutDoc = new JMenuItem("Cut");
        cutDoc.addActionListener(this);
        cutDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

        JMenuItem selectDoc = new JMenuItem("Select All");
        selectDoc.addActionListener(this);
        selectDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));

        edit.add(copyDoc);
        edit.add(pasteDoc);
        edit.add(cutDoc);
        edit.add(selectDoc);

        menuBar.add(edit);

        JMenu help = new JMenu("Help");

        JMenuItem helpDoc = new JMenuItem("Help");
        helpDoc.addActionListener(this);
        helpDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));


        help.add(helpDoc);

        menuBar.add(help);

        setJMenuBar(menuBar); // menubar set to JFrame window

        area = new JTextArea();
        area.setFont(new Font("SAN_SERIF", Font.PLAIN, 20));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        // add ScollBar on TextAreaa
        JScrollPane pane = new JScrollPane(area);
        pane.setBorder(BorderFactory.createEmptyBorder());


        // Add components to main JFrame
        add(pane, BorderLayout.CENTER);
        // add(outputPanel, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(pane); // pane(JScrollPane) added onto JFrame

        setExtendedState(JFrame.MAXIMIZED_BOTH); // full-screen Frame        
        setVisible(true);
        
    }

    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();

        if (name.equals("New")) {

            area.setText("");
            flag = 0;
            file = null;
        } else if (name.equals("Open")) {

            flag = 0;

            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(true); // for All files
            int action = chooser.showOpenDialog(this); // Show open dialog
            if (action != JFileChooser.APPROVE_OPTION) {
                return;
            }
            // for reading-purpose (file)
            file = chooser.getSelectedFile();
            filePath = file.getPath();
            System.out.println(filePath);

            
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                area.read(reader, null);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

        } else if (name.equals("Save")) {
            
            if(isTextAreaEmpty()){
                JOptionPane.showMessageDialog(this,"Will Not store Empty-File");
                return;
            }
            if (file == null) {
                saveAsNewFile();
            } else {
                saveAsRepeatFile();
            }
        } else if (name.equals("Save As")) {
            if(isTextAreaEmpty()){
                JOptionPane.showMessageDialog(this,"Will Not store Empty-File");
            }
            saveAsNewFile();
        } else if (name.equals("Print")) {
            try {
                area.print();
            } catch (Exception exc) {
                System.out.println(exc);
            }
        } else if (name.equals("Exit")) {
            System.exit(0);
        } else if (name.equals("Copy")) {
            text = area.getSelectedText();
        } else if (name.equals("Paste")) {
            area.insert(text, area.getCaretPosition());
        } else if (name.equals("Cut")) {
            text = area.getSelectedText();
            area.replaceRange("", area.getSelectionStart(), area.getSelectionEnd());
        }else if (name.equals("Select All")) {
            area.selectAll();
        } else if (name.equals("Help")) {
            getHelp();
        }
    }

    // if user-saves the the file intially when it opens.
    private boolean isTextAreaEmpty(){
        if(area.getText().isEmpty()){
            return true;
        }
        return false;
    }
    private void getHelp() {

        area.setText("");
        int c;
        FileReader in = null;
        try {
            in = new FileReader("Help.txt");
            while ((c = in.read()) != -1) {
                area.append(Character.toString(c));
            }   
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try{
                in.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    // Method to determine the file extension based on the file name
    private String getFileType(String fileName) {
        if (fileName.endsWith(".java")) {
            return "java";
        } else if (fileName.endsWith(".c")) {
            return "c";
        } else if (fileName.endsWith(".cpp")) {
            return "cpp";
        } else if (fileName.endsWith(".html")) {
            return "html";
        } else if (fileName.endsWith(".py")) {
            return "py";
        } else if (fileName.endsWith(".txt")) {
            return "txt";
        } else {
            return null; // Unsupported file type
        }
    }

    private void saveAsNewFile() {

        JFileChooser saveChooser = new JFileChooser();
        int saveAction = saveChooser.showSaveDialog(this);
        if (saveAction != JFileChooser.APPROVE_OPTION) {
            return;
        }

        file = saveChooser.getSelectedFile();
        filePath = file.getPath();

        System.out.println("file:" + file);
        System.out.println("File path:" + filePath);

        // Check if the file already has an extension, if not, add appropriate extension
        if (!filePath.toLowerCase().endsWith(".java") && !filePath.toLowerCase().endsWith(".c")
                && !filePath.toLowerCase().endsWith(".cpp") && !filePath.toLowerCase().endsWith(".html")
                && !filePath.toLowerCase().endsWith(".txt") && !filePath.toLowerCase().endsWith(".py")) {
            // Determine the appropriate extension based on the file type
            String fileType = getFileType(file.getName());
            if (fileType == null) {
                JOptionPane.showMessageDialog(this, "Unsupported file type");
                return;
            }
            filePath += "." + fileType;
            filePath += "." + fileType;
        }

        System.out.println("After File Saving:");
        System.out.println(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(area.getText());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private void saveAsRepeatFile() {
        // JOptionPane.showMessageDialog(this,"This is Reapeat File");
        filePath = file.getAbsolutePath();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(area.getText());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "File Saved Successfully..!");
    }

    public static void main(String[] args) {
        new NotepadClone();
    }
}