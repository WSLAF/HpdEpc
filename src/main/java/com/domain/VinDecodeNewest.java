package com.domain;

public class VinDecodeNewest {
    private String vin;

    private String interior_key_colour_code;

    private String exterior_key_colour_code;

    private String interior_colour_desc;

    private String exterior_colour_desc;

    private String interior_colour_sub_attributes;

    private String exterior_colour_sub_attributes;

    private String catalog_code;

    private String production_date;

    private String model_year;

    private String model;

    private String country;

    private String region;

    private String plant;

    private String section_applicability;

    public String getInterior_colour_sub_attributes() {
        return interior_colour_sub_attributes;
    }

    public void setInterior_colour_sub_attributes(String interior_colour_sub_attributes) {
        this.interior_colour_sub_attributes = interior_colour_sub_attributes;
    }

    public String getExterior_colour_sub_attributes() {
        return exterior_colour_sub_attributes;
    }

    public void setExterior_colour_sub_attributes(String exterior_colour_sub_attributes) {
        this.exterior_colour_sub_attributes = exterior_colour_sub_attributes;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getInterior_key_colour_code() {
        return interior_key_colour_code;
    }

    public void setInterior_key_colour_code(String interior_key_colour_code) {
        this.interior_key_colour_code = interior_key_colour_code;
    }

    public String getExterior_key_colour_code() {
        return exterior_key_colour_code;
    }

    public void setExterior_key_colour_code(String exterior_key_colour_code) {
        this.exterior_key_colour_code = exterior_key_colour_code;
    }

    public String getInterior_colour_desc() {
        return interior_colour_desc;
    }

    public void setInterior_colour_desc(String interior_colour_desc) {
        this.interior_colour_desc = interior_colour_desc;
    }

    public String getExterior_colour_desc() {
        return exterior_colour_desc;
    }

    public void setExterior_colour_desc(String exterior_colour_desc) {
        this.exterior_colour_desc = exterior_colour_desc;
    }

    public String getCatalog_code() {
        return catalog_code;
    }

    public void setCatalog_code(String catalog_code) {
        this.catalog_code = catalog_code;
    }

    public String getProduction_date() {
        return production_date;
    }

    public void setProduction_date(String production_date) {
        this.production_date = production_date;
    }

    public String getModel_year() {
        return model_year;
    }

    public void setModel_year(String model_year) {
        this.model_year = model_year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getSection_applicability() {
        return section_applicability;
    }

    public void setSection_applicability(String section_applicability) {
        this.section_applicability = section_applicability;
    }

    @Override
    public String toString() {
        return "VinDecodeNewest{" +
                "vin='" + vin + '\'' +
                ", interior_key_colour_code='" + interior_key_colour_code + '\'' +
                ", exterior_key_colour_code='" + exterior_key_colour_code + '\'' +
                ", interior_colour_desc='" + interior_colour_desc + '\'' +
                ", exterior_colour_desc='" + exterior_colour_desc + '\'' +
                ", interior_color_sub_attributes='" + interior_colour_sub_attributes + '\'' +
                ", exterior_color_sub_attributes='" + exterior_colour_sub_attributes + '\'' +
                ", catalog_code='" + catalog_code + '\'' +
                ", production_date='" + production_date + '\'' +
                ", model_year='" + model_year + '\'' +
                ", model='" + model + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", plant='" + plant + '\'' +
                ", section_applicability='" + section_applicability + '\'' +
                '}';
    }
}