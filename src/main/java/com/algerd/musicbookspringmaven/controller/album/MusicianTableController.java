package com.algerd.musicbookspringmaven.controller.album;

import com.algerd.musicbookspringmaven.controller.BaseIncludeController;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import com.algerd.musicbookspringmaven.utils.Helper;
import com.algerd.musicbookspringmaven.repository.Musician.MusicianEntity;
import com.algerd.musicbookspringmaven.repository.MusicianAlbum.MusicianAlbumEntity;
import com.algerd.musicbookspringmaven.repository.MusicianInstrument.MusicianInstrumentEntity;
import static com.algerd.musicbookspringmaven.service.impl.ContextMenuItemType.ADD_MUSICIAN_ALBUM;
import static com.algerd.musicbookspringmaven.service.impl.ContextMenuItemType.DELETE_MUSICIAN_ALBUM;
import static com.algerd.musicbookspringmaven.service.impl.ContextMenuItemType.EDIT_MUSICIAN_ALBUM;

public class MusicianTableController extends BaseIncludeController<AlbumPaneController> {
       
    private MusicianAlbumEntity selectedItem;   
    
    @FXML
    private AnchorPane musicianTable;
    @FXML
    private TableView<MusicianAlbumEntity> musicianTableView;
    @FXML
    private TableColumn<MusicianAlbumEntity, String> musicianColumn;
    @FXML
    private TableColumn<MusicianAlbumEntity, MusicianAlbumEntity> instrumentColumn;   
    @FXML
    private TableColumn<MusicianAlbumEntity, Integer> ratingColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        musicianColumn.setCellValueFactory(cellData -> cellData.getValue().getMusician().nameProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().getMusician().ratingProperty().asObject());
        
        instrumentColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        instrumentColumn.setCellFactory(col -> {
            TableCell<MusicianAlbumEntity, MusicianAlbumEntity> cell = new TableCell<MusicianAlbumEntity, MusicianAlbumEntity>() {
                @Override
                public void updateItem(MusicianAlbumEntity item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    if (!empty) {
                        List<MusicianInstrumentEntity> musicianInstruments = 
                            repositoryService.getMusicianInstrumentRepository().selectMusicianInstrumentByMusician(item.getMusician());
                        String str = "";
                        for (MusicianInstrumentEntity musicianInstrument :  musicianInstruments){
                            str += (!str.equals("")) ? ", " : "";
                            str += musicianInstrument.getInstrument().getName();
                        }
                        this.setText(str);                                         
                    }
                }
            };
			return cell;
        });
    }  
    
    @Override
    public void bootstrap() {   
        setTableValue();
        initListeners();
        initRepositoryListeners();
    }
    
    private void setTableValue() {
        clearSelectionTable();
        musicianTableView.getItems().clear();
        List<MusicianAlbumEntity> musicianAlbums = repositoryService.getMusicianAlbumRepository().selectMusicianAlbumByAlbum(paneController.getAlbum());              
        musicianTableView.setItems(FXCollections.observableArrayList(musicianAlbums));
        sort();
        Helper.setHeightTable(musicianTableView, 10);
    }
    
    private void initRepositoryListeners() {
        repositoryService.getMusicianAlbumRepository().clearChangeListeners(this);                
        repositoryService.getMusicianRepository().clearDeleteListeners(this);           
        repositoryService.getMusicianRepository().clearUpdateListeners(this);        
        repositoryService.getAlbumRepository().clearDeleteListeners(this);           
        repositoryService.getAlbumRepository().clearUpdateListeners(this);              
        repositoryService.getArtistRepository().clearDeleteListeners(this);
      
        repositoryService.getMusicianAlbumRepository().addChangeListener(this::changed, this);                
        repositoryService.getMusicianRepository().addDeleteListener(this::changed, this);           
        repositoryService.getMusicianRepository().addUpdateListener(this::changed, this);        
        repositoryService.getAlbumRepository().addDeleteListener(this::changed, this);           
        repositoryService.getAlbumRepository().addUpdateListener(this::changed, this);              
        repositoryService.getArtistRepository().addDeleteListener(this::changed, this);
    }
    
    private void initListeners() {
        musicianTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectedItem = musicianTableView.getSelectionModel().getSelectedItem()
        );       
    }
    
    private void changed(ObservableValue observable, Object oldVal, Object newVal) {
        setTableValue();
    }
    
    private void sort() {
        clearSelectionTable();
        musicianTableView.getItems().sort(Comparator.comparing((MusicianAlbumEntity musicianAlbum) -> musicianAlbum.getMusician().getName()));
    }
    
    private void clearSelectionTable() {
        musicianTableView.getSelectionModel().clearSelection();
        selectedItem = null;
    }
    
    @FXML
    private void onMouseClickTable(MouseEvent mouseEvent) {
        boolean isShowingContextMenu = contextMenuService.getContextMenu().isShowing();
        contextMenuService.clear();  
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            // если контекстное меню выбрано, то лкм сбрасывает контекстное меню и выбор в таблице
            if (isShowingContextMenu) {
                clearSelectionTable();
            }
            // если лкм выбрана запись - показать её
            if (selectedItem != null) {
                MusicianEntity musician = repositoryService.getMusicianRepository().selectById(selectedItem.getMusician().getId());
                requestPageService.musicianPane(musician);
            }
        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) { 
            MusicianAlbumEntity newMusicianAlbum = new MusicianAlbumEntity();
            newMusicianAlbum.setId_album(paneController.getAlbum().getId());
            contextMenuService.add(ADD_MUSICIAN_ALBUM, newMusicianAlbum);                    
            if (selectedItem != null) {
                contextMenuService.add(EDIT_MUSICIAN_ALBUM, selectedItem);
                contextMenuService.add(DELETE_MUSICIAN_ALBUM, selectedItem);     
            }
            contextMenuService.show(paneController.getView(), mouseEvent);            
        }
    }  
    
}
