import com.qbizzle.http.Requests;
import com.qbizzle.orbit.TLE;
import com.qbizzle.time.JD;
import com.qbizzle.tracking.Coordinates;
import com.qbizzle.tracking.SatellitePass;
import com.qbizzle.tracking.Tracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.Vector;

public class SatelliteTracker extends JFrame {
    private JPanel mainPanel;
    private JTextPane tleViewPanel;
    private JRadioButton locationManual;
    private JSpinner latitudeDegreeSpinner;
    private JSpinner latitudeMinuteSpinner;
    private JSpinner latitudeSecondSpinner;
    private JSpinner longDegreeSpinner;
    private JSpinner longMinuteSpinner;
    private JSpinner longSecondSpinner;
    private JRadioButton locationLookup;
    private JTextField locationSearchText;
    private JRadioButton tleLookupManual;
    private JTextArea tleImportText;
    private JRadioButton tleLookupSearch;
    private JButton locationSearchButton;
    private JTextField tleSearchText;
    private JButton tleSearchButton;
    private JTable passTable;
    private JButton tleManualImport;
    private JButton generatePassesButton;
    private JScrollPane passScrollPane;
    //    unused components
//    private JLabel tleLabel;
//    private JLabel locationLabel;
//    private JLabel latitudeLabel;
//    private JLabel latDegreeSymbol;
//    private JLabel latMinuteSymbol;
//    private JLabel latSecondSymbol;
//    private JLabel longitudeLabel;
//    private JLabel longSecondSymbol;
//    private JLabel longMinuteSymbol;
//    private JLabel longDegreeSymbol;
//    private JSeparator tleLocationSeparator;
//    private JSeparator locationTleSeparator;
//    private JLabel tleLookupLabel;
//    private JSeparator tleLookupPassSeparator;
//    private JLabel passLabel;

    private static TLE tle;
    private static Coordinates geoPos;
    private static double duration;
    private final String[] tableHeader = {
            "Date",
            "Visible",
            "Height",
            "Dir",
            "Max",
            "Height",
            "Dir",
            "Disappear",
            "Height",
            "Max"
    };

    public SatelliteTracker(String title) {
        super(title);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        locationManual.setSelected(true);
        tleLookupSearch.setSelected(true);
        displayGeoPosition();
        displayTLE();
        this.setSize(new Dimension(616, 750));


        locationManual.addActionListener(e -> {
            latitudeDegreeSpinner.setEnabled(true);
            latitudeMinuteSpinner.setEnabled(true);
            latitudeSecondSpinner.setEnabled(true);
            longDegreeSpinner.setEnabled(true);
            longMinuteSpinner.setEnabled(true);
            longSecondSpinner.setEnabled(true);
            locationSearchText.setEnabled(false);
            locationSearchButton.setEnabled(false);
        });
        locationLookup.addActionListener(e -> {
            locationSearchText.setEnabled(true);
            locationSearchButton.setEnabled(true);
            latitudeDegreeSpinner.setEnabled(false);
            latitudeMinuteSpinner.setEnabled(false);
            latitudeSecondSpinner.setEnabled(false);
            longDegreeSpinner.setEnabled(false);
            longMinuteSpinner.setEnabled(false);
            longSecondSpinner.setEnabled(false);
        });
        tleLookupManual.addActionListener(e -> {
            tleImportText.setEnabled(true);
            tleManualImport.setEnabled(true);
            tleSearchText.setEnabled(false);
            tleSearchButton.setEnabled(false);
        });
        tleLookupSearch.addActionListener(e -> {
            tleImportText.setEnabled(false);
            tleManualImport.setEnabled(false);
            tleSearchText.setEnabled(true);
            tleSearchButton.setEnabled(true);
        });
        latitudeDegreeSpinner.addChangeListener(e -> {
            int value = (int) latitudeDegreeSpinner.getValue();
            if (value == 91) latitudeDegreeSpinner.setValue(-90);
            else if (value == -91) latitudeDegreeSpinner.setValue(90);
            else if (value < -91 || value > 91) latitudeDegreeSpinner.setValue(0);
            updateGeoPosition();
        });
        longDegreeSpinner.addChangeListener(e -> {
            int value = (int) longDegreeSpinner.getValue();
            if (value == 181) longDegreeSpinner.setValue(-180);
            else if (value == -181) longDegreeSpinner.setValue(180);
            else if (value < -181 || value > 181) longDegreeSpinner.setValue(0);
            updateGeoPosition();
        });
        latitudeMinuteSpinner.addChangeListener(e -> {
            int value = (int) latitudeMinuteSpinner.getValue();
            if (value == 60) latitudeMinuteSpinner.setValue(0);
            else if (value == -1) latitudeMinuteSpinner.setValue(59);
            else if (value < -1 || value > 60) latitudeMinuteSpinner.setValue(0);
            updateGeoPosition();
        });
        latitudeSecondSpinner.addChangeListener(e -> {
            int value = (int) latitudeSecondSpinner.getValue();
            if (value == 60) latitudeSecondSpinner.setValue(0);
            else if (value == -1) latitudeSecondSpinner.setValue(59);
            else if (value < -1 || value > 60) latitudeSecondSpinner.setValue(0);
            updateGeoPosition();
        });
        longMinuteSpinner.addChangeListener(e -> {
            int value = (int) longMinuteSpinner.getValue();
            if (value == 60) longMinuteSpinner.setValue(0);
            else if (value == -1) longMinuteSpinner.setValue(59);
            else if (value < -1 || value > 60) longMinuteSpinner.setValue(0);
            updateGeoPosition();
        });
        longSecondSpinner.addChangeListener(e -> {
            int value = (int) longSecondSpinner.getValue();
            if (value == 60) longSecondSpinner.setValue(0);
            else if (value == -1) longSecondSpinner.setValue(59);
            else if (value < -1 || value > 60) longSecondSpinner.setValue(0);
            updateGeoPosition();
        });
        locationSearchButton.addActionListener(e -> {
            Coordinates fetchedGeoPos;
            try {
                fetchedGeoPos = Requests.getGeoPosition(locationSearchText.getText());
            } catch (Exception ex) {
                latitudeDegreeSpinner.setValue(0);
                latitudeMinuteSpinner.setValue(0);
                latitudeSecondSpinner.setValue(0);
                longDegreeSpinner.setValue(0);
                longMinuteSpinner.setValue(0);
                longSecondSpinner.setValue(0);
                return;
            }
            geoPos.setLatitude(fetchedGeoPos.getLatitude());
            geoPos.setLongitude((fetchedGeoPos.getLongitude()));
            displayGeoPosition();
        });
        tleSearchButton.addActionListener(e -> {
            Vector<TLE> tleList;
            try {
                tleList = Requests.getTLEList(tleSearchText.getText());
            } catch (Exception ex) {
                // need a way to signal error here
                return;
            }
            tle = tleList.get(0);
            displayTLE();
        });
        tleManualImport.addActionListener(e -> {
//                need to catch all exceptions here
            tle = new TLE(tleImportText.getText());
            displayTLE();
        });
        generatePassesButton.addActionListener(e -> {
            var now = new JD(new Date());
            Vector<SatellitePass> passList = Tracker.getPasses(
                    tle, now, now.Future(duration), geoPos
            );

            DefaultTableModel model = new DefaultTableModel(tableHeader, 0);
            for (SatellitePass pass :
                    passList) {
                model.addRow(new Object[] {
                        pass.getVisibleTime().day(-6),
                        pass.getVisibleTime().time(-6),
                        Math.round( pass.getVisibleHeight() ),
                        pass.getVisibleDirection(),
                        pass.getMaxTime().time(-6),
                        Math.round( pass.getMaxHeight() ),
                        pass.getMaxDirection(),
                        pass.getDisappearTime().time(-6),
                        Math.round( pass.getDisappearHeight() ),
                        pass.getDisappearDirection()
                });
            }
            passTable.setModel(model);
        });
    }

