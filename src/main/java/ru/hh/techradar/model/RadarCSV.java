package ru.hh.techradar.model;

import com.opencsv.bean.CsvBindByName;

public class RadarCSV {

  @CsvBindByName(column = "name")
  private String blipName;
  @CsvBindByName(column = "ring")
  private String ringName;
  @CsvBindByName(column = "quadrant")
  private String quadrantName;
  @CsvBindByName(column = "isNew")
  private String isNew;
  @CsvBindByName(column = "description")
  private String description;

  public RadarCSV() {
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

  public String getIsNew() {
    return isNew;
  }

  public void setIsNew(String isNew) {
    this.isNew = isNew;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "RadarCSV{" +
        "blipName='" + blipName + '\'' +
        ", ringName='" + ringName + '\'' +
        ", quadrantName='" + quadrantName + '\'' +
        ", isNew='" + isNew + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
