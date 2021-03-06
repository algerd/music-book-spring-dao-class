package com.algerd.musicbookspringmaven.controller.musician;

import com.algerd.musicbookspringmaven.controller.BaseDialogController;
import java.net.URL;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import com.algerd.musicbookspringmaven.utils.Helper;
import com.algerd.musicbookspringmaven.repository.Artist.ArtistEntity;
import com.algerd.musicbookspringmaven.repository.Entity;
import com.algerd.musicbookspringmaven.repository.Musician.MusicianEntity;
import com.algerd.musicbookspringmaven.repository.MusicianGroup.MusicianGroupEntity;
import javafx.scene.layout.AnchorPane;

public class MusicianGroupDialogController extends BaseDialogController {
   
    private MusicianGroupEntity musicianGroup;    
    
    @FXML
    private AnchorPane view;
    @FXML
    private ChoiceBox<MusicianEntity> musicianChoiceBox;
    @FXML
    private ChoiceBox<ArtistEntity> artistChoiceBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Helper.initEntityChoiceBox(musicianChoiceBox);
        Helper.initEntityChoiceBox(artistChoiceBox);
        musicianChoiceBox.getItems().addAll(repositoryService.getMusicianRepository().selectAll());
        artistChoiceBox.getItems().addAll(repositoryService.getArtistRepository().selectAll());
        Helper.initDatePicker(startDatePicker, true);
        Helper.initDatePicker(endDatePicker, true);       
    }  
    
    @FXML
    private void handleOkButton() {
        if (isInputValid()) {
            MusicianEntity musician = musicianChoiceBox.getValue();
            ArtistEntity artist = artistChoiceBox.getValue();
            if (!repositoryService.getMusicianGroupRepository().containsMusicianGroupByMusicianAndArtist(musician, artist)) {
                musicianGroup.setId_artist(artistChoiceBox.getValue().getId());
                musicianGroup.setId_musician(musicianChoiceBox.getValue().getId());
                musicianGroup.setStart_date(startDatePicker.getEditor().getText());
                musicianGroup.setEnd_date(endDatePicker.getEditor().getText());
                repositoryService.getMusicianGroupRepository().save(musicianGroup);
            }    
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleCancelButton() {
        dialogStage.close();
    }
    
    @Override
    protected void edit() {     
        add();
        startDatePicker.setValue(startDatePicker.getConverter().fromString(musicianGroup.getStart_date()));
        endDatePicker.setValue(endDatePicker.getConverter().fromString(musicianGroup.getEnd_date()));            
    }
    
    @Override
    protected void add() {     
        if (musicianGroup.getId_musician() != 0) {
            musicianChoiceBox.getSelectionModel().select(repositoryService.getMusicianRepository().selectById(musicianGroup.getId_musician()));
        }
        if (musicianGroup.getId_artist() != 0) {
            artistChoiceBox.getSelectionModel().select(repositoryService.getArtistRepository().selectById(musicianGroup.getId_artist()));
        }     
    }
    
    @Override
    protected boolean isInputValid() {
        String errorMessage = "";
        
        if (musicianChoiceBox.getValue() == null) {
            errorMessage += "Выберите музыканта из списка \n";
        }       
        if (artistChoiceBox.getValue() == null) {
            errorMessage += "Выберите группу из списка \n";
        }
        
        try { 
            startDatePicker.getConverter().fromString(startDatePicker.getEditor().getText());
        } catch (DateTimeParseException e) {
            errorMessage += "Неправильно введён формат Start Date " + startDatePicker.getEditor().getText() +". Надо mm.dd.yyyy \n";
        }     
        try { 
            endDatePicker.getConverter().fromString(endDatePicker.getEditor().getText());
        } catch (DateTimeParseException e) {
            errorMessage += "Неправильно введён формат End Date " + endDatePicker.getEditor().getText() +". Надо mm.dd.yyyy \n";
        }
        
        if (errorMessage.equals("")) {
            return true;
        } 
        else {
            errorMessage(errorMessage);          
            return false;
        }   
    }     
      
    @Override
    public void setEntity(Entity entity) {
        musicianGroup = (MusicianGroupEntity) entity;
        super.setEntity(entity);
    }
       
}
