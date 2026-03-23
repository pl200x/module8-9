import Exception.StudentNotExistException;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Main.java — Student CRM GUI
 * Works with Student.java and Exception/StudentNotExistException.java
 *
 * QA / Repository Master contribution
 */
public class Main extends JFrame {

    // ── Collections ──────────────────────────────────────────
    private final ArrayList<Student> studentList = new ArrayList<>();

    // ── UI Components ─────────────────────────────────────────
    private JTextField tfName, tfAge, tfSearch;
    private JComboBox<String> cbGender, cbGrade;
    private JButton btnAdd, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblCount, lblStatus;

    public Main() {
        setTitle("Student CRM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 580);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildForm(),    BorderLayout.WEST);
        add(buildTable(),   BorderLayout.CENTER);
        add(buildStatus(),  BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(40, 44, 60));
        p.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("🎓 Student CRM");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        lblCount = new JLabel("Students: 0");
        lblCount.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblCount.setForeground(new Color(150, 180, 255));

        p.add(title,    BorderLayout.WEST);
        p.add(lblCount, BorderLayout.EAST);
        return p;
    }

    // ── Form Panel ────────────────────────────────────────────
    private JPanel buildForm() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setPreferredSize(new Dimension(260, 0));
        outer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createTitledBorder("Add Student"));
        card.setBackground(new Color(245, 247, 252));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 8, 5, 8);
        gc.gridwidth = 2;

        tfName = new JTextField();
        tfAge  = new JTextField();
        cbGender = new JComboBox<>(new String[]{"1 - Male", "2 - Female", "3 - Other"});
        cbGrade  = new JComboBox<>(new String[]{"A","B","C","D","F"});

        // Name
        gc.gridy = 0; gc.gridx = 0;
        card.add(new JLabel("Name *"), gc);
        gc.gridy = 1;
        card.add(tfName, gc);

        // Age
        gc.gridy = 2;
        card.add(new JLabel("Age *"), gc);
        gc.gridy = 3;
        card.add(tfAge, gc);

        // Gender
        gc.gridy = 4;
        card.add(new JLabel("Gender *"), gc);
        gc.gridy = 5;
        card.add(cbGender, gc);

        // Grade
        gc.gridy = 6;
        card.add(new JLabel("Grade *"), gc);
        gc.gridy = 7;
        card.add(cbGrade, gc);

        // Buttons
        gc.gridy = 8; gc.gridwidth = 1; gc.weightx = 0.5;
        gc.gridx = 0;
        btnAdd = new JButton("Add");
        btnAdd.setBackground(new Color(70, 100, 220));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        card.add(btnAdd, gc);

        gc.gridx = 1;
        btnClear = new JButton("Clear");
        btnClear.setFocusPainted(false);
        card.add(btnClear, gc);

        // Search
        gc.gridy = 9; gc.gridx = 0; gc.gridwidth = 2;
        gc.insets = new Insets(16, 8, 4, 8);
        card.add(new JLabel("Search by name:"), gc);
        gc.gridy = 10; gc.insets = new Insets(2, 8, 5, 8);
        tfSearch = new JTextField();
        card.add(tfSearch, gc);

        // Delete
        gc.gridy = 11; gc.insets = new Insets(8, 8, 8, 8);
        btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(new Color(200, 60, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        card.add(btnDelete, gc);

        // Listeners
        btnAdd.addActionListener(e -> addStudent());
        btnClear.addActionListener(e -> clearForm());
        btnDelete.addActionListener(e -> deleteStudent());
        tfSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    // ── Table Panel ───────────────────────────────────────────
    private JPanel buildTable() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        String[] cols = {"Name", "Age", "Gender", "Grade"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionBackground(new Color(180, 200, 255));
        table.setGridColor(new Color(220, 222, 230));

        // Grade color renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) {
                    l.setForeground(switch (v.toString()) {
                        case "A" -> new Color(0, 150, 80);
                        case "B" -> new Color(50, 120, 200);
                        case "C" -> new Color(180, 130, 0);
                        case "D" -> new Color(200, 80, 0);
                        default  -> new Color(200, 0, 0);
                    });
                }
                return l;
            }
        });

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    // ── Status Bar ────────────────────────────────────────────
    private JPanel buildStatus() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(new Color(240, 242, 248));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        lblStatus = new JLabel("Ready");
        lblStatus.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblStatus.setForeground(Color.GRAY);
        p.add(lblStatus);
        return p;
    }

    // ── Add Student (with Exception Handling) ─────────────────
    private void addStudent() {
        // ── Exception Handling ──────────────────────────────
        String name = tfName.getText().trim();
        String ageStr = tfAge.getText().trim();

        // Validate name
        if (name.isEmpty()) {
            showError("Name cannot be empty.");
            return;
        }
        if (name.length() < 2) {
            showError("Name must be at least 2 characters.");
            return;
        }
        if (!name.matches("[A-Za-z\\s\\-']+")) {
            showError("Name can only contain letters, spaces, hyphens, and apostrophes.");
            return;
        }

        // Validate age — catches wrong data type (non-integer input)
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException ex) {
            showError("Age must be a whole number (e.g. 20).\n\nYou entered: \"" + ageStr + "\"");
            return;
        }
        if (age < 1 || age > 120) {
            showError("Age must be between 1 and 120.");
            return;
        }

        // Gender: extract int from combo selection
        int gender = cbGender.getSelectedIndex() + 1;

        // Grade: extract char from combo selection
        char grade = cbGrade.getSelectedItem().toString().charAt(0);

        // Create Student using their existing Student class
        Student s = new Student(name, age, gender, grade);
        studentList.add(s);

        refreshTable();
        clearForm();
        updateCount();
        setStatus("✅ Added: " + name);
    }

    // ── Delete Student (uses StudentNotExistException) ────────
    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Please select a student from the list first.");
            return;
        }

        String name = tableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete student \"" + name + "\"?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        // ── Uses StudentNotExistException ─────────────────
        try {
            Student found = studentList.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new StudentNotExistException(
                    "Student \"" + name + "\" does not exist in the system."));

            studentList.remove(found);
            refreshTable();
            updateCount();
            setStatus("🗑 Deleted: " + name);

        } catch (StudentNotExistException ex) {
            showError(ex.getMessage());
        }
    }

    // ── Filter / Search ───────────────────────────────────────
    private void filterTable() {
        String q = tfSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        for (Student s : studentList) {
            if (q.isEmpty() || s.getName().toLowerCase().contains(q)) {
                addRow(s);
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────
    private void refreshTable() {
        tableModel.setRowCount(0);
        studentList.forEach(this::addRow);
    }

    private void addRow(Student s) {
        String genderStr = switch (s.getGender()) {
            case 1 -> "Male"; case 2 -> "Female"; default -> "Other";
        };
        tableModel.addRow(new Object[]{s.getName(), s.getAge(), genderStr, s.getGrade()});
    }

    private void clearForm() {
        tfName.setText(""); tfAge.setText("");
        cbGender.setSelectedIndex(0); cbGrade.setSelectedIndex(0);
    }

    private void updateCount() {
        lblCount.setText("Students: " + studentList.size());
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        setStatus("❌ " + msg.split("\n")[0]);
    }

    private void setStatus(String msg) { lblStatus.setText(msg); }

    // ── Main Entry ────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
