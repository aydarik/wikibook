package ru.gumerbaev.data;

public record Coordinate(double lat, double lon) {

    @Override
    public String toString() {
        return "Coordinate{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
