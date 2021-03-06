
package com.algerd.musicbookspringmaven.controller.musician;

import com.algerd.musicbookspringmaven.controller.BaseIncludeController;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import com.algerd.musicbookspringmaven.utils.Helper;
import com.algerd.musicbookspringmaven.repository.MusicianSong.MusicianSongEntity;
import com.algerd.musicbookspringmaven.repository.Song.SongEntity;
import static com.algerd.musicbookspringmaven.service.ContextMenuItemType.ADD_MUSICIAN_SONG;
import static com.algerd.musicbookspringmaven.service.ContextMenuItemType.DELETE_MUSICIAN_SONG;
import static com.algerd.musicbookspringmaven.service.ContextMenuItemType.EDIT_MUSICIAN_SONG;

public class SongTableController extends BaseIncludeController<MusicianPaneController> {
    
    private MusicianSongEntity selectedItem;   
    
    @FXML
    private AnchorPane songTable;
    @FXML
    private Label titleLabel;
    @FXML
    private TableView<MusicianSongEntity> songTableView;
    @FXML
    private TableColumn<MusicianSongEntity, String> artistColumn;
    @FXML
    private TableColumn<MusicianSongEntity, String> albumColumn;   
    @FXML
    private TableColumn<MusicianSongEntity, Integer> yearColumn;
    @FXML
    private TableColumn<MusicianSongEntity, String> songColumn;       
    @FXML
    private TableColumn<MusicianSongEntity, Integer> ratingColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artistColumn.setCellValueFactory(cellData -> cellData.getValue().getSong().getAlbum().getArtist().nameProperty());
        albumColumn.setCellValueFactory(cellData -> cellData.getValue().getSong().getAlbum().nameProperty());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().getSong().getAlbum().yearProperty().asObject());
        songColumn.setCellValueFactory(cellData -> cellData.getValue().getSong().nameProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().getSong().ratingProperty().asObject());
    } 
    
    @Override
    public void bootstrap() {
        setTableValue();
        initListeners();
        titleLabel.setText("Песни с авторством музыканта " + paneController.getMusician().getName());
    }
    
    private void setTableValue() {
        List<MusicianSongEntity> musicianSongs = repositoryService.getMusicianSongRepository().selectMusicianSongByMusician(paneController.getMusician());
        clearSelectionTable();
        songTableView.getItems().clear();
        songTableView.setItems(FXCollections.observableArrayList(musicianSongs));
        sort();
        Helper.setHeightTable(songTableView, 10);  
    }
    
    private void initListeners() {
        songTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectedItem = songTableView.getSelectionModel().getSelectedItem()
        );               
    }
    
    private void initRepositoryListeners() {
        //clear listeners
        repositoryService.getMusicianSongRepository().clearChangeListeners(this);               
        repositoryService.getSongRepository().clearDeleteListeners(this);           
        repositoryService.getSongRepository().clearUpdateListeners(this);       
        repositoryService.getMusicianRepository().clearDeleteListeners(this);           
        repositoryService.getMusicianRepository().clearUpdateListeners(this);
        repositoryService.getAlbumRepository().clearDeleteListeners(this);
        repositoryService.getArtistRepository().clearDeleteListeners(this);   
        
        //add listeners
        repositoryService.getMusicianSongRepository().addChangeListener(this::changed, this);               
        repositoryService.getSongRepository().addDeleteListener(this::changed, this);           
        repositoryService.getSongRepository().addUpdateListener(this::changed, this);       
        repositoryService.getMusicianRepository().addDeleteListener(this::changed, this);           
        repositoryService.getMusicianRepository().addUpdateListener(this::changed, this);
        repositoryService.getAlbumRepository().addDeleteListener(this::changed, this);
        repositoryService.getArtistRepository().addDeleteListener(this::changed, this);   
    }
    
    
    private void changed(ObservableValue observable, Object oldVal, Object newVal) {
        setTableValue();
    }
    
    private void sort() {
        clearSelectionTable();
        songTableView.getItems().sort(Comparator.comparing((MusicianSongEntity musicianSong) -> musicianSong.getSong().getName()));
    }
    
    private void clearSelectionTable() {
        songTableView.getSelectionModel().clearSelection();
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
                SongEntity song = repositoryService.getSongRepository().selectById(selectedItem.getSong().getId());
                requestPageService.songPane(song);
            }
        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) { 
            MusicianSongEntity newMusicianSong = new MusicianSongEntity();
            newMusicianSong.setId_musician(paneController.getMusician().getId());
            contextMenuService.add(ADD_MUSICIAN_SONG, newMusicianSong);                    
            if (selectedItem != null) {
                contextMenuService.add(EDIT_MUSICIAN_SONG, selectedItem);
                contextMenuService.add(DELETE_MUSICIAN_SONG, selectedItem);     
            }
            contextMenuService.show(paneController.getView(), mouseEvent);            
        }
    }  
    
}
