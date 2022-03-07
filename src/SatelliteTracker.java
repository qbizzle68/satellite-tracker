import javax.swing.*;

public class SatelliteTracker extends JFrame {
    private JPanel mainPanel;
    private JTextPane tleViewPanel;
    private JLabel tleLabel;
    private JLabel locationLabel;
    private JRadioButton locationManual;
    private JLabel latitudeLabel;
    private JSpinner latitudeDegreeSpinner;
    private JLabel latDegreeSymbol;
    private JSpinner latitudeMinuteSpinner;
    private JSpinner latitudeSecondSpinner;
    private JLabel latMinuteSymbol;
    private JLabel latSecondSymbol;
    private JLabel longitudeLabel;
    private JSpinner longDegreeSpinner;
    private JSpinner longMinuteSpinner;
    private JSpinner longSecondSpinner;
    private JLabel longSecondSymbol;
    private JLabel longMinuteSymbol;
    private JLabel longDegreeSymbol;
    private JRadioButton locationLookup;
    private JTextField locationSearchText;
    private JSeparator tleLocationSeparator;
    private JSeparator locationTleSeparator;
    private JLabel tleLookupLabel;
    private JRadioButton tleLookupManual;
    private JTextField textField1;
    private JRadioButton tleLookupSearch;
    private JButton searchButton;
    private JTextField tleSearchText;
    private JButton tleSearchButton;
    private JSeparator tleLookupPassSeparator;
    private JLabel passLabel;
    private JTable passTable;

    public SatelliteTracker(String title) {
        super(title);
//        SpinnerModel latSpinnerModel = new SpinnerNumberModel(38, -90, 90, 1);
//        SpinnerModel longSpinnerModel = new SpinnerNumberModel(-97, -180, 180, 1);
//        SpinnerModel subDegreeSpinnerModel = new SpinnerNumberModel(0, 0, 59, 1);
//
//        latitudeDegreeSpinner = new JSpinner(latSpinnerModel);


        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }

    public static void main(String[] args) {
        JFrame frame = new SatelliteTracker("Satellite Tracker");
        frame.setVisible(true);
    }

}
