package org.example.model;

import cn.hutool.json.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class VehicleInfoDto {
    private String optionCodes;

    private String transmissionCode;

    private String transmissionFactory;

    private String transmissionNumber;

    private String keyNumber;

    private String engineCode;

    private String engineFactory;

    private String modelYearCode;

    private String seatCode;

    private String version;

    private String optionNumber;

    private String nation;

    private String plant;

    private String exteriorKeyColorCode;

    private String interiorKeyColorCode;

    private String engineProductDate;

    private String transmissionProductDate;

    private String area;

    private String vinTag;

    private Boolean localModel;

    private String fullSpecificationCode;

    private String engineMipCode;

    private String transmissionMipCode;

    private String clientCatalogInformationString;

    private String buildDate;

    private String vin;

    private String engineNumber;

    private String displayBuildDate;

    private String modelCode;

    private String modelYear;

    private String modelID;

    private String applicableRegions;

    private Integer match;

    private Integer vehicleDataSource;

    private JSONArray interpretationAVSData;

    private String catalogCode;

    @JsonIgnore
    private JSONArray notesReferenceKeys;

    @JsonIgnore
    private JSONArray vehicleInfoList;

    @JsonIgnore
    private JSONArray userNote;

    private String type;

    public JSONArray getUserNote() {
        return userNote;
    }

    public void setUserNote(JSONArray userNote) {
        this.userNote = userNote;
    }

    public JSONArray getVehicleInfoList() {
        return this.vehicleInfoList;
    }

    public void setVehicleInfoList(JSONArray vehicleInfoList) {
        this.vehicleInfoList = vehicleInfoList;
    }

    public JSONArray getNotesReferenceKeys() {
        return notesReferenceKeys;
    }

    public void setNotesReferenceKeys(JSONArray notesReferenceKeys) {
        this.notesReferenceKeys = notesReferenceKeys;
    }

    public String getOptionCodes() {
        return optionCodes;
    }

    public void setOptionCodes(String optionCodes) {
        this.optionCodes = optionCodes;
    }

    public String getTransmissionCode() {
        return transmissionCode;
    }

    public void setTransmissionCode(String transmissionCode) {
        this.transmissionCode = transmissionCode;
    }

    public String getTransmissionFactory() {
        return transmissionFactory;
    }

    public void setTransmissionFactory(String transmissionFactory) {
        this.transmissionFactory = transmissionFactory;
    }

    public String getTransmissionNumber() {
        return transmissionNumber;
    }

    public void setTransmissionNumber(String transmissionNumber) {
        this.transmissionNumber = transmissionNumber;
    }

    public String getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(String keyNumber) {
        this.keyNumber = keyNumber;
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }

    public String getEngineFactory() {
        return engineFactory;
    }

    public void setEngineFactory(String engineFactory) {
        this.engineFactory = engineFactory;
    }

    public String getModelYearCode() {
        return modelYearCode;
    }

    public void setModelYearCode(String modelYearCode) {
        this.modelYearCode = modelYearCode;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOptionNumber() {
        return optionNumber;
    }

    public void setOptionNumber(String optionNumber) {
        this.optionNumber = optionNumber;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getExteriorKeyColorCode() {
        return exteriorKeyColorCode;
    }

    public void setExteriorKeyColorCode(String exteriorKeyColorCode) {
        this.exteriorKeyColorCode = exteriorKeyColorCode;
    }

    public String getInteriorKeyColorCode() {
        return interiorKeyColorCode;
    }

    public void setInteriorKeyColorCode(String interiorKeyColorCode) {
        this.interiorKeyColorCode = interiorKeyColorCode;
    }

    public String getEngineProductDate() {
        return engineProductDate;
    }

    public void setEngineProductDate(String engineProductDate) {
        this.engineProductDate = engineProductDate;
    }

    public String getTransmissionProductDate() {
        return transmissionProductDate;
    }

    public void setTransmissionProductDate(String transmissionProductDate) {
        this.transmissionProductDate = transmissionProductDate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getVinTag() {
        return vinTag;
    }

    public void setVinTag(String vinTag) {
        this.vinTag = vinTag;
    }

    public Boolean getLocalModel() {
        return localModel;
    }

    public void setLocalModel(Boolean localModel) {
        this.localModel = localModel;
    }

    public String getFullSpecificationCode() {
        return fullSpecificationCode;
    }

    public void setFullSpecificationCode(String fullSpecificationCode) {
        this.fullSpecificationCode = fullSpecificationCode;
    }

    public String getEngineMipCode() {
        return engineMipCode;
    }

    public void setEngineMipCode(String engineMipCode) {
        this.engineMipCode = engineMipCode;
    }

    public String getTransmissionMipCode() {
        return transmissionMipCode;
    }

    public void setTransmissionMipCode(String transmissionMipCode) {
        this.transmissionMipCode = transmissionMipCode;
    }

    public String getClientCatalogInformationString() {
        return clientCatalogInformationString;
    }

    public void setClientCatalogInformationString(String clientCatalogInformationString) {
        this.clientCatalogInformationString = clientCatalogInformationString;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getDisplayBuildDate() {
        return displayBuildDate;
    }

    public void setDisplayBuildDate(String displayBuildDate) {
        this.displayBuildDate = displayBuildDate;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public String getApplicableRegions() {
        return applicableRegions;
    }

    public void setApplicableRegions(String applicableRegions) {
        this.applicableRegions = applicableRegions;
    }

    public Integer getMatch() {
        return match;
    }

    public void setMatch(Integer match) {
        this.match = match;
    }

    public Integer getVehicleDataSource() {
        return vehicleDataSource;
    }

    public void setVehicleDataSource(Integer vehicleDataSource) {
        this.vehicleDataSource = vehicleDataSource;
    }

    public JSONArray getInterpretationAVSData() {
        return interpretationAVSData;
    }

    public void setInterpretationAVSData(JSONArray interpretationAVSData) {
        this.interpretationAVSData = interpretationAVSData;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}