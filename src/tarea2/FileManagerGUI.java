package tarea2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Abdias
 */
public class FileManagerGUI extends JFrame implements ActionListener {

    private JButton createButton;
    private JButton openButton;
    private JButton filterButton;
    private JButton moveButton;
    private JButton viewButton;
    private JButton deleteButton;

    public FileManagerGUI() {
        setTitle("File explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);

        createButton = new JButton("Create New File");
        openButton = new JButton("Open File");
        filterButton = new JButton("Filter by extension");
        moveButton = new JButton("Move File");
        viewButton = new JButton("Show properties");
        deleteButton = new JButton("Delete File");

        createButton.addActionListener(this);
        openButton.addActionListener(this);
        filterButton.addActionListener(this);
        moveButton.addActionListener(this);
        viewButton.addActionListener(this);
        deleteButton.addActionListener(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        panel.add(createButton);
        panel.add(openButton);
        panel.add(filterButton);
        panel.add(moveButton);
        panel.add(viewButton);
        panel.add(deleteButton);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton) {
            createFile();
        } else if (e.getSource() == openButton) {
            openFile();
        } else if (e.getSource() == filterButton) {
            filterByExtension();
        } else if (e.getSource() == moveButton) {
            moveFile();
        } else if (e.getSource() == viewButton) {
            viewFileAttributes();
        } else if (e.getSource() == deleteButton) {
            deleteFile();
        }
    }

    private void createFile() {
        String fileName = JOptionPane.showInputDialog(this,
                "Enter the file name:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            File DesktopDirectory
                    = FileSystemView.getFileSystemView().getHomeDirectory();
            String path = DesktopDirectory.getAbsolutePath()
                    + File.separator + fileName;
            File file = new File(path);
            try {
                if (file.createNewFile()) {
                    JOptionPane.showMessageDialog(this, "File created"
                            + " successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "Already"
                            + " existing file.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creating the file:"
                        + " " + e.getMessage());
            }
        }
    }

    //the desktop class is used to open the file
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {

                // usign desktop
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(selectedFile);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Cannot open the file "
                            + "on this system.");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error opening the file: "
                        + e.getMessage());
            }
        }
    }

    //
    private void filterByExtension() {
        String extension = JOptionPane.showInputDialog(this, "Enter the file "
                + "extension:");

        if (extension != null && !extension.trim().isEmpty()) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            FileNameExtensionFilter filter = new FileNameExtensionFilter(""
                    + "Files"
                    + " (*." + extension + ")", extension);
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String selectedFilePath = selectedFile.getAbsolutePath();

                // Check if the selected file has the desired extension
                if (getFileExtension(selectedFile).equalsIgnoreCase(extension)
                        )
                {
                    JOptionPane.showMessageDialog(this, "Selected file: "
                            + selectedFilePath);
                } else {
                    JOptionPane.showMessageDialog(this, "The selected"
                            + " file does"
                            + " not have the desired extension.");
                }
            }
        }
    }

    private void moveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            JFileChooser files = new JFileChooser();
            files.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int folderResult = files.showDialog(this, "Select Destination "
                    + "Folder");

            if (folderResult == JFileChooser.APPROVE_OPTION) {
                File destinationFolder = files.getSelectedFile();
                if (destinationFolder != null
                        && destinationFolder.isDirectory()) {
                    File newFile = 
                            new File(destinationFolder.getAbsolutePath()
                            + File.separator + selectedFile.getName());
                    if (selectedFile.renameTo(newFile)) {
                        JOptionPane.showMessageDialog(this, "File moved"
                                + " successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error when"
                                + " moving"
                                + " the file.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid destination"
                            + " folder selected.");
                }
            }
        }
    }

    private void viewFileAttributes() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            StringBuilder attributes = new StringBuilder();
            attributes.append("Name:"
                    + " ").append(selectedFile.getName()).append("\n");
            attributes.append("Path absolute:"
                    +" ").append(selectedFile.getAbsolutePath()).append("\n");
            attributes.append("Size:"
                    + " ").append(selectedFile.length()).append(""
                    + " bytes").append("\n");
            attributes.append("Is directory:"
                    + " ").append(selectedFile.isDirectory()).append("\n");
            attributes.append("Is a File:"
                    + " ").append(selectedFile.isFile()).append("\n");
            attributes.append("Is hidden:"
                    + " ").append(selectedFile.isHidden());
            JOptionPane.showMessageDialog(this, attributes.toString(),
                    "File attributes", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            int confirmResult = JOptionPane.showConfirmDialog(this,
                    "¿You are sure you want to delete the file?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirmResult == JOptionPane.YES_OPTION) {
                if (selectedFile.delete()) {
                    JOptionPane.showMessageDialog(this, "File successfull"
                            + " delete.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting"
                            + " file.");
                }
            }
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOfDot = name.lastIndexOf(".");
        if (lastIndexOfDot != -1 && lastIndexOfDot != 0) {
            return name.substring(lastIndexOfDot + 1);
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FileManagerGUI fileManagerGUI = new FileManagerGUI();
                fileManagerGUI.setVisible(true);
            }
        });
    }
}
