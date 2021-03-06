
package com.algerd.musicbookspringmaven.repository.Album;

import com.algerd.musicbookspringmaven.repository.Entity;
import com.algerd.musicbookspringmaven.repository.annotation.Column;
import com.algerd.musicbookspringmaven.repository.annotation.Table;
import com.algerd.musicbookspringmaven.repository.Artist.ArtistEntity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Table("album")
public class AlbumEntity extends Entity {
    @Column("id")   
    private int id = 0;
    @Column("id_artist")
    private int id_artist = 1; // id = 1 "Unknown" artist
    @Column("name")
    private final StringProperty name = new SimpleStringProperty("");
    @Column("year")
    private final IntegerProperty year = new SimpleIntegerProperty(0);
    @Column("time")
    private final StringProperty time = new SimpleStringProperty("");
    @Column("rating")
    private final IntegerProperty rating = new SimpleIntegerProperty(0);
    @Column("description")
    private final StringProperty description = new SimpleStringProperty("");  
    
    private ArtistEntity artist;

    public AlbumEntity() {
        super();
    }
   
    @Override
	public boolean equals(Object obj) {        
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof AlbumEntity) {
            Entity entity = (AlbumEntity) obj;
            if (entity.getId() == getId()) {
                return true;
            }
        }    
		return false;
	}
    
    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getId_artist() {
        return id_artist;
    }
    public void setId_artist(int id_artist) {
        this.id_artist = id_artist;
    }

    @Override
    public String getName() {
        return name.get();
    }
    public void setName(String value) {
        name.set(value);
    }
    public StringProperty nameProperty() {
        return name;
    }
    
    public int getYear() {
        return year.get();
    }
    public void setYear(int value) {
        year.set(value);
    }
    public IntegerProperty yearProperty() {
        return year;
    }

    public String getTime() {
        return time.get();
    }
    public void setTime(String value) {
        time.set(value);
    }
    public StringProperty timeProperty() {
        return time;
    }

    public int getRating() {
        return rating.get();
    }
    public void setRating(int value) {
        rating.set(value);
    }
    public IntegerProperty ratingProperty() {
        return rating;
    }
    
    public String getDescription() {
        return description.get();
    }
    public void setDescription(String value) {
        description.set(value);
    }
    public StringProperty descriptionProperty() {
        return description;
    }

    public ArtistEntity getArtist() {
        return artist;
    }
    public void setArtist(ArtistEntity artist) {
        this.artist = artist;
    }
    
    @Override
    public String toString() {
        return getName();
    }   
              
}
