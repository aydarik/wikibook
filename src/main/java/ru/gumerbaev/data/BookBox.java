package ru.gumerbaev.data;

public record BookBox(String address, String imageUrl, Coordinate coordinate, String type, String comment) {

    @Override
    public String toString() {
        return "BookBox{" +
                "address='" + address + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", coordinate=" + coordinate +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