    public static void main(String[] args) {
        geoPos = new Coordinates(38.0608, -97.9298);
        duration = 14.0;
//        either do this, lookup with http request everytime, or save the most recent tle before exiting
        tle = new TLE("""
                ISS (ZARYA)            \s
                1 25544U 98067A   22066.87965278  .00007224  00000+0  13623-3 0  9990
                2 25544  51.6433 118.2979 0004901 229.8721 120.0122 15.49615511329466""");
        JFrame frame = new SatelliteTracker("Satellite Tracker");
        frame.setVisible(true);
    }

    private void updateGeoPosition() {
        int latDegree = (int)latitudeDegreeSpinner.getValue();
        if (latDegree == 90 || latDegree == -90) geoPos.setLatitude(latDegree);
        else geoPos.setLatitude(
                latDegree + ((int)latitudeMinuteSpinner.getValue() / 60.0) + ((int)latitudeSecondSpinner.getValue() / 3600.0)
        );
        int longDegree = (int)longDegreeSpinner.getValue();
        if(longDegree == 180 || longDegree == -180) geoPos.setLongitude(longDegree);
        else geoPos.setLongitude(
                longDegree + ((int)longMinuteSpinner.getValue() / 60.0) + ((int)longSecondSpinner.getValue() / 3600.0)
        );
    }

    private void displayGeoPosition() {
        double lat = geoPos.getLatitude();
        int latDegree = (int)lat;
        double latFloat = Math.abs(lat - latDegree);
        int latMinute = (int)(latFloat * 60);
        int latSecond = (int)Math.round((latFloat - latMinute / 60.0) * 3600);
        double lng = geoPos.getLongitude();
        int lngDegree = (int)lng;
        double lngFloat = Math.abs(lng - lngDegree);
        int lngMinute = (int)(lngFloat * 60);
        int lngSecond = (int)Math.round((lngFloat - lngMinute / 60.0) * 3600);

        latitudeDegreeSpinner.setValue(latDegree);
        latitudeMinuteSpinner.setValue(latMinute);
        latitudeSecondSpinner.setValue(latSecond);
        longDegreeSpinner.setValue(lngDegree);
        longMinuteSpinner.setValue(lngMinute);
        longSecondSpinner.setValue(lngSecond);
    }

    private void displayTLE() {
        StringBuilder str = new StringBuilder(tle.getLine0())
                .append("\n")
                .append(tle.getLine1())
                .append("\n")
                .append(tle.getLine2());
        tleViewPanel.setText(new String(str));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
//        String[] columnHeaders = {"abc", "def", "ghi", "jkl"};
        Object[][] data = {{" "," "," "," "," "," "," "," "," "," ",}};
        passTable = new JTable(data, tableHeader);
        passScrollPane = new JScrollPane(passTable);
    }
}
