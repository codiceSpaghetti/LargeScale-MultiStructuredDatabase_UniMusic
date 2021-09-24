package it.unipi.dii.inginf.lsmdb.unimusic.frontend.gui;

import it.unipi.dii.inginf.lsmdb.unimusic.frontend.MiddlewareConnector;
import it.unipi.dii.inginf.lsmdb.unimusic.middleware.entities.Playlist;
import it.unipi.dii.inginf.lsmdb.unimusic.middleware.entities.Song;
import it.unipi.dii.inginf.lsmdb.unimusic.middleware.entities.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class homepageController implements Initializable {
    private static MiddlewareConnector connector = MiddlewareConnector.getInstance();

    @FXML private ScrollPane scrollPane;
    @FXML private VBox verticalScroll;

    @FXML private ScrollPane hotSongsScroll;
    @FXML private HBox hotSongsPane;

    @FXML private ScrollPane suggPlaylistsScroll;
    @FXML private HBox suggPlaylistsPane;

    @FXML private ScrollPane suggUsersScroll;
    @FXML private HBox suggUsersPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        scrollPane.setFitToWidth(true);
        verticalScroll.setSpacing(75);

        hotSongsScroll.setFitToHeight(true); hotSongsScroll.setMinViewportHeight(340);
        hotSongsPane.setSpacing(5);

        suggPlaylistsScroll.setFitToHeight(true); suggPlaylistsScroll.setMinViewportHeight(340);
        suggPlaylistsPane.setSpacing(5);

        suggUsersScroll.setFitToHeight(true); suggUsersScroll.setMinViewportHeight(200);
        suggUsersPane.setSpacing(5);

        displayHotSongs();
        displaySuggPlaylists();
        displaySuggUsers();

    }

    //--------------------------------------------------------------------------------------------------------

    private void displayHotSongs() {
        List<Song> hotSongs = connector.getHotSongs();
        if(hotSongs.size() == 0)
            displayEmpty(hotSongsPane);
        else {
            for(Song song: hotSongs) {
                hotSongsPane.getChildren().add(createSongPreview(song));
            }
        }
    }

    private void displaySuggPlaylists() {
        List<Playlist> sugPlaylists = connector.getSuggestedPlaylists();
        if(sugPlaylists.size() == 0)
            displayEmpty(suggPlaylistsPane);
        else {
            for(Playlist playlist: sugPlaylists) {
                suggPlaylistsPane.getChildren().add(createPlaylistPreview(playlist));
            }
        }
    }

    private void displaySuggUsers() {
        List<User> suggUsers = connector.getSuggestedUsers();
        if(suggUsers.size() == 0)
            displayEmpty(suggUsersPane);
        else {
            for(User user: suggUsers) {
                suggUsersPane.getChildren().add(createUserPreview(user));
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------

    private void displayEmpty(Pane pane) {
        pane.getChildren().clear();

        Text emptyText = new Text("<EMPTY>");
        emptyText.setStyle("-fx-font-size: 28");
        emptyText.getStyleClass().add("text-id");

        pane.getChildren().add(emptyText);
    }

    private Button createSongPreview(Song song) {
        Button songPreview = new Button(); songPreview.setStyle("-fx-background-color: transparent");
        songPreview.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    songPageController.getSongPage(song);
                } catch (IOException e) {

                }
            }
        });

        Image songImage;
        VBox songGraphic = new VBox(5);
        try {
            if(song.getAlbum().getImage() == null || song.getAlbum().getImage().equals(""))
                throw new Exception();

            songImage = new Image(
                    song.getAlbum().getImage(),
                    App.previewImageWidth,
                    0,
                    true,
                    true,
                    true
            );

            if(songImage.isError()) {
                throw new Exception();
            }

        }catch(Exception ex){
            songImage = new Image(
                    "file:src/main/resources/it/unipi/dii/inginf/lsmdb/unimusic/frontend/gui/img/empty.jpg",
                    App.previewImageWidth,
                    0,
                    true,
                    true,
                    true
            );
        }

        ImageView songImageView = new ImageView(songImage);
        Text title = new Text(song.getTitle()); title.setWrappingWidth(App.previewImageWidth); title.setFill(Color.WHITE);
        Text artist = new Text(song.getArtist()); artist.setWrappingWidth(App.previewImageWidth); artist.setFill(Color.GRAY);

        songGraphic.getChildren().addAll(songImageView, title, artist);

        songPreview.setGraphic(songGraphic);
        return songPreview;
    }

    private Button createPlaylistPreview(Playlist playlist) {
        Button playlistPreview = new Button(); playlistPreview.setStyle("-fx-background-color: transparent");
        playlistPreview.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    playlistPageController.getPlaylistPage(playlist);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Image playlistImage;
        VBox playlistGraphic = new VBox(5);
        try {

            if(playlist.getUrlImage() == null || playlist.getUrlImage().equals("")) {
                throw new Exception();
            }

            playlistImage = new Image(
                    playlist.getUrlImage(),
                    App.previewImageWidth,
                    0,
                    true,
                    true,
                    true
            );

            if(playlistImage.isError()) {
                throw new Exception();
            }

        }catch(Exception ex){
            playlistImage = new Image(
                    "file:src/main/resources/it/unipi/dii/inginf/lsmdb/unimusic/frontend/gui/img/empty.jpg",
                    App.previewImageWidth,
                    0,
                    true,
                    true,
                    true
            );
        }
        ImageView playlistImageView = new ImageView(playlistImage);
        Text pName = new Text(playlist.getName()); pName.setWrappingWidth(App.previewImageWidth); pName.setFill(Color.WHITE);

        playlistGraphic.getChildren().addAll(playlistImageView, pName);

        playlistPreview.setGraphic(playlistGraphic);
        return playlistPreview;
    }

    private Button createUserPreview(User user) {
        Button songPreview = new Button(); songPreview.setStyle("-fx-background-color: transparent");
        songPreview.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    userPageController.getUserPage(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Image userImage = new Image(
                "file:src/main/resources/it/unipi/dii/inginf/lsmdb/unimusic/frontend/gui/img/user.png",
                App.previewImageWidth,
                0,
                true,
                true,
                true
        );
        ImageView userImageView = new ImageView(userImage);
        Text username = new Text(user.getUsername()); username.setWrappingWidth(App.previewImageWidth); username.setFill(Color.WHITE);

        VBox userGraphic = new VBox(5); userGraphic.getChildren().addAll(userImageView, username);
        songPreview.setGraphic(userGraphic);
        return songPreview;
    }
}