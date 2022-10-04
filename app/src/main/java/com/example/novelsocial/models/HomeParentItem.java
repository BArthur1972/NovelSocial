package com.example.novelsocial.models;

import java.util.List;

public class HomeParentItem {
    String genreTitle;
    List<HomeChildItem> homeChildItemList;

    public HomeParentItem(String title, List<HomeChildItem> childItems) {
        setGenreTitle(title);
        setHomeChildItemList(childItems);
    }

    public HomeParentItem() {
    }

    public String getGenreTitle() {
        return genreTitle;
    }

    public List<HomeChildItem> getHomeChildItemList() {
        return homeChildItemList;
    }

    public void setGenreTitle(String title) {
        genreTitle = title;
    }

    public void setHomeChildItemList(List<HomeChildItem> childItems) {
        homeChildItemList = childItems;
    }
}
