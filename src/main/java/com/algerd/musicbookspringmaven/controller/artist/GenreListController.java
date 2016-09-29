package com.algerd.musicbookspringmaven.controller.artist;


import com.algerd.musicbookspringmaven.controller.BaseIncludeController;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.beans.value.ObservableValue;
import com.algerd.musicbookspringmaven.repository.ArtistGenre.ArtistGenreEntity;
import com.algerd.musicbookspringmaven.repository.Genre.GenreEntity;
import com.algerd.musicbookspringmaven.utils.Helper;

public class GenreListController extends BaseIncludeController<ArtistPaneController> {

    @FXML
    private AnchorPane genreList;
    @FXML
    private ListView<GenreEntity> genreListView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    }
    
    @Override
    public void bootstrap() {
        setListValue();
        initRepositoryListeners();     
    }
    
    private void setListValue() {
        List<GenreEntity> genres = new ArrayList<>();
        List<ArtistGenreEntity> artistGenres = repositoryService.getArtistGenreRepository().selectArtistGenreByArtist(paneController.getArtist());
        artistGenres.stream().forEach(artistGenre -> genres.add(artistGenre.getGenre())); 
        genreListView.getItems().clear();       
        if (!genres.isEmpty()) {
            genreListView.getItems().addAll(genres);
            sort();
        } else {
            GenreEntity genre = new GenreEntity();
            genre.setName("Unknown");
            genreListView.getItems().add(genre);
        }                     
        Helper.setHeightList(genreListView, 8);        
    }
    
    private void initRepositoryListeners() {
        repositoryService.getArtistGenreRepository().clearChangeListeners(this);                                      
        repositoryService.getArtistRepository().clearUpdateListeners(this);                
        repositoryService.getGenreRepository().clearDeleteListeners(this);           
        repositoryService.getGenreRepository().clearUpdateListeners(this);      
    
        repositoryService.getArtistGenreRepository().addChangeListener(this::changed, this);                                      
        repositoryService.getArtistRepository().addUpdateListener(this::changed, this);                
        repositoryService.getGenreRepository().addDeleteListener(this::changed, this);           
        repositoryService.getGenreRepository().addUpdateListener(this::changed, this);       
    }
    
    private void changed(ObservableValue observable, Object oldVal, Object newVal) {
        setListValue();
    }
   
    private void sort() {
        genreListView.getSelectionModel().clearSelection();
        genreListView.getItems().sort(Comparator.comparing(GenreEntity::getName));
    }  
    
     @FXML
    private void onMouseClickGenreList(MouseEvent mouseEvent) {    
        contextMenuService.clear();   
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {           
            GenreEntity selectedItem = genreListView.getSelectionModel().getSelectedItem();
            // если лкм выбрана запись - показать её
            if (selectedItem != null && selectedItem.getId() != 0) {
                // Дозагрузка
                GenreEntity genre = repositoryService.getGenreRepository().selectById(selectedItem.getId());
                requestPageService.genrePane(genre);
            }           
        }
    }
    
}
