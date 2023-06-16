package ru.hh.techradar.model;

import com.opencsv.bean.CsvBindByName;
import java.util.Objects;
import java.util.StringJoiner;

public class RadarItem {

  @CsvBindByName(column = "name")
  private String blipName;
  @CsvBindByName(column = "ring")
  private String ringName;
  @CsvBindByName(column = "quadrant")
  private String quadrantName;
  @CsvBindByName(column = "ringPosition")
  private Integer ringPosition;
  @CsvBindByName(column = "quadrantPosition")
  private Integer quadrantPosition;
  @CsvBindByName(column = "description")
  private String description;

  public RadarItem() {
  }

  public String getBlipName() {
    return blipName;
  }

  public void setBlipName(String blipName) {
    this.blipName = blipName;
  }

  public String getRingName() {
    return ringName;
  }

  public void setRingName(String ringName) {
    this.ringName = ringName;
  }

  public String getQuadrantName() {
    return quadrantName;
  }

  public void setQuadrantName(String quadrantName) {
    this.quadrantName = quadrantName;
  }

  public Integer getRingPosition() {
    return ringPosition;
  }

  public void setRingPosition(Integer ringPosition) {
    this.ringPosition = ringPosition;
  }

  public Integer getQuadrantPosition() {
    return quadrantPosition;
  }

  public void setQuadrantPosition(Integer quadrantPosition) {
    this.quadrantPosition = quadrantPosition;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RadarItem radarItem = (RadarItem) o;
    return Objects.equals(blipName, radarItem.blipName)
        && Objects.equals(ringName, radarItem.ringName)
        && Objects.equals(quadrantName, radarItem.quadrantName)
        && Objects.equals(ringPosition, radarItem.ringPosition)
        && Objects.equals(quadrantPosition, radarItem.quadrantPosition)
        && Objects.equals(description, radarItem.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(blipName, ringName, quadrantName, ringPosition, quadrantPosition, description);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", RadarItem.class.getSimpleName() + "[", "]")
        .add("blipName='" + blipName + "'")
        .add("ringName='" + ringName + "'")
        .add("quadrantName='" + quadrantName + "'")
        .add("ringPosition=" + ringPosition)
        .add("quadrantPosition=" + quadrantPosition)
        .add("description='" + description + "'")
        .toString();
  }
}
