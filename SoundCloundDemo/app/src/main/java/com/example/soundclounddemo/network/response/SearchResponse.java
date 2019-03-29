package com.example.soundclounddemo.network.response;


public class SearchResponse {
    private long id;
    private String title;
    private String artwork_url;
    private String stream_url;
    private UserJson user;

    public SearchResponse(long id, String title, String artwork_url, String stream_url, UserJson user) {
        this.id = id;
        this.title = title;
        this.artwork_url = artwork_url;
        this.stream_url = stream_url;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtwork_url() {
        return artwork_url;
    }

    public String getStream_url() {
        return stream_url;
    }

    public UserJson getUser() {
        return user;
    }

    public class UserJson{
        private String username;

        public UserJson(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }

}
