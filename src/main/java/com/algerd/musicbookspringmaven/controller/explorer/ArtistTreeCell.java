
package com.algerd.musicbookspringmaven.controller.explorer;

import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.algerd.musicbookspringmaven.repository.Album.AlbumEntity;
import com.algerd.musicbookspringmaven.repository.Artist.ArtistEntity;
import com.algerd.musicbookspringmaven.repository.Entity;
import com.algerd.musicbookspringmaven.repository.Song.SongEntity;
import static com.algerd.musicbookspringmaven.Params.DIR_IMAGES;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ArtistTreeCell extends TreeCell<Entity> {
    
    private static final Logger LOG = LogManager.getLogger(ArtistTreeCell.class);

    @Override
    public void updateItem(Entity item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            textProperty().unbind();
            setText(null);
            setGraphic(null);
        } else {
            if (item instanceof ArtistEntity) {
                textProperty().bind(((ArtistEntity) item).nameProperty());
                setGraphic(getIcon("folder.jpg"));
            }
            else if (item instanceof AlbumEntity) {
                textProperty().bind(((AlbumEntity) item).nameProperty());
                setGraphic(getIcon("file.jpg"));
            }
            else if (item instanceof SongEntity) {
                this.textProperty().bind(((SongEntity) item).nameProperty());
                //this.setGraphic(getIcon("file.jpg"));
            }
        }
    }
    
     private ImageView getIcon(String fileName) {
		ImageView imgView = null;
		try {
			String imagePath = DIR_IMAGES + fileName;			
			Image img = new Image(imagePath);
			imgView = new ImageView(img);
		} 
		catch (Exception e) {
			//e.printStackTrace();
            LOG.error("Error: ",  e);
		}
		return imgView;
	}
}
