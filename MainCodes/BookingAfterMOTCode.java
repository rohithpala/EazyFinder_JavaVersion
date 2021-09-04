package EazyFinderGUI.MainCodes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookingAfterMOTCode {
    public BookingAfterMOTCode(){
        DefaultTableModel dtm;
        ButtonGroup bg;
        JTable table;
        JScrollPane jsp;
    }

    public static void main(String[] args) {
        new BookingAfterMOTCode();
    }
}

//class RadioButtonRenderer implements TableCellRenderer {
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        if (value == null) return null;
//        return (Component) value;
//    }
//}
//
//class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
//    JRadioButton button;
//
//    public RadioButtonEditor(JCheckBox checkBox) {
//        super(checkBox);
//    }
//
//    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        if (value == null) return null;
//        button = (JRadioButton) value;
//        button.addItemListener(this);
//        return (Component) value;
//    }
//
//    public Object getCellEditorValue() {
//        button.removeItemListener(this);
//        return button;
//    }
//
//    public void itemStateChanged(ItemEvent e) {
//        super.fireEditingStopped();
//    }
//}