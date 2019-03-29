package com.example.soundclounddemo.network.response;

import com.google.android.exoplayer2.extractor.mp4.Track;

import java.util.List;

public class SearchPaginationResponse {

    private List<TrackJson> collection;
    private String next_href;

    public SearchPaginationResponse(List<TrackJson> collection, String next_href) {
        this.collection = collection;
        this.next_href = next_href;
    }

    public List<TrackJson> getCollection() {
        return collection;
    }

    public String getNext_href() {
        return next_href;
    }

    public class TrackJson{
        private long id;
        private String title;
        private String artwork_url;
        private String stream_url;
        private SearchResponse.UserJson user;

        public TrackJson(long id, String title, String artwork_url, String stream_url, SearchResponse.UserJson user) {
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

        public SearchResponse.UserJson getUser() {
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
}
