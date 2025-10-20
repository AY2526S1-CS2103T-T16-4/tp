package bloodnet.ui;

import java.util.logging.Logger;

import bloodnet.commons.core.LogsCenter;
import bloodnet.model.donationrecord.DonationRecord;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

public class DonationRecordListPanel extends UiPart<Region> {
    private static final String FXML = "DonationRecordListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(DonationRecordListPanel.class);

    @FXML
    private ListView<DonationRecord> donationRecordListView;

    public DonationRecordListPanel(ObservableList<DonationRecord> donationRecordList) {
        super(FXML);
        donationRecordListView.setItems(donationRecordList);
        donationRecordListView.setCellFactory(listView -> new DonationRecordListViewCell());
    }

    class DonationRecordListViewCell extends ListCell<DonationRecord> {
        @Override
        protected void updateItem(DonationRecord record, boolean empty) {
            super.updateItem(record, empty);

            if (empty || record == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new DonationRecordCard(record, getIndex() + 1).getRoot());
            }
        }
    }


}
